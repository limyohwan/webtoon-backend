package com.yohwan.webtoon.domain;

import com.yohwan.webtoon.domain.constants.AgeType;
import com.yohwan.webtoon.domain.constants.ChargeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contents_id")
    private Long id;

    @Column(name = "name")
    private String contentsName;

    private String author;

    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    @Enumerated(EnumType.STRING)
    private AgeType ageType;

    private int coin;

    private LocalDateTime openDate;

    @Builder
    public Contents(String contentsName, String author, ChargeType chargeType, AgeType ageType, int coin, LocalDateTime openDate) {
        this.contentsName = contentsName;
        this.author = author;
        this.chargeType = chargeType;
        this.ageType = ageType;
        this.coin = coin;
        this.openDate = openDate;
    }

    public void update(ChargeType chargeType, Integer coin) {
        Assert.notNull(chargeType, "청구타입은 필수입니다.");

        this.chargeType = chargeType;
        if(chargeType == ChargeType.FREE) {
            this.coin = 0;
        } else if(chargeType == ChargeType.PAY) {
            Assert.isTrue(coin >= 100, "금액은 100원보다 커야합니다.");
            Assert.isTrue(coin <= 500, "금액은 500원보다 작아야합니다.");
            this.coin = coin;
        }
    }
}
