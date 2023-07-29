package com.yohwan.webtoon.repository;

import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.Evaluation;
import com.yohwan.webtoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByContentsAndUser(Contents contents, User user);

    List<Evaluation> findByUser(User user);
}
