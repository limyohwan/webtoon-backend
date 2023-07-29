package com.yohwan.webtoon.service;

import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.dto.ViewHistoryResponse;
import com.yohwan.webtoon.repository.ContentsRepository;
import com.yohwan.webtoon.repository.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ViewHistoryService {
    private final ViewHistoryRepository viewHistoryRepository;
    private final ContentsRepository contentsRepository;

    @Transactional(readOnly = true)
    public List<ViewHistoryResponse> findViewHistories(Long contentsId) {
        Contents contents = validateContents(contentsId);

        return viewHistoryRepository.findByContents(contents).stream()
                .map(ViewHistoryResponse::new).collect(Collectors.toList());
    }

    private Contents validateContents(Long contentsId) {
        return contentsRepository.findById(contentsId).orElseThrow(() -> new IllegalStateException("콘텐츠가 존재하지 않습니다"));
    }
}
