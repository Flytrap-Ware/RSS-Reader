package com.flytrap.rssreader.api.folder.infrastructure.entity;

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
@Table(name = "folder_member")
public class FolderMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private Long memberId;

    @Builder
    protected FolderMemberEntity(Long id, Long folderId, Long memberId) {
        this.id = id;
        this.folderId = folderId;
        this.memberId = memberId;
    }

    public static FolderMemberEntity of(Long id, long inviteeId) {
        return FolderMemberEntity.builder()
                .folderId(id)
                .memberId(inviteeId)
                .build();
    }
}
