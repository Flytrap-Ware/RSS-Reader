package com.flytrap.rssreader.api.post.domain;

public record PostRead(
    boolean isOpen
) {
    public PostRead read() {
        return new PostRead(true);
    }

    public PostRead unRead() {
        return new PostRead(false);
    }

}
