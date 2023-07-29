package com.yohwan.webtoon.service;

import com.yohwan.webtoon.ServiceTest;
import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.Evaluation;
import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.domain.constants.AgeType;
import com.yohwan.webtoon.domain.constants.ChargeType;
import com.yohwan.webtoon.domain.constants.EvaluationType;
import com.yohwan.webtoon.domain.constants.GenderType;
import com.yohwan.webtoon.dto.EvaluationSaveRequest;
import com.yohwan.webtoon.repository.ContentsRepository;
import com.yohwan.webtoon.repository.EvaluationRepository;
import com.yohwan.webtoon.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ServiceTest
class EvaluationServiceTest {
    @Autowired
    private ContentsRepository contentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluationService evaluationService;


    private Long savedContentsId;

    private Long savedUserId;

    @BeforeEach
    void setUp() {
        saveContents();
        saveUser();
    }

    private Contents saveContents() {
        Contents savedContents = contentsRepository.save(Contents.builder()
                .contentsName("새로운컨텐츠")
                .author("요환")
                .coin(0)
                .openDate(LocalDateTime.now())
                .chargeType(ChargeType.FREE)
                .build());
        this.savedContentsId = savedContents.getId();
        return savedContents;
    }

    private User saveUser() {
        User savedUser = userRepository.save(User.builder()
                .userName("임요환")
                .userEmail("dyghks7102@naver.com")
                .gender(GenderType.MALE)
                .ageType(AgeType.ADULT)
                .registerDate(LocalDateTime.now())
                .build());
        this.savedUserId = savedUser.getId();
        return savedUser;
    }

    @AfterEach
    void tearDown() {
        evaluationRepository.deleteAll();
        contentsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("평가를 저장함")
    void saveEvaluation() {
        EvaluationType saveType = EvaluationType.LIKE;
        String saveComment = "댓글입니다";
        EvaluationSaveRequest response = new EvaluationSaveRequest();
        response.setEvaluationType(saveType);
        response.setComment(saveComment);

        Long evaluationId = evaluationService.saveEvaluation(savedContentsId, response, savedUserId);

        Evaluation savedEvaluation = evaluationRepository.findById(evaluationId).get();

        assertThat(savedEvaluation.getEvaluationType()).isEqualTo(saveType);
        assertThat(savedEvaluation.getComment()).isEqualTo(saveComment);
    }

    @Test
    @DisplayName("평가를 저장함(댓글은 제외)")
    void saveEvaluationWithoutComment() {
        EvaluationType saveType = EvaluationType.LIKE;
        EvaluationSaveRequest response = new EvaluationSaveRequest();
        response.setEvaluationType(saveType);

        Long evaluationId = evaluationService.saveEvaluation(savedContentsId, response, savedUserId);

        Evaluation savedEvaluation = evaluationRepository.findById(evaluationId).get();

        assertThat(savedEvaluation.getEvaluationType()).isEqualTo(saveType);
    }

    @Test
    @DisplayName("평가를 저장함(좋아요/싫어요는 필수)")
    void saveEvaluationThrowsNoType() {
        EvaluationSaveRequest response = new EvaluationSaveRequest();
        assertThrows(IllegalArgumentException.class, () -> {
            evaluationService.saveEvaluation(savedContentsId, response, savedUserId);
        });
    }

    @Test
    @DisplayName("같은 컨텐츠 중복 평가시 에러 발생")
    void saveEvaluationToDuplicatedContents() {
        EvaluationType saveType = EvaluationType.LIKE;
        String saveComment = "댓글입니다";
        EvaluationSaveRequest response = new EvaluationSaveRequest();
        response.setEvaluationType(saveType);
        response.setComment(saveComment);

        evaluationService.saveEvaluation(savedContentsId, response, savedUserId);

        assertThrows(IllegalStateException.class, () -> {
            evaluationService.saveEvaluation(savedContentsId, response, savedUserId);
        });
    }

    @Test
    @DisplayName("평가를 저장할 시 댓글의 특수문자는 제거됨")
    void saveEvaluationWithoutSpecialCharacters() {
        EvaluationType saveType = EvaluationType.LIKE;
        String saveComment = "댓 글 입 니 다ㄷㄷ";
        String commentWithSpecialCharacters = "^&*(()" + saveComment + "!@#$%";
        EvaluationSaveRequest response = new EvaluationSaveRequest();
        response.setEvaluationType(saveType);
        response.setComment(commentWithSpecialCharacters);

        Long evaluationId = evaluationService.saveEvaluation(savedContentsId, response, savedUserId);

        Evaluation savedEvaluation = evaluationRepository.findById(evaluationId).get();

        assertThat(savedEvaluation.getEvaluationType()).isEqualTo(saveType);
        assertThat(savedEvaluation.getComment()).isEqualTo(saveComment);
    }
}