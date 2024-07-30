package org.example.studyenglishjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@Entity
public class AIToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    @Column(columnDefinition = "JSON")
    private String token;

    private Long createdAt;


}
