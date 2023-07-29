package com.yohwan.webtoon.dto;

import com.yohwan.webtoon.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String userName;
    private String userEmail;

    public UserResponse(User user) {
        this(user.getId(), user.getUserName(), user.getUserEmail());
    }

    public UserResponse(Long id, String userName, String userEmail) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
