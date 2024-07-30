package org.example.studyenglishjava.admin;

import lombok.val;
import org.example.studyenglishjava.auth.PrintRequestFilter;
import org.example.studyenglishjava.dto.ApiPageResponse;
import org.example.studyenglishjava.dto.WordBookDto;
import org.example.studyenglishjava.dto.WordDTO;
import org.example.studyenglishjava.entity.Word;
import org.example.studyenglishjava.entity.WordBook;
import org.example.studyenglishjava.word.WordBookRepository;
import org.example.studyenglishjava.word.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/app/admin/words")
public class ManagerWordsController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PrintRequestFilter.class);

    @Autowired
    private WordBookRepository wordBookRepository;
    @Autowired
    private WordRepository wordRepository;

    @GetMapping("/wordBooks")
    public ApiPageResponse<WordBookDto> getAllWordBooks(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        val books = wordBookRepository.findWordBooksBy(pageable);
        return new ApiPageResponse<>(
                books.getTotalPages(),
                books.getNumber(),
                books.getTotalElements(),
                books.getContent().stream().map(WordBook::toDTO).toList());
    }

    @GetMapping("/getBook")
    public WordBookDto getWordBookById(@RequestParam Long id) {
        Optional<WordBook> wordBook = wordBookRepository.findById(id);
        return wordBook.orElseThrow().toDTO();
    }

    @PostMapping("/createBook")
    public WordBookDto createWordBook(@RequestBody WordBookDto wordBookDto) {
        WordBook wordBook = new WordBook();
        wordBook.setBookId(wordBookDto.bookId());
        wordBook.setPicUrl(wordBookDto.picUrl());
        wordBook.setTitle(wordBookDto.title());
        wordBook.setWordNum(wordBookDto.wordNum());
        wordBook.setTags(wordBookDto.tags());
        return wordBookRepository.save(wordBook).toDTO();
    }

    @PostMapping("/updateBook")
    public WordBookDto updateWordBook(@RequestParam Long id, @RequestBody WordBookDto wordBookDto) {
        Optional<WordBook> optionalWordBook = wordBookRepository.findById(id);
        WordBook wordBook = optionalWordBook.orElseThrow();
        wordBook.setBookId(wordBookDto.bookId());
        wordBook.setPicUrl(wordBookDto.picUrl());
        wordBook.setTitle(wordBookDto.title());
        wordBook.setWordNum(wordBookDto.wordNum());
        wordBook.setTags(wordBookDto.tags());
        return wordBookRepository.save(wordBook).toDTO();
    }

    @PostMapping("/deleteBook")
    public Long deleteWordBook(@RequestParam Long id) {
        wordBookRepository.deleteById(id);
        return id;
    }

    @GetMapping("/getWordsByBookId")
    public ApiPageResponse<WordDTO> getAllWords(@RequestParam Long bookId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        val books = wordRepository.findWordsByBookIdOrderByWordRank(bookId, pageable);
        return new ApiPageResponse<>(
                books.getTotalPages(),
                books.getNumber(),
                books.getTotalElements(),
                books.getContent().stream().map(Word::toDTO).toList());
    }

    @GetMapping("/getWord")
    public WordDTO getWordById(@RequestParam Long id) {
        Optional<Word> word = wordRepository.findById(id);
        return word.orElseThrow().toDTO();
    }

    @PostMapping("/createWord")
    public WordDTO createWord(@RequestBody WordDTO wordDto) throws UnsupportedEncodingException {
        val count = wordRepository.countByBookId(wordDto.bookId());
        Word word = new Word();
        word.setBookId(wordDto.bookId());
        word.setWordRank(count + 1);
        word.setHeadWord(wordDto.headWord());
        //word.setWord(wordDto.word());
//        word.setWord(HtmlUtils.htmlEscape(wordDto.word(),"UTF-8"));
        val encode = URLEncoder.encode(wordDto.word(), StandardCharsets.UTF_8);
        logger.info(" origin json : "+wordDto.word());
        logger.info(" encode json : "+encode);
        word.setWord(encode);
        wordBookRepository.findById(wordDto.bookId()).ifPresent(wordBook -> {
            wordBook.setWordNum(count + 1);
            wordBookRepository.save(wordBook);
        });
        return wordRepository.save(word).toDTO();
    }

    @PostMapping("/updateWord")
    public WordDTO updateWord(@RequestParam Long id, @RequestBody WordDTO wordDto) {
        Optional<Word> optionalWord = wordRepository.findById(id);
        Word word = optionalWord.orElseThrow();
        word.setWordRank(wordDto.wordRank());
        word.setHeadWord(wordDto.headWord());
        word.setWord(wordDto.word());
        return wordRepository.save(word).toDTO();
    }

    @PostMapping("/deleteWord")
    public Long deleteWord(@RequestParam Long id) {
        val word = wordRepository.findById(id);
        word.ifPresent(w -> {
            wordRepository.deleteById(id);
            val count = wordRepository.countByBookId(w.getBookId());
            wordBookRepository.findById(w.getBookId()).ifPresent(wordBook -> {
                wordBook.setWordNum(count + 1);
                wordBookRepository.save(wordBook);
            });
        });
        return id;
    }

}
