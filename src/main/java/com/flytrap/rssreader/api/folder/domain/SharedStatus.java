package com.flytrap.rssreader.api.folder.domain;

public enum SharedStatus {
    SHARED, PRIVATE;

    public static SharedStatus from(boolean isShared) {
        return isShared ? SHARED : PRIVATE;
    }
}
