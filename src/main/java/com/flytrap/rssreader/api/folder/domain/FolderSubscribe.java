package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.global.model.Domain;
import lombok.Getter;

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

    public static FolderSubscribe from(Subscribe subscribes) {
        return new FolderSubscribe(subscribes.getId(), subscribes.getTitle(), 0);
    }

    public void addUnreadCount(Integer post, Integer open) {
        int postCount = post == null ? 0 : post;
        int openCount = open == null ? 0 : open;

        this.postCount = postCount;
        this.openCount = openCount;
        this.unreadCount = postCount - openCount;
    }
}
