package com.yohwan.webtoon.controller;

import com.yohwan.webtoon.ControllerTest;
import com.yohwan.webtoon.common.ApiResponse;
import com.yohwan.webtoon.domain.constants.ChargeType;
import com.yohwan.webtoon.domain.constants.EvaluationType;
import com.yohwan.webtoon.dto.ContentsResponse;
import com.yohwan.webtoon.dto.ContentsUpdateRequest;
import com.yohwan.webtoon.dto.EvaluationSaveRequest;
import com.yohwan.webtoon.dto.ViewHistoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SqlGroup({
        @Sql(scripts = "classpath:sql/test-data.sql", config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        , @Sql(scripts = "classpath:sql/truncate.sql", config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@ControllerTest
class ContentsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:";

    @Test
    @DisplayName("평가를 저장함")
    void saveEvaluation() {
        Long contentsId = 1L;

        EvaluationSaveRequest request = new EvaluationSaveRequest();
        request.setComment("댓글입니다");
        request.setEvaluationType(EvaluationType.LIKE);

        Long userId = 7L;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", String.valueOf(userId));

        HttpEntity<EvaluationSaveRequest> requestHttpEntity = new HttpEntity<>(request, requestHeaders);

        String apiUrl = "/contents/" + contentsId + "/evaluation";

        String url = BASE_URL + port + apiUrl;

        ResponseEntity<ApiResponse<Long>> result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestHttpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("특정 작품을 수정함")
    void updateContents() {
        Long contentsId = 1L;

        ContentsUpdateRequest request = new ContentsUpdateRequest();
        request.setChargeType(ChargeType.FREE);

        HttpEntity<ContentsUpdateRequest> requestHttpEntity = new HttpEntity<>(request);

        String apiUrl = "/contents/" + contentsId;

        String url = BASE_URL + port + apiUrl;

        ResponseEntity<ApiResponse<Long>> result = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestHttpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("특정작품의 조회 이력을 검색함")
    void findViewHistories() {
        Long contentsId = 1L;

        String apiUrl = "/contents/" + contentsId + "/view-histories";

        String url = BASE_URL + port + apiUrl;

        ResponseEntity<ApiResponse<List<ViewHistoryResponse>>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("좋아요 top3 게시글을 조회함")
    void findLikeTop3Contents() {
        String apiUrl = "/contents/top-rated";
        EvaluationType evaluationType = EvaluationType.LIKE;
        int limit = 3;
        String queryParameter = "?evaluationType=" + evaluationType + "&limit=" + limit;

        String url = BASE_URL + port + apiUrl + queryParameter;

        ResponseEntity<ApiResponse<List<ContentsResponse>>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData())
                .map(ContentsResponse::getId)
                .contains(4L,5L,6L);
    }

    @Test
    @DisplayName("싫어요 top3 게시글을 조회함")
    void findDislikeTop3Contents() {
        String apiUrl = "/contents/top-rated";
        EvaluationType evaluationType = EvaluationType.DISLIKE;
        int limit = 3;
        String queryParameter = "?evaluationType=" + evaluationType + "&limit=" + limit;

        String url = BASE_URL + port + apiUrl + queryParameter;

        ResponseEntity<ApiResponse<List<ContentsResponse>>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getData())
                .map(ContentsResponse::getId)
                .contains(1L,2L,3L);
    }
}