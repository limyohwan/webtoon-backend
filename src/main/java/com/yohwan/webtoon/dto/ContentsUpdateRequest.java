package com.yohwan.webtoon.dto;

import com.yohwan.webtoon.domain.constants.ChargeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentsUpdateRequest {
    private ChargeType chargeType;
    private int coin;
}
