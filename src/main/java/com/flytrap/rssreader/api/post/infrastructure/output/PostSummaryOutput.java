package com.flytrap.rssreader.api.post.infrastructure.output;


import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Post;

import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.domain.PostRead;
import java.time.Instant;

public record PostSummaryOutput(
        Long id,
        Long subscribeId,
        String guid,
        String title,
        String thumbnailUrl,
        String description,
        Instant pubDate,
        String subscribeTitle,
        boolean read,
        boolean bookmark
        // TODO: react 추가하기
) {

    public Post toDomain() {
        return Post.builder()
                .id(new PostId(id))
                .guid(guid)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .description(description)
                .pubDate(pubDate)
                .subscribeTitle(subscribeTitle)
                .read(new PostRead(read))
                .bookmark(new Bookmark(bookmark))
                .build();
    }
}
