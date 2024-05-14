package com.flytrap.rssreader.api.post.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Open{
    MARKED(true), UNMARKED(false);

    private final boolean flag;

    public static Open from(boolean flag) {
        return flag ? MARKED : UNMARKED;
    }

    public boolean flag() {
        return this.flag;
    }
}
