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
    private int postCollectionBatchSize;
    private int coreThreadPoolSize;

    @Builder
    protected AdminSystemAggregate(
        AdminSystemId id, boolean postCollectionEnabled, long postCollectionDelay,
        int postCollectionBatchSize, int coreThreadPoolSize
    ) {
        this.id = id;
        this.postCollectionEnabled = postCollectionEnabled;
        this.postCollectionDelay = postCollectionDelay;
        this.postCollectionBatchSize = postCollectionBatchSize;
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

    public void changePostCollectionBatchSize(int postCollectionBatchSize) {
        this.postCollectionBatchSize = postCollectionBatchSize;
    }

    public void changeCoreThreadPoolSize(int coreThreadPoolSize) {
        this.coreThreadPoolSize = coreThreadPoolSize;
    }
}
