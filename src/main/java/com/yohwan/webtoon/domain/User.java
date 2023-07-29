package com.yohwan.webtoon.domain;

import com.yohwan.webtoon.domain.constants.AgeType;
import com.yohwan.webtoon.domain.constants.GenderType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String userName;

    @Column(name = "email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    private AgeType ageType;

    @Column(updatable = false)
    private LocalDateTime registerDate;

    @Builder
    public User(String userName, String userEmail, GenderType gender, AgeType ageType, LocalDateTime registerDate) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.gender = gender;
        this.ageType = ageType;
        this.registerDate = registerDate;
    }
}
