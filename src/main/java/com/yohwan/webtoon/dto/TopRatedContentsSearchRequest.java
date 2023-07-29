package com.yohwan.webtoon.dto;

import com.yohwan.webtoon.domain.constants.EvaluationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TopRatedContentsSearchRequest {
    private EvaluationType evaluationType;
    private int limit;
}
