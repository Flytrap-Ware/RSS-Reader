package com.flytrap.rssreader.api.folder.infrastructure.entity;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import jakarta.persistence.*;
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
                .isDeleted(folder.getIsDeleted())
                .build();
    }

    public static FolderEntity from(FolderCreate folderCreate) {
        return FolderEntity.builder()
            .name(folderCreate.getName())
            .memberId(folderCreate.getOwnerId().value())
            .isShared(folderCreate.getSharedStatus().isShared())
            .isDeleted(folderCreate.isDeleted())
            .build();
    }

    public Folder toDomain() {
        return Folder.of(id, name, memberId, isShared, isDeleted);
    }

    public MyOwnFolder toMyOwnFolder() {
        return MyOwnFolder.builder()
            .id(new FolderId(id))
            .name(name)
            .build();
    }

}
