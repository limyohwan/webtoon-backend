package com.yohwan.webtoon.service;

import com.yohwan.webtoon.ServiceTest;
import com.yohwan.webtoon.domain.Contents;
import com.yohwan.webtoon.domain.User;
import com.yohwan.webtoon.domain.ViewHistory;
import com.yohwan.webtoon.domain.constants.AgeType;
import com.yohwan.webtoon.domain.constants.ChargeType;
import com.yohwan.webtoon.domain.constants.GenderType;
import com.yohwan.webtoon.dto.ViewHistoryResponse;
import com.yohwan.webtoon.repository.ContentsRepository;
import com.yohwan.webtoon.repository.UserRepository;
import com.yohwan.webtoon.repository.ViewHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ServiceTest
class ViewHistoryServiceTest {
    @Autowired
    private ContentsRepository contentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViewHistoryRepository viewHistoryRepository;

    @Autowired
    private ViewHistoryService viewHistoryService;

    private Long savedContentsId;

    private Long savedUserId;

    private static final int VIEW_HISTORY_COUNT = 10;

    @BeforeEach
    void setUp() {
        Contents savedContents = saveContents();
        User savedUser = saveUser();
        saveViewHistories(savedContents, savedUser);

    }

    private Contents saveContents() {
        Contents savedContents = contentsRepository.save(Contents.builder()
                .contentsName("새로운컨텐츠")
                .author("요환")
                .coin(0)
                .openDate(LocalDateTime.now())
                .chargeType(ChargeType.FREE)
                .build());
        this.savedContentsId = savedContents.getId();

        return savedContents;
    }

    private User saveUser() {
        User savedUser = userRepository.save(User.builder()
                .userName("임요환")
                .userEmail("dyghks7102@naver.com")
                .gender(GenderType.MALE)
                .ageType(AgeType.ADULT)
                .registerDate(LocalDateTime.now())
                .build());
        this.savedUserId = savedUser.getId();
        return savedUser;
    }

    private void saveViewHistories(Contents savedContents, User savedUser) {
        for (int i = 0; i < VIEW_HISTORY_COUNT; i++) {
            viewHistoryRepository.save(
                    ViewHistory.builder()
                            .contents(savedContents)
                            .user(savedUser)
                            .createdDate(LocalDateTime.now())
                            .build()
            );
        }
    }

    @AfterEach
    void tearDown() {
        viewHistoryRepository.deleteAll();
        contentsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("콘텐츠의 이력을 조회함")
    void findViewHistories() {
        List<ViewHistoryResponse> viewHistories = viewHistoryService.findViewHistories(savedContentsId);
        Assertions.assertThat(viewHistories.size()).isEqualTo(VIEW_HISTORY_COUNT);
    }

    @Test
    @DisplayName("존재하지 않는 콘텐츠의 이력을 조회함")
    void findViewHistoriesThrowsNoContents() {
        assertThrows(IllegalStateException.class, () -> {
            viewHistoryService.findViewHistories(99999999L);
        });
    }
}