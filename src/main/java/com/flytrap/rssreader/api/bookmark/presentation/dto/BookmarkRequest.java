package com.flytrap.rssreader.api.bookmark.presentation.dto;

import com.flytrap.rssreader.api.bookmark.domain.Bookmark;

public record BookmarkRequest() {

    public record Response(Long id, Long memberId, Long postId) {
        public static Response from(Bookmark bookmark) {
            return new Response(bookmark.getId(), bookmark.getMemberId(), bookmark.getPostId());
        }
    }

}
