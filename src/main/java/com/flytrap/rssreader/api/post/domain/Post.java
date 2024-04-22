package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.NewDefaultDomain;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post implements NewDefaultDomain {

    private PostId id;
    private String guid;
    private String title;
    private String thumbnailUrl;
    private String description;
    private Instant pubDate;
    private String subscribeTitle;
    private Open open;
    private Bookmark bookmark;
    // TODO: react

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
