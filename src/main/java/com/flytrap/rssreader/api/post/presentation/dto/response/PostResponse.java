package com.flytrap.rssreader.api.post.presentation.dto.response;

import com.flytrap.rssreader.api.post.domain.Post;
import java.time.Instant;
import java.util.List;

public record PostResponse(
        long id,
        String guid,
        String title,
        String thumbnailUrl,
        String description,
        Instant pubDate,
        String subscribeTitle,
        boolean open,
        boolean bookmark
) {

    public record PostListResponse(
            List<PostResponse> posts
            // TODO: react
    ) { }

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId().value(),
                post.getGuid(),
                post.getTitle(),
                post.getThumbnailUrl(),
                post.getDescription(),
                post.getPubDate(),
                post.getSubscribeTitle(),
                post.getOpen().flag(),
                post.getBookmark().flag()
        );
    }
}
