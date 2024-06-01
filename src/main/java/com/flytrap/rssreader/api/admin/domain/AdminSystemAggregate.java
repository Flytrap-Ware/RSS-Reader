package com.flytrap.rssreader.api.admin.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "post")
public class AdminSystemAggregate implements DefaultDomain {

    private final AdminSystemId id;
    private boolean postCollectionEnabled;
    private long postCollectionDelay;
    private int coreThreadPoolSize;

    @Builder
    protected AdminSystemAggregate(
        AdminSystemId id, boolean postCollectionEnabled, long postCollectionDelay,
        int coreThreadPoolSize
    ) {
        this.id = id;
        this.postCollectionEnabled = postCollectionEnabled;
        this.postCollectionDelay = postCollectionDelay;
        this.coreThreadPoolSize = coreThreadPoolSize;
    }

    public void startPostCollection() {
        this.postCollectionEnabled = true;
    }

    public void stopPostCollection() {
        this.postCollectionEnabled = false;
    }

    public void changePostCollectionDelay(long delay) {
        this.postCollectionDelay = delay;
    }

    public void changeCoreThreadPoolSize(int coreThreadPoolSize) {
        this.coreThreadPoolSize = coreThreadPoolSize;
    }
}
