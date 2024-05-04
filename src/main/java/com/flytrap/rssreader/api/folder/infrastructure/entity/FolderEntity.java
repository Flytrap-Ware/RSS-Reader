package com.flytrap.rssreader.api.folder.infrastructure.entity;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    public static FolderEntity from(MyOwnFolder myOwnFolder) {
        return FolderEntity.builder()
            .id(myOwnFolder.getId().value())
            .name(myOwnFolder.getName())
            .memberId(myOwnFolder.getOwnerId().value())
            .isShared(myOwnFolder.getSharedStatus().isShared())
            .isDeleted(false)
            .build();
    }

    public static FolderEntity fromForDelete(MyOwnFolder myOwnFolder) {
        return FolderEntity.builder()
            .id(myOwnFolder.getId().value())
            .name(myOwnFolder.getName())
            .memberId(myOwnFolder.getOwnerId().value())
            .isShared(myOwnFolder.getSharedStatus().isShared())
            .isDeleted(true)
            .build();
    }

    public Folder toDomain() {
        return Folder.of(id, name, memberId, isShared);
    }

    public MyOwnFolder toMyOwnFolder() {
        return MyOwnFolder.builder()
            .id(new FolderId(id))
            .name(name)
            .ownerId(new AccountId(memberId))
            .sharedStatus(SharedStatus.from(isShared))
            .build();
    }

}
