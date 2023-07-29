package com.yohwan.webtoon.repository;

import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.domain.constants.AgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u " +
            "FROM User u " +
            "LEFT JOIN ViewHistory vh " +
            "ON u.id = vh.user.id " +
            "LEFT JOIN Contents c " +
            "ON vh.contents.id = c.id " +
            "WHERE u.registerDate >= :weekAgo " +
            "AND c.ageType = :ageType " +
            "GROUP BY u.id " +
            "HAVING COUNT(DISTINCT(c.id)) >= 3")
    List<User> findAdultSearchUsersWithinLastWeek(@Param("weekAgo") LocalDateTime weekAgo, @Param("ageType") AgeType ageType);
}
