package com.flytrap.rssreader.api.folder.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderDomain;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
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
    private Long memberId;
    @Column(nullable = false)
    private Boolean isShared;
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Builder
    public FolderEntity(Long id, String name, Long memberId, Boolean isShared, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.isShared = isShared;
        this.isDeleted = isDeleted;
    }

    public static FolderEntity from(Folder folder) {
        return FolderEntity.builder()
                .id(folder.getId())
                .name(folder.getName())
                .memberId(folder.getMemberId())
                .isShared(folder.isShared())
                .isDeleted(false)
                .build();
    }

    public static FolderEntity from(FolderCreate folderCreate) {
        return FolderEntity.builder()
            .name(folderCreate.getName())
            .memberId(folderCreate.getOwnerId().value())
            .isShared(folderCreate.getSharedStatus().isShared())
            .isDeleted(false)
            .build();
    }

    public static FolderEntity from(FolderAggregate folderAggregate) {
        return FolderEntity.builder()
            .id(folderAggregate.getId().value())
            .name(folderAggregate.getName())
            .memberId(folderAggregate.getOwnerId().value())
            .isShared(folderAggregate.getSharedStatus().isShared())
            .isDeleted(false)
            .build();
    }

    public static FolderEntity fromForDelete(FolderAggregate folderAggregate) {
        return FolderEntity.builder()
            .id(folderAggregate.getId().value())
            .name(folderAggregate.getName())
            .memberId(folderAggregate.getOwnerId().value())
            .isShared(folderAggregate.getSharedStatus().isShared())
            .isDeleted(true)
            .build();
    }

    public Folder toDomain() {
        return Folder.of(id, name, memberId, isShared);
    }

    public FolderDomain toReadonly(List<FolderSubscription> folderSubscriptions, List<SharedMember> sharedMembers) {
        return FolderDomain.builder()
            .id(new FolderId(id))
            .name(name)
            .ownerId(new AccountId(memberId))
            .sharedStatus(SharedStatus.from(isShared))
            .subscriptions(folderSubscriptions)
            .sharedMembers(sharedMembers)
            .build();
    }

    public FolderAggregate toAggregate() {
        return FolderAggregate.builder()
            .id(new FolderId(id))
            .name(name)
            .ownerId(new AccountId(memberId))
            .sharedStatus(SharedStatus.from(isShared))
            .build();
    }

}
