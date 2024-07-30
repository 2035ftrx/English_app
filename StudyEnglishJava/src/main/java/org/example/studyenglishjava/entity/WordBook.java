package org.example.studyenglishjava.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.studyenglishjava.dto.WordBookDto;
import org.example.studyenglishjava.dto.WordDTO;

@Setter
@Getter
@ToString
@Entity
public class WordBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookId;
    private String picUrl;
    private String title;
    private Integer wordNum;
    private String tags;

    // todto
    public WordBookDto toDTO() {
        return new WordBookDto(
                id,
                bookId,
                picUrl,
                title,
                wordNum,
                tags
        );
    }
}
