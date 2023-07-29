package com.yohwan.webtoon.service;

import com.yohwan.webtoon.ServiceTest;
import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.Evaluation;
import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.domain.ViewHistory;
import com.yohwan.webtoon.dto.UserResponse;
import com.yohwan.webtoon.repository.ContentsRepository;
import com.yohwan.webtoon.repository.EvaluationRepository;
import com.yohwan.webtoon.repository.UserRepository;
import com.yohwan.webtoon.repository.ViewHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SqlGroup({
        @Sql(scripts = "classpath:sql/test-data.sql", config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        , @Sql(scripts = "classpath:sql/truncate.sql", config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@ServiceTest
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ViewHistoryRepository viewHistoryRepository;

    @Autowired
    private ContentsRepository contentsRepository;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("최근 일주일간 등록 사용자 중 성인작품 3개 이상 조회한 사용자 목록 조회")
    void findRecentRegisteredUsersFindAdultContents() {
        List<UserResponse> users = userService.findAdultSearchUsersWithinLastWeek();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자 삭제시 해당 사용자 정보와 사용자 평가, 조회 이력까지 모두 삭제")
    void deleteUser() {
        Long deleteUserId = 1L;

        User deleteUser = findUser(deleteUserId);

        userService.deleteUser(deleteUserId);

        assertThrows(IllegalStateException.class, () -> {
            findUser(deleteUserId);
        });

        List<Evaluation> evaluations = evaluationRepository.findByUser(deleteUser);
        assertThat(evaluations.size()).isEqualTo(0);

        List<ViewHistory> viewHistories = viewHistoryRepository.findByUser(deleteUser);
        assertThat(viewHistories.size()).isEqualTo(0);

        List<Contents> contents = contentsRepository.findAll();
        assertThat(contents.size()).isEqualTo(6);
    }

    private User findUser(Long deleteUserId) {
        return userRepository.findById(deleteUserId).orElseThrow(
                () -> new IllegalStateException("해당 유저가 없습니다")
        );
    }

}