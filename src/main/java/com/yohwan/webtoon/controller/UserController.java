package com.yohwan.webtoon.controller;

import com.yohwan.webtoon.common.ApiResponse;
import com.yohwan.webtoon.dto.UserResponse;
import com.yohwan.webtoon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/users/adult-search/recent")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAdultSearchUsersWithinLastWeek() {
        return ResponseEntity.ok(new ApiResponse<>(userService.findAdultSearchUsersWithinLastWeek()));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
