package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderMemberEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderMemberJpaRepository;
import com.flytrap.rssreader.api.account.domain.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedFolderReadService {

    private final FolderMemberJpaRepository folderMemberJpaRepository; // Todo: folderMemberJpaRepository

    @Transactional(readOnly = true)
    public long countAllMembersByFolder(long folderId) {
        return folderMemberJpaRepository.countAllByFolderId(folderId);
    }

}
