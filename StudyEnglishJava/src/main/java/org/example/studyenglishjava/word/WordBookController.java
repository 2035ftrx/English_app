package org.example.studyenglishjava.word;

import lombok.val;
import org.example.studyenglishjava.config.PageConfig;
import org.example.studyenglishjava.dto.ApiPageResponse;
import org.example.studyenglishjava.dto.WordBookDto;
import org.example.studyenglishjava.entity.WordBook;
import org.example.studyenglishjava.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/books")
public class WordBookController {
    // logger
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WordBookController.class);

    @Autowired
    private WordBookRepository wordBookRepository;

    @GetMapping("list")
    public ApiPageResponse<WordBookDto> list(
            @RequestParam(value = "page", defaultValue = PageConfig.PAGE_START) int page,
            @RequestParam(value = "size", defaultValue = PageConfig.PAGE_SIZE) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        val books = wordBookRepository.findWordBooksBy(pageable);
        return new ApiPageResponse<>(
                books.getTotalPages(),
                books.getNumber(),
                books.getTotalElements(),
                books.getContent().stream().map(WordBook::toDTO).toList());
    }

    @GetMapping("get")
    public WordBookDto get(@RequestParam(value = "bookId") long bookId) throws NotFoundException {
        return wordBookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(" not found book . " + bookId))
                .toDTO();
    }

}