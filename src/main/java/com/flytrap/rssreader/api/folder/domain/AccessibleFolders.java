package com.flytrap.rssreader.api.folder.domain;

import java.util.List;

public record AccessibleFolders(
    List<PrivateFolder> privateFolders,
    List<SharedFolder> sharedFolder
) { }
