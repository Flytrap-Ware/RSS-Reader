package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.global.model.Domain;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "sharedFolder")
public class SharedFolder {

    private final FolderId id;
    private final String name;
    private final FolderMemberId ownerId;
    private final SharedStatus sharedStatus = SharedStatus.SHARED;
    private final List<FolderSubscription> subscriptions;
    private final List<SharedMember> sharedMembers;

    @Builder
    protected SharedFolder(FolderId id, String name, FolderMemberId ownerId,
        List<FolderSubscription> subscriptions, List<SharedMember> sharedMembers) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.subscriptions = subscriptions;
        this.sharedMembers = sharedMembers;
    }

}

