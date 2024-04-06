package com.flytrap.rssreader.api.folder.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder_member")
public class SharedFolderEntity { // TODO: rename folder member entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private Long memberId;

    @Builder
    protected SharedFolderEntity(Long id, Long folderId, Long memberId) {
        this.id = id;
        this.folderId = folderId;
        this.memberId = memberId;
    }

    public static SharedFolderEntity of(Long id, long inviteeId) {
        return SharedFolderEntity.builder()
                .folderId(id)
                .memberId(inviteeId)
                .build();
    }
}
