package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.global.model.Domain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "myOwnFolder")
public class MyOwnFolder {

    private final FolderId id;
    private final String name;

    @Builder
    protected MyOwnFolder(FolderId id, String name) {
        this.id = id;
        this.name = name;
    }
}
