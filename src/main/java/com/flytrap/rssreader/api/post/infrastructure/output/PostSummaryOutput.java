package com.flytrap.rssreader.api.post.infrastructure.output;


import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.Post;

import com.flytrap.rssreader.api.post.domain.PostId;
import java.time.Instant;

public record PostSummaryOutput(
        String id,
        Long rssSourceId,
        String guid,
        String title,
        String thumbnailUrl,
        String description,
        Instant pubDate,
        String subscribeTitle,
        boolean open,
        boolean bookmark
) {

    public Post toReadOnly() {
        return Post.builder()
                .id(new PostId(id))
                .guid(guid)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .description(description)
                .pubDate(pubDate)
                .subscribeTitle(subscribeTitle)
                .open(Open.from(open))
                .bookmark(Bookmark.from(bookmark))
                .build();
    }
}
