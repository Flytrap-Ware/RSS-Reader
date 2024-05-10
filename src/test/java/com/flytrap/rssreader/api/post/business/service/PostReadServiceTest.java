package com.flytrap.rssreader.api.post.business.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionReader;
import com.flytrap.rssreader.global.event.GlobalEventPublisher;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostReadServiceTest {

    @InjectMocks
    PostReadService postReadService;

    @Mock
    PostCommand postCommand;

    @Mock
    SubscriptionReader subscriptionReader;

    @Mock
    GlobalEventPublisher globalEventPublisher;

    @Nested
    @DisplayName("Post 조회하기")
    class GetPost {

        @Test
        @DisplayName("Post를 조회할 수 있다")
        void test_read() {
            // Given
            AccountId accountId = new AccountId(1L);
            PostId postId = new PostId(1L);
            SubscriptionId subscriptionId = new SubscriptionId(1L);
            PostAggregate postAggregate = PostAggregate.builder()
                .id(postId).subscriptionId(subscriptionId).build();
            Subscribe subscription = Subscribe.builder()
                .id(subscriptionId.value()).build();

            // When
            when(postCommand.readAggregate(postId, accountId))
                .thenReturn(postAggregate);
            when(subscriptionReader.read(subscriptionId))
                .thenReturn(subscription);

            // Then
            Post result = postReadService.viewPost(accountId, postId);
            assertNotNull(result);
        }

        @Test
        @DisplayName("존재하지 않는 Post를 조회할 경우 NoSuchDomainException이 발생한다.")
        void test_non_existent_post() {
            // Given
            AccountId accountId = new AccountId(1L);
            PostId postId = new PostId(1L);

            // When
            when(postCommand.readAggregate(postId, accountId))
                .thenThrow(new NoSuchDomainException(PostAggregate.class));

            // Then
            assertThrows(NoSuchDomainException.class,
                () -> postReadService.viewPost(accountId, postId));
        }

    }

}