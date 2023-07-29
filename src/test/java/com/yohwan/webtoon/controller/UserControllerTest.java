package com.yohwan.webtoon.controller;

import com.yohwan.webtoon.ControllerTest;
import com.yohwan.webtoon.common.ApiResponse;
import com.yohwan.webtoon.domain.Evaluation;
import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.domain.ViewHistory;
import com.yohwan.webtoon.dto.UserResponse;
import com.yohwan.webtoon.repository.EvaluationRepository;
import com.yohwan.webtoon.repository.UserRepository;
import com.yohwan.webtoon.repository.ViewHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@ControllerTest
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ViewHistoryRepository viewHistoryRepository;
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:";

    @Test
    @DisplayName("최근 일주일간 등록 사용자 중 성인작품 3개 이상 조회한 사용자 목록 조회")
    void findAdultSearchUsersWithinLastWeek() {
        String apiUrl = "/users/adult-search/recent";

        String url = BASE_URL + port + apiUrl;

        ResponseEntity<ApiResponse<List<UserResponse>>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().size()).isEqualTo(2);
        assertThat(result.getBody().getData())
                .map(UserResponse::getId)
                .contains(1L,3L);
    }

    @Test
    @DisplayName("사용자 삭제시 해당 사용자 정보와 사용자 평가, 조회 이력까지 모두 삭제")
    void deleteUser() {
        Long deleteUserId = 1L;

        User deleteUser = findUser(deleteUserId);

        String apiUrl = "/users/" + deleteUserId;

        String url = BASE_URL + port + apiUrl;

        ResponseEntity<Void> result = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThrows(IllegalStateException.class, () -> {
            findUser(deleteUserId);
        });

        List<Evaluation> evaluations = evaluationRepository.findByUser(deleteUser);
        assertThat(evaluations.size()).isEqualTo(0);

        List<ViewHistory> viewHistories = viewHistoryRepository.findByUser(deleteUser);
        assertThat(viewHistories.size()).isEqualTo(0);
    }

    private User findUser(Long deleteUserId) {
        return userRepository.findById(deleteUserId).orElseThrow(
                () -> new IllegalStateException("해당 유저가 없습니다")
        );
    }
}