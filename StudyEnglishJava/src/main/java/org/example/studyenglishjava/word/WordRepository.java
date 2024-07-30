package org.example.studyenglishjava.word;

import org.example.studyenglishjava.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findWordsByBookIdOrderByWordRank(Long bookId, Pageable pageable);

    List<Word> findByBookId(Long bookId);

    Optional<Word> findByBookIdAndWordRank(Long bookId, Integer wordRank);

    // 根据 bookId 和 ids 查询，要求结果不在 ids 中，限返回 limit 条。
    List<Word> findByBookIdAndIdNotInOrderByWordRank(Long bookId, List<Long> ids, Pageable pageable);

    List<Word> findByIdIn(List<Long> ids);

    int countByBookId(Long bookId);


}
