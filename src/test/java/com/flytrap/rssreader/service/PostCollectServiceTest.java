package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.generate50RssItemDataList;
import static com.flytrap.rssreader.fixture.FixtureFactory.generateSubscribeEntityList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.times;

import com.flytrap.rssreader.api.parser.RssPostParser;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.business.service.collect.PostCollectService;
import com.flytrap.rssreader.api.post.business.service.collect.SubscribeCollectionPriorityQueue;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@DisplayName("PostCollect 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PostCollectServiceTest {

    @Mock
    RssPostParser postParser;

    @Mock
    SubscribeEntityJpaRepository subscribeEntityJpaRepository;

    @Mock
    SubscribeCollectionPriorityQueue collectionQueue;

    @InjectMocks
    PostCollectService postCollectService;

    @DisplayName("한번에 n개의 Subscribe를 불러와 파싱할 수 있다.")
    @Test
    void collectPostsTest() {
        // given
        AtomicInteger callCount = new AtomicInteger(0);
        int expectedPollCount = 50;
        List<SubscribeEntity> subscribes = generateSubscribeEntityList(expectedPollCount);
        RssPostsData postData = new RssPostsData("title", generate50RssItemDataList());
        when(subscribeEntityJpaRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(subscribes));

        when(collectionQueue.isQueueEmpty()).thenAnswer(invocation -> callCount.getAndIncrement() >= expectedPollCount);
        when(collectionQueue.poll()).thenAnswer(invocation -> subscribes.get(0));
        when(postParser.parseRssDocuments(anyString())).thenReturn(Optional.of(postData));

        // when
        postCollectService.collectPosts(expectedPollCount);

        // then
        verify(collectionQueue, times(expectedPollCount)).poll();
    }
}
