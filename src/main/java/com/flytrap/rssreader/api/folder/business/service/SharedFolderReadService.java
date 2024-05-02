package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedFolderReadService {

    private final FolderMemberJpaRepository folderMemberJpaRepository; // Todo: folderMemberJpaRepository

    @Transactional(readOnly = true)
    public long countAllMembersByFolder(long folderId) {
        return folderMemberJpaRepository.countAllByFolderId(folderId);
    }

}
