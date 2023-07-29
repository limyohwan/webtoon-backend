package com.yohwan.webtoon.repository;

import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.constants.EvaluationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    @Query("SELECT c FROM Contents c " +
            "LEFT JOIN Evaluation e " +
            "ON c.id = e.contents.id " +
            "WHERE e.evaluationType = :evaluationType " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(e.id) DESC")
    List<Contents> findTopRatedContents(@Param("evaluationType") EvaluationType evaluationType, Pageable pageable);
}
