package com.yohwan.webtoon.controller;

import com.yohwan.webtoon.common.ApiResponse;
import com.yohwan.webtoon.common.config.auth.AuthenticationUserId;
import com.yohwan.webtoon.dto.*;
import com.yohwan.webtoon.service.ContentsService;
import com.yohwan.webtoon.service.EvaluationService;
import com.yohwan.webtoon.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ContentsController {
    private final ContentsService contentsService;
    private final EvaluationService evaluationService;
    private final ViewHistoryService viewHistoryService;

    @PostMapping("/contents/{contentsId}/evaluation")
    public ResponseEntity<ApiResponse<Long>> saveEvaluation(@PathVariable Long contentsId, @RequestBody EvaluationSaveRequest request, @AuthenticationUserId Long userId) {
        return ResponseEntity.ok(new ApiResponse<>(evaluationService.saveEvaluation(contentsId, request, userId)));
    }

    @PutMapping("/contents/{contentsId}")
    public ResponseEntity<ApiResponse<Long>> updateContents(@PathVariable Long contentsId, @RequestBody ContentsUpdateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(contentsService.updateContents(contentsId, request)));
    }

    @GetMapping("/contents/{contentsId}/view-histories")
    public ResponseEntity<ApiResponse<List<ViewHistoryResponse>>> findViewHistories(@PathVariable Long contentsId) {
        return ResponseEntity.ok(new ApiResponse<>(viewHistoryService.findViewHistories(contentsId)));
    }

    @GetMapping("/contents/top-rated")
    public ResponseEntity<ApiResponse<List<ContentsResponse>>> findTopRatedContents(@ModelAttribute TopRatedContentsSearchRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(contentsService.findTopRatedContents(request)));
    }
}
