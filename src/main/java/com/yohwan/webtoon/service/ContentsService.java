package com.yohwan.webtoon.service;

import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.dto.ContentsResponse;
import com.yohwan.webtoon.dto.ContentsUpdateRequest;
import com.yohwan.webtoon.dto.TopRatedContentsSearchRequest;
import com.yohwan.webtoon.repository.ContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ContentsService {
    private final ContentsRepository contentsRepository;

    public Long updateContents(Long contentsId, ContentsUpdateRequest request) {
        Contents contents = validateContents(contentsId);
        contents.update(request.getChargeType(), request.getCoin());
        return contents.getId();
    }

    public List<ContentsResponse> findTopRatedContents(TopRatedContentsSearchRequest request) {
        Assert.notNull(request.getEvaluationType(), "좋아요/싫어요는 필수입니다.");
        Assert.isTrue(request.getLimit() > 0, "리밋은 0보다 커야합니다.");
        return contentsRepository.findTopRatedContents(request.getEvaluationType(), PageRequest.of(0, request.getLimit())).stream()
                .map(ContentsResponse::new).collect(Collectors.toList());
    }

    private Contents validateContents(Long contentsId) {
        return contentsRepository.findById(contentsId).orElseThrow(() -> new IllegalStateException("콘텐츠가 존재하지 않습니다"));
    }
}
