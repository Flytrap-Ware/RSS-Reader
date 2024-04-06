package com.flytrap.rssreader.api.alert.infrastructure.external;

import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.business.service.dto.AlertParam;

public interface AlertSender {

    boolean isSupport(AlertPlatform alertPlatform);
    void sendAlert(AlertParam value);
}
