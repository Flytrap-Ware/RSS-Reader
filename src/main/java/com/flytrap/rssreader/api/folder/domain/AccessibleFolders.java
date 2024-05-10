package com.flytrap.rssreader.api.folder.domain;

import java.util.List;

public record AccessibleFolders(
    List<FolderDomain> privateFolders,
    List<FolderDomain> sharedFolder
) { }
