package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderDomain;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderReadService {

    private final FolderEntityJpaRepository repository; // TODO: 제거하기
    private final FolderQuery folderQuery;

    @Transactional(readOnly = true)
    public AccessibleFolders getMyFolders(AccountId accountId) {
        List<FolderDomain> folders = folderQuery.readAllByAccount(accountId);

        return new AccessibleFolders(
            folders.stream().filter(FolderDomain::isPrivate).toList(),
            folders.stream().filter(FolderDomain::isShared).toList()
        );
    }

    // TODO: 제거하기
    @Transactional(readOnly = true)
    public Folder findById(long id) {
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow().toDomain();
    }
}
