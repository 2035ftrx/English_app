package org.example.studyenglishjava.word;

import com.google.gson.Gson;
import lombok.val;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.studyenglishjava.config.PageConfig;
import org.example.studyenglishjava.dto.ApiPageResponse;
import org.example.studyenglishjava.dto.WordDTO;
import org.example.studyenglishjava.entity.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

@RestController
@RequestMapping("/app/words")
public class WordController {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private WordRepository wordRepository;


    @GetMapping("list")
    public ApiPageResponse<WordDTO> list(
            @RequestParam(value = "bookId") long bookId,
            @RequestParam(value = "page", defaultValue = PageConfig.PAGE_START) int page,
            @RequestParam(value = "size", defaultValue = PageConfig.PAGE_SIZE) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        val works = wordRepository.findWordsByBookIdOrderByWordRank(bookId, pageable);
        return new ApiPageResponse<>(
                works.getTotalPages(),
                works.getNumber(),
                works.getTotalElements(),
                works.getContent().stream().map(Word::toDTO).toList());
    }

    @GetMapping("getWordById")
    public WordDTO getWordById(@RequestParam(value = "wordId") long wordId) {
        val work = wordRepository.findById(wordId);
        return work.orElseThrow().toDTO();
    }

    @GetMapping("getWordByRank")
    public WordDTO getWordByRank(@RequestParam(value = "wordRank") int wordRank,
                                 @RequestParam(value = "bookId") long bookId) {
        val work = wordRepository.findByBookIdAndWordRank(bookId, wordRank);
        return work.orElseThrow().toDTO();
    }


    @GetMapping("allwordsfile")
    public ResponseEntity<StreamingResponseBody> allwordsfile(@RequestParam(value = "bookId") long bookId) {

        val works = wordRepository.findByBookId(bookId);
        logger.info(" works : " + works);

        File jsonFile = exportJsonFile(works);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + jsonFile.getName());

        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream inputStream = new FileInputStream(jsonFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                }
            } catch (IOException e) {
                // 异常处理
                e.printStackTrace();
            }
        };

        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }

    private File exportJsonFile(Object obj) {
        Gson gson = new Gson();
        // 将数据写入JSON文件
        try {
            File jsonFile = File.createTempFile("words", ".json");
            FileWriter writer = new FileWriter(jsonFile);
            gson.toJson(obj, writer);
            return jsonFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}