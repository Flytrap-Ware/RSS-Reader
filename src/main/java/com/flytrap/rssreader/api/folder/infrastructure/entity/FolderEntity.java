package com.flytrap.rssreader.api.folder.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder")
public class FolderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(nullable = false)
    private Long accountId;
    @Column(nullable = false)
    private Boolean isShared;
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public FolderEntity(Long id, String name, Long accountId, Boolean isShared, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.accountId = accountId;
        this.isShared = isShared;
        this.isDeleted = isDeleted;
    }

    public static FolderEntity from(FolderCreate folderCreate) {
        return FolderEntity.builder()
            .name(folderCreate.getName())
            .accountId(folderCreate.getOwnerId().value())
            .isShared(folderCreate.getSharedStatus().isShared())
            .isDeleted(false)
            .build();
    }

    public static FolderEntity from(FolderAggregate folderAggregate) {
        return FolderEntity.builder()
            .id(folderAggregate.getId().value())
            .name(folderAggregate.getName())
            .accountId(folderAggregate.getOwnerId().value())
            .isShared(folderAggregate.getSharedStatus().isShared())
            .isDeleted(false)
            .build();
    }

    public static FolderEntity fromForDelete(FolderAggregate folderAggregate) {
        return FolderEntity.builder()
            .id(folderAggregate.getId().value())
            .name(folderAggregate.getName())
            .accountId(folderAggregate.getOwnerId().value())
            .isShared(folderAggregate.getSharedStatus().isShared())
            .isDeleted(true)
            .build();
    }

    public Folder toReadonly(List<Subscription> subscriptions, List<SharedMember> sharedMembers) {
        return Folder.builder()
            .id(new FolderId(id))
            .name(name)
            .ownerId(new AccountId(accountId))
            .sharedStatus(SharedStatus.from(isShared))
            .subscriptions(subscriptions)
            .sharedMembers(sharedMembers)
            .build();
    }

    public FolderAggregate toAggregate() {
        return FolderAggregate.builder()
            .id(new FolderId(id))
            .name(name)
            .ownerId(new AccountId(accountId))
            .sharedStatus(SharedStatus.from(isShared))
            .build();
    }

}
