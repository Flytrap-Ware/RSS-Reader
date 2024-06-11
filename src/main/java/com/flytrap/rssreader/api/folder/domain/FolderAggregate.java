package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.global.model.DefaultDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FolderAggregate implements DefaultDomain<Long> {

    private final FolderId id;
    private String name;
    private final AccountId ownerId;
    private SharedStatus sharedStatus;

    @Builder
    protected FolderAggregate(FolderId id, String name, AccountId ownerId,
        SharedStatus sharedStatus) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.sharedStatus = sharedStatus;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void toPrivate() {
        this.sharedStatus = SharedStatus.PRIVATE;
    }

    public void toShared() {
        this.sharedStatus = SharedStatus.SHARED;
    }

}
