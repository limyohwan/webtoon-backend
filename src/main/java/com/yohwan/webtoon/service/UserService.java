package com.yohwan.webtoon.service;

import com.yohwan.webtoon.domain.constants.AgeType;
import com.yohwan.webtoon.dto.UserResponse;
import com.yohwan.webtoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> findAdultSearchUsersWithinLastWeek() {
        return userRepository.findAdultSearchUsersWithinLastWeek(LocalDateTime.now().minusWeeks(1L), AgeType.ADULT).stream()
                .map(UserResponse::new).collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
