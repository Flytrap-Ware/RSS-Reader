package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "folderCreate")
public class FolderCreate {

    private final String name;
    private final AccountId ownerId;
    private final SharedStatus sharedStatus = SharedStatus.PRIVATE;
    private final boolean isDeleted = false;

    @Builder
    protected FolderCreate(String name, AccountId ownerId) {
        this.name = name;
        this.ownerId = ownerId;
    }
}
