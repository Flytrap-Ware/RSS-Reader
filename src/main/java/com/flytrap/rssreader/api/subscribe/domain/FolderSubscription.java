package com.flytrap.rssreader.api.subscribe.domain;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.NewDefaultDomain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Domain(name = "folderSubscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderSubscription implements NewDefaultDomain {

    private FolderSubscriptionId id;
    private String title;
    private String url;
    private BlogPlatform platform;

    @Builder
    protected FolderSubscription(FolderSubscriptionId id, String title, String url, BlogPlatform platform) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
    }
}
