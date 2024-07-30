package org.example.studyenglishjava.word;

import org.example.studyenglishjava.entity.WordBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {

    Page<WordBook> findWordBooksBy(Pageable pageable);

}
