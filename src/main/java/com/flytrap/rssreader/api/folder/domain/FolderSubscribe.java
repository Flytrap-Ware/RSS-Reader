package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.Domain;
import lombok.Getter;

// TODO: 제거하기
@Getter
@Domain(name = "folderSubscribe")
public class FolderSubscribe {

    private Long id;
    private String title;
    private Integer unreadCount;
    private Integer postCount;
    private Integer openCount;

    public FolderSubscribe(Long id, String title, Integer unreadCount) {
        this.id = id;
        this.title = title;
        this.unreadCount = unreadCount;
    }

}
