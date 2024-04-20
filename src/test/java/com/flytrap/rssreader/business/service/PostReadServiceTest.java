package com.flytrap.rssreader.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostRead 단위 테스트")
public class PostReadServiceTest {

    @Test
    @DisplayName("Post 도메인을 조회할 수 있다")
    void test_read() {

    }

    @Test
    @DisplayName("존재하지 않는 Post를 조회할 경우 NoSuchDomainException이 발생한다.")
    void test_non_existent_post() {

    }

}
