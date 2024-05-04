package com.flytrap.rssreader.api.post.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Bookmark{
    MARKED(true), UNMARKED(false);

    private final boolean flag;

    public static Bookmark from(boolean flag) {
        return flag ? MARKED : UNMARKED;
    }

    public boolean flag() {
        return this.flag;
    }
}
