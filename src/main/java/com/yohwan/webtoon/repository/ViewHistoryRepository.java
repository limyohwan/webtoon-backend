package com.yohwan.webtoon.repository;

import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.domain.ViewHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
    @EntityGraph(attributePaths = {"contents", "user"})
    List<ViewHistory> findByContents(Contents contents);

    List<ViewHistory> findByUser(User user);
}
