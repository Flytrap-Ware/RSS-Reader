package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostStat {

    AlertPlatform platform;
    Long postCount;

    @Builder
    public PostStat(AlertPlatform platform, Long postCount) {
        this.platform = platform;
        this.postCount = postCount;
    }

}

