package com.flytrap.rssreader.service.alert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.alert.business.service.AlertService;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.external.AlertSender;
import com.flytrap.rssreader.api.alert.infrastructure.external.DiscordAlertSender;
import com.flytrap.rssreader.api.alert.infrastructure.external.SlackAlertSender;
import com.flytrap.rssreader.api.alert.infrastructure.entity.AlertEntity;
import com.flytrap.rssreader.api.alert.infrastructure.repository.AlertEntityJpaRepository;
import com.flytrap.rssreader.api.alert.business.service.dto.AlertParam;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Alert 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    AlertEntityJpaRepository alertRepository;

    @Mock
    SlackAlertSender slackAlertSender;

    @Mock
    DiscordAlertSender discordAlertSender;

    AlertService alertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 어노테이션 초기화
        List<AlertSender> senders = Arrays.asList(slackAlertSender, discordAlertSender);

        /*
         * 생성자를 통해 Mock 객체를 주입
         * AlertService에서 AlertService List를 자동 주입 받고 있습니다.
         * 그러나 Mock으로는 자동 주입이 불가능 해서 AlertService를 직접 생성하고 있습니다.
         */
        alertService = new AlertService(alertRepository, senders);
    }

    @Test
    @DisplayName("폴더에 알림 웹 훅 URL 등록할 수 있다.")
    void registerAlert() {
        // given
        AlertEntity alertEntity = AlertEntity.create(
            1L, 1L, AlertPlatform.SLACK, AlertPlatform.SLACK.getSignatureUrl());

        // when
        when(alertRepository.save(any(AlertEntity.class)))
            .thenReturn(alertEntity);

        alertService.registerAlert(alertEntity.getFolderId(), alertEntity.getMemberId(),
            alertEntity.getWebhookUrl());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertRepository, times(1)).save(any(AlertEntity.class));
        });
    }

    @Test
    @DisplayName("폴더에 등록된 알림 웹 훅을 삭제할 수 있다.")
    void removeAlert() {
        AlertEntity alertEntity = AlertEntity.builder()
            .id(1L)
            .memberId(1L)
            .folderId(1L)
            .alertPlatform(AlertPlatform.SLACK)
            .webhookUrl(AlertPlatform.SLACK.getSignatureUrl())
            .build();

        when(alertRepository.findById(alertEntity.getId())
        ).thenReturn(Optional.of(alertEntity));

        // when
        alertService.removeAlert(alertEntity.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(alertRepository, times(1)).deleteById(alertEntity.getId());
        });
    }

    @Test
    @DisplayName("특정 플랫폼 알람 받기")
    void testNotifyPlatform() {

        // then
        AlertParam alertParam = new AlertParam(
            "folderName",
            AlertPlatform.SLACK.getSignatureUrl(),
            Map.of(
                "newPostUrl1", "newPostTitle1",
                "newPostUrl2", "newPostTitle2",
                "newPostUrl3", "newPostTitle3"
            )
        );

        // when
        when(slackAlertSender.isSupport(AlertPlatform.SLACK)).thenReturn(true);
        alertService.sendAlertToPlatform(alertParam);

        // then
        verify(slackAlertSender, times(1)).sendAlert(alertParam);
    }
}
