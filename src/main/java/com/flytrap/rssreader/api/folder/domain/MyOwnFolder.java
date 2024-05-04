package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "myOwnFolder")
public class MyOwnFolder {

    private final FolderId id;
    private String name;
    private final AccountId ownerId;
    private final SharedStatus sharedStatus;
    private final boolean isDeleted;

    @Builder
    protected MyOwnFolder(FolderId id, String name, AccountId ownerId, SharedStatus sharedStatus,
        boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.sharedStatus = sharedStatus;
        this.isDeleted = isDeleted;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
