package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.DefaultDomain;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "post")
public class Post implements DefaultDomain<String> {

    private final PostId id;
    private final String guid;
    private final String title;
    private final String thumbnailUrl;
    private final String description;
    private final Instant pubDate;
    private final String subscribeTitle;
    private final Open open;
    private final Bookmark bookmark;

    @Builder
    protected Post(PostId id, String guid, String title, String thumbnailUrl,
        String description, Instant pubDate, String subscribeTitle, Open open,
        Bookmark bookmark) {
        this.id = id;
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
