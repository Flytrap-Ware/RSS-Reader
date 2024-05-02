package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.global.model.Domain;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "accessibleFolder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessibleFolder {

    private FolderId id;
    private String name;
    private FolderMemberId ownerId;
    private SharedStatus sharedStatus;

    @Builder
    protected AccessibleFolder(FolderId id, String name, FolderMemberId ownerId,
        SharedStatus sharedStatus) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.sharedStatus = sharedStatus;
    }

    public static AccessibleFolder from(Folder folder) {
        return AccessibleFolder.builder()
            .id(new FolderId(folder.getId()))
            .name(folder.getName())
            .ownerId(new FolderMemberId(folder.getMemberId()))
            .sharedStatus(SharedStatus.from(folder.isShared()))
            .build();
    }

    public boolean isPrivate() {
        return sharedStatus == SharedStatus.PRIVATE;
    }

    public boolean isShared() {
        return sharedStatus == SharedStatus.SHARED;
    }

    public PrivateFolder toPrivateFolder(List<FolderSubscription> folderSubscriptions) {
        return PrivateFolder.builder()
            .id(id)
            .name(name)
            .ownerId(ownerId)
            .subscriptions(folderSubscriptions)
            .build();
    }

    public SharedFolder toSharedFolder(List<FolderSubscription> folderSubscriptions,
        List<SharedMember> sharedMembers) {

        return SharedFolder.builder()
            .id(id)
            .name(name)
            .ownerId(ownerId)
            .subscriptions(folderSubscriptions)
            .sharedMembers(sharedMembers)
            .build();
    }
}
