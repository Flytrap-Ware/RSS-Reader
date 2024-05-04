package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.AccessibleFolders;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.PrivateFolder;
import com.flytrap.rssreader.api.folder.domain.SharedFolder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
        List<PrivateFolder> privateFolders = folderQuery.readAllPrivateFolders(accountId);
        List<SharedFolder> sharedFolders = folderQuery.readAllSharedFolders(accountId);

        return new AccessibleFolders(privateFolders, sharedFolders);
    }

    // TODO: 제거하기
    @Transactional(readOnly = true)
    public Folder findById(long id) {
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow().toDomain();
    }

    // TODO: 제거하기
    @Transactional(readOnly = true)
    public List<Folder> findAllByMemberId(long memberId) {
        return repository.findAllByMemberIdAndIsDeletedFalse(memberId).stream()
                .map(FolderEntity::toDomain)
                .collect(Collectors.toList());
    }

    // TODO: 제거하기
    @Transactional(readOnly = true)
    public List<Folder> findAllByIds(Collection<Long> ids) {
        return repository.findAllById(ids).stream()
                .map(FolderEntity::toDomain)
                .toList();
    }
}
