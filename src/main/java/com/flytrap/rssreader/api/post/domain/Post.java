package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Domain(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post implements DefaultDomain {

    private Long id;
    private Long subscribeId;
    private String guid;
    private String title;
    private String thumbnailUrl;
    private String description;
    private Instant pubDate;
    private String subscribeTitle;
    private boolean open;
    private boolean bookmark;
    // TODO: react

    @Builder
    protected Post(Long id, Long subscribeId, String guid, String title, String thumbnailUrl,
                   String description, Instant pubDate, String subscribeTitle, boolean open,
                   boolean bookmark) {
        this.id = id;
        this.subscribeId = subscribeId;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.subscribeTitle = subscribeTitle;
        this.open = open;
        this.bookmark = bookmark;
    }
}
