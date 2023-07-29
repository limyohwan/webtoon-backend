package com.yohwan.webtoon.dto;

import com.yohwan.webtoon.domain.constants.EvaluationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EvaluationSaveRequest {
    private EvaluationType evaluationType;
    private String comment;
}
