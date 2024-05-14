package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.DefaultDomain;
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
public class Folder implements DefaultDomain {

    private Long id;
    private String name;
    private Long memberId;
    private SharedStatus sharedStatus;
    private final List<FolderSubscribe> subscribes = new ArrayList<>();

    @Builder
    protected Folder(Long id, String name, Long memberId, Boolean isShared) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.sharedStatus = SharedStatus.from(isShared);
    }

    public static Folder of(Long id, String name, Long memberId, Boolean isShared) {
        return Folder.builder()
                .id(id)
                .name(name)
                .memberId(memberId)
                .isShared(isShared)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Folder && ((Folder) obj).getId().equals(this.id);
    }

}
