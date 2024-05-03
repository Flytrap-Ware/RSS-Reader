package com.flytrap.rssreader.api.folder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SharedStatus {
    SHARED(true), PRIVATE(false);

    private final boolean shared;

    public static SharedStatus from(boolean isShared) {
        return isShared ? SHARED : PRIVATE;
    }
}
