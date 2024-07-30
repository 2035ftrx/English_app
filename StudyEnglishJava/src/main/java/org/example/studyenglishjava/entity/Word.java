package org.example.studyenglishjava.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.studyenglishjava.dto.WordDTO;

@ToString
@Setter
@Getter
@Entity(name = "word_table")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private Integer wordRank;
    private String headWord;

//    @Column(columnDefinition = "JSON")
    private String word;

    // todto
    public WordDTO toDTO() {
        return new WordDTO(id, bookId, wordRank, headWord, word);
    }
}
