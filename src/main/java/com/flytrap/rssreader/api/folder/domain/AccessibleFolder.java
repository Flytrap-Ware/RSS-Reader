package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.Domain;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Domain(name = "folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessibleFolder {
    private Long id;
    private String name;
    private Long memberId;
    private SharedStatus sharedStatus;
    private Boolean isDeleted;
    private final List<FolderSubscribe> subscribes = new ArrayList<>();

    @Builder
    protected AccessibleFolder(Long id, String name, Long memberId, Boolean isShared, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.sharedStatus = SharedStatus.from(isShared);
        this.isDeleted = isDeleted;
    }

    public static AccessibleFolder from(Folder folder) {
        return AccessibleFolder.builder()
            .id(folder.getId())
            .name(folder.getName())
            .memberId(folder.getMemberId())
            .isShared(folder.isShared())
            .isDeleted(folder.isDeleted())
            .build();
    }
}
