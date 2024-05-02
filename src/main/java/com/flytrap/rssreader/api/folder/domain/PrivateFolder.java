package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.global.model.Domain;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "privateFolder")
public class PrivateFolder {

    private final FolderId id;
    private final String name;
    private final FolderMemberId ownerId;
    private final SharedStatus sharedStatus = SharedStatus.SHARED;
    private final List<FolderSubscription> subscriptions;

    @Builder
    protected PrivateFolder(FolderId id, String name, FolderMemberId ownerId,
        List<FolderSubscription> subscriptions) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.subscriptions = subscriptions;
    }
}
