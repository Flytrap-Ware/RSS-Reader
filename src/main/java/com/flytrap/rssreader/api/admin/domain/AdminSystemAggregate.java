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

    @Builder
    protected AdminSystemAggregate(AdminSystemId id, boolean postCollectionEnabled) {
        this.id = id;
        this.postCollectionEnabled = postCollectionEnabled;
    }

    public void startPostCollection() {
        this.postCollectionEnabled = true;
    }

    public void stopPostCollection() {
        this.postCollectionEnabled = false;
    }
}
