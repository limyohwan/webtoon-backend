package com.yohwan.webtoon.service;

import com.yohwan.webtoon.ServiceTest;
import com.yohwan.webtoon.domain.constants.ChargeType;
import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.constants.EvaluationType;
import com.yohwan.webtoon.dto.ContentsResponse;
import com.yohwan.webtoon.dto.ContentsUpdateRequest;
import com.yohwan.webtoon.dto.TopRatedContentsSearchRequest;
import com.yohwan.webtoon.repository.ContentsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SqlGroup({
        @Sql(scripts = "classpath:sql/test-data.sql", config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        , @Sql(scripts = "classpath:sql/truncate.sql", config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@ServiceTest
class ContentsServiceTest {
    @Autowired
    private ContentsRepository contentsRepository;

    @Autowired
    private ContentsService contentsService;

    @Test
    @DisplayName("특정 작품을 유료로 변경함")
    void updateContentsToPay() {
        Long savedContentsId = 1L;
        ChargeType updateType = ChargeType.PAY;
        int updateCoin = 500;
        ContentsUpdateRequest contentsUpdateRequest = new ContentsUpdateRequest();
        contentsUpdateRequest.setChargeType(updateType);
        contentsUpdateRequest.setCoin(updateCoin);
        contentsService.updateContents(savedContentsId, contentsUpdateRequest);

        Contents contents = contentsRepository.findById(savedContentsId).get();
        assertThat(contents.getChargeType()).isEqualTo(updateType);
        assertThat(contents.getCoin()).isEqualTo(updateCoin);
    }

    @Test
    @DisplayName("특정 작품을 무료로 변경함(무료로 변경할시 금액은 무조건 0원)")
    void updateContentsToFree() {
        Long savedContentsId = 1L;
        ChargeType updateType = ChargeType.FREE;
        int updateCoin = 500;

        ContentsUpdateRequest contentsUpdateRequest = new ContentsUpdateRequest();
        contentsUpdateRequest.setChargeType(updateType);
        contentsUpdateRequest.setCoin(updateCoin);
        contentsService.updateContents(savedContentsId, contentsUpdateRequest);

        Contents contents = contentsRepository.findById(savedContentsId).get();
        assertThat(contents.getChargeType()).isEqualTo(updateType);
        assertThat(contents.getCoin()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 작품을 유료로 변경 시 금액은 100원 이상 500원 이하여야함(500원 초과시 에러)")
    void updateContentsToPayThrowsOver500() {
        Long savedContentsId = 1L;
        ChargeType updateType = ChargeType.PAY;
        int updateCoin = 501;

        ContentsUpdateRequest contentsUpdateRequest = new ContentsUpdateRequest();
        contentsUpdateRequest.setChargeType(updateType);
        contentsUpdateRequest.setCoin(updateCoin);
        assertThrows(IllegalArgumentException.class, () -> {
            contentsService.updateContents(savedContentsId, contentsUpdateRequest);
        });
    }

    @Test
    @DisplayName("특정 작품을 유료로 변경 시 금액은 100원 이상 500원 이하여야함(100원 미만시 에러)")
    void updateContentsToPayThrowsUnder100() {
        Long savedContentsId = 1L;
        ChargeType updateType = ChargeType.PAY;
        int updateCoin = 99;

        ContentsUpdateRequest contentsUpdateRequest = new ContentsUpdateRequest();
        contentsUpdateRequest.setChargeType(updateType);
        contentsUpdateRequest.setCoin(updateCoin);
        assertThrows(IllegalArgumentException.class, () -> {
            contentsService.updateContents(savedContentsId, contentsUpdateRequest);
        });
    }

    @Test
    @DisplayName("좋아요 Top 3 콘텐츠 조회")
    void findLikeTop3Contents() {
        TopRatedContentsSearchRequest request = new TopRatedContentsSearchRequest();
        request.setEvaluationType(EvaluationType.LIKE);
        request.setLimit(3);

        List<ContentsResponse> topRatedContents = contentsService.findTopRatedContents(request);

        assertThat(topRatedContents)
                .map(ContentsResponse::getId)
                .contains(4L,5L,6L);
    }

    @Test
    @DisplayName("싫어요 Top 3 콘텐츠 조회")
    void findDislikeTop3Contents() {
        TopRatedContentsSearchRequest request = new TopRatedContentsSearchRequest();
        request.setEvaluationType(EvaluationType.DISLIKE);
        request.setLimit(3);

        List<ContentsResponse> topRatedContents = contentsService.findTopRatedContents(request);

        assertThat(topRatedContents)
                .map(ContentsResponse::getId)
                .contains(1L,2L,3L);
    }
}