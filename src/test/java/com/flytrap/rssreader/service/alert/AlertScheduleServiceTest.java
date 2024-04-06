package com.flytrap.rssreader.service.alert;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEvent;
import com.flytrap.rssreader.api.alert.business.service.AlertScheduleService;
import com.flytrap.rssreader.api.alert.business.service.AlertService;
import com.flytrap.rssreader.api.alert.domain.Alert;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.business.event.subscribe.SubscribeEventQueue;
import java.util.List;
import java.util.Map;

import com.flytrap.rssreader.api.folder.business.service.FolderReadService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("AlertScheduleService  로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AlertScheduleServiceTest {

    @Mock
    private SubscribeEventQueue queue;

    @Mock
    private AlertService alertService;

    @Mock
    private FolderReadService folderReadService;

    @InjectMocks
    private AlertScheduleService alertScheduleService;


    @Test
    @DisplayName("새로운 구독 이벤트가 발행되면 알람 이벤트를 발행할 수 있다.")
    void alert_success() {
        // Given
        List<Alert> alertList = List.of(
            Alert.builder()
                .id(1L)
                .memberId(1L)
                .folderId(1L)
                .alertPlatform(AlertPlatform.DISCORD)
                .webhookUrl("WEBHOOK_URL")
                .build()
        );

        SubscribeEvent subscribeEvent = new SubscribeEvent(
            1L,
            Map.of(
                "newPostUrl1", "newPostTitle1",
                "newPostUrl2", "newPostTitle2",
                "newPostUrl3", "newPostTitle3"
            )
        );

        Folder folder = Folder.create("folder1", 1L);

        when(queue.isRemaining()).thenReturn(true);
        when(queue.poll()).thenReturn(subscribeEvent);
        when(alertService.getAlertListBySubscribe(anyLong())).thenReturn(alertList);
        when(folderReadService.findById(anyLong())).thenReturn(folder);

        // When
        alertScheduleService.processAlertSchedule();

        // Then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertService, times(alertList.size()))
                .publishAlertEvent(anyString(), anyString(), anyMap());
        });
    }

    @Test
    @DisplayName("새로운 구독 이벤트가 없으면 알림 이벤트로 발행되지 않는다.")
    void noSubscribeEventInQueue() {
        // Given
        when(queue.isRemaining()).thenReturn(false);

        // When
        alertScheduleService.processAlertSchedule();

        // Then
        verify(alertService, never())
            .publishAlertEvent(anyString(), anyString(), anyMap());
    }
}
