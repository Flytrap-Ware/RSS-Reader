package com.flytrap.rssreader.api.post.infrastructure.output;


import com.flytrap.rssreader.api.post.domain.Post;

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
        boolean open,
        boolean bookmark
        // TODO: react 추가하기
) {

    public Post toDomain() {
        return Post.builder()
                .id(id)
                .subscribeId(subscribeId)
                .guid(guid)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .description(description)
                .pubDate(pubDate)
                .subscribeTitle(subscribeTitle)
                .open(open)
                .bookmark(bookmark)
                .build();
    }
}
