package com.flytrap.rssreader.api.alert.infrastructure.external;

import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.List;

public interface AlertSender {

    boolean isSupport(AlertPlatform alertPlatform);
    void sendAlert(String folderName, String webhookUrl, List<PostEntity> posts);
}
