package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.shared_member.infrastructure.repository.SharedMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedFolderReadService {

    private final SharedMemberJpaRepository sharedMemberJpaRepository; // Todo: folderMemberJpaRepository

    @Transactional(readOnly = true)
    public long countAllMembersByFolder(long folderId) {
        return sharedMemberJpaRepository.countAllByFolderId(folderId);
    }

}
