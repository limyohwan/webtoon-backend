package com.yohwan.webtoon.service;

import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.Evaluation;
import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.dto.EvaluationSaveRequest;
import com.yohwan.webtoon.repository.ContentsRepository;
import com.yohwan.webtoon.repository.EvaluationRepository;
import com.yohwan.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class EvaluationService {
    private final ContentsRepository contentsRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private static final String REGEX = "[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]";

    public Long saveEvaluation(Long contentsId, EvaluationSaveRequest request, Long userId) {
        Contents contents = validateContents(contentsId);
        User user = validateUser(userId);

        evaluationRepository.findByContentsAndUser(contents, user).ifPresent(evaluation -> {
            throw new IllegalStateException("해당 콘텐츠에 대한 평가가 이미 존재합니다");
        });

        return evaluationRepository.save(
                Evaluation.builder()
                        .contents(contents)
                        .user(user)
                        .evaluationType(request.getEvaluationType())
                        .comment(removeSpecialCharacters(request.getComment()))
                        .registerDate(LocalDateTime.now())
                        .build()).getId();
    }

    private Contents validateContents(Long contentsId) {
        return contentsRepository.findById(contentsId).orElseThrow(() -> new IllegalStateException("콘텐츠가 존재하지 않습니다"));
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다"));
    }

    private String removeSpecialCharacters(String comment) {
        return StringUtils.hasText(comment) ? comment.replaceAll(REGEX, "") : comment;
    }
}
