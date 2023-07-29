package com.yohwan.webtoon.dto;

import com.yohwan.webtoon.domain.ViewHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ViewHistoryResponse {
    private Long id;
    private ContentsResponse contents;
    private UserResponse user;
    private LocalDateTime createdDate;

    public ViewHistoryResponse(ViewHistory viewHistory) {
        this(
                viewHistory.getId(),
                new ContentsResponse(viewHistory.getContents()),
                new UserResponse(viewHistory.getUser()),
                viewHistory.getCreatedDate()
        );
    }

    public ViewHistoryResponse(Long id, ContentsResponse contents, UserResponse user, LocalDateTime createdDate) {
        this.id = id;
        this.contents = contents;
        this.user = user;
        this.createdDate = createdDate;
    }
}
