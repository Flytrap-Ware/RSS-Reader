package com.flytrap.rssreader.domain;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEvent;
import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEventListener;
import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEventQueue;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("이벤트 큐 단위 테스트")
@ExtendWith(MockitoExtension.class)
class SubscribeEventListenerTest {

    @Spy
    ApplicationEventPublisher publisher;

    @Mock
    SubscribeEventQueue eventQueue;

    @InjectMocks
    SubscribeEventListener subscribeEventListener;

    @Test
    @DisplayName("이벤트 리스너 on evnet실행")
    void test() {
        SubscribeEvent event = new SubscribeEvent(1L, Map.of());

        subscribeEventListener.onEvent(event);

        SoftAssertions.assertSoftly(softAssertions -> {
            verify(eventQueue, times(1)).isFull(); // Verify that isFull method is called
            verify(eventQueue, times(1)).offer(event);
        });
    }
}
