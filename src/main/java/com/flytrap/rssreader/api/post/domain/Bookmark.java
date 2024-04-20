package com.flytrap.rssreader.api.post.domain;

public record Bookmark(
    boolean isBookmark
) {
    public Bookmark bookmark() {
        return new Bookmark(true);
    }

    public Bookmark unBookmark() {
        return new Bookmark(false);
    }
}
