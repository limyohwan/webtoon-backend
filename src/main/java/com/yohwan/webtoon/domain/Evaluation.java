package com.yohwan.webtoon.domain;

import com.yohwan.webtoon.domain.constants.EvaluationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "evaluation_uk",
                        columnNames={"user_id", "contents_id"}
                )
        }
)
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    private Contents contents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationType evaluationType;

    private String comment;

    @Column(updatable = false)
    private LocalDateTime registerDate;

    @Builder
    public Evaluation(User user, Contents contents, EvaluationType evaluationType, String comment, LocalDateTime registerDate) {
        Assert.notNull(evaluationType, "좋아요/싫어요는 필수입니다.");
        this.user = user;
        this.contents = contents;
        this.evaluationType = evaluationType;
        this.comment = comment;
        this.registerDate = registerDate;
    }
}
