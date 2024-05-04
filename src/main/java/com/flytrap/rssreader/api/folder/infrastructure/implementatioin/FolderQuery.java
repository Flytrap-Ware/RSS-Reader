package com.flytrap.rssreader.api.folder.infrastructure.implementatioin;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.AccessibleFolder;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.MyOwnFolder;
import com.flytrap.rssreader.api.folder.domain.PrivateFolder;
import com.flytrap.rssreader.api.folder.domain.SharedFolder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.FolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityDslRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderEntityJpaRepository;
import com.flytrap.rssreader.api.folder.infrastructure.repository.FolderMemberJpaRepository;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.FolderSubscriptionQuery;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FolderQuery {

    private final FolderEntityJpaRepository folderEntityJpaRepository;
    private final FolderEntityDslRepository folderEntityDslRepository;
    private final FolderMemberJpaRepository folderMemberJpaRepository;
    private final FolderSubscriptionQuery folderSubscriptionQuery;
    private final FolderMemberQuery folderMemberQuery;

    @Transactional(readOnly = true)
    public Folder read(FolderId folderId) {
        return folderEntityJpaRepository.findByIdAndIsDeletedFalse(folderId.value())
            .orElseThrow(() -> new NoSuchDomainException(Folder.class))
            .toDomain();
    }

    // TODO: Transaction이 적용되지 않는 메서드라서 수정 필요함
    @Transactional(readOnly = true)
    public AccessibleFolder readAccessible(FolderId folderId, AccountId accountId) {
        Folder folder = read(folderId);

        if (!isAccessible(folder, accountId)) {
            throw new ForbiddenAccessFolderException(folder);
        }

        return AccessibleFolder.from(folder);
    }

    @Transactional(readOnly = true)
    public MyOwnFolder readMyOwn(FolderId folderId, AccountId accountId) {
        return folderEntityJpaRepository
            .findByIdAndMemberIdAndIsDeletedFalse(folderId.value(), accountId.value())
            .orElseThrow(() -> new NoSuchDomainException(Folder.class))
            .toMyOwnFolder();
    }

    @Transactional(readOnly = true)
    public List<PrivateFolder> readAllPrivateFolders(AccountId accountId) {
        return folderEntityDslRepository
            .findAllAccessibleFolder(accountId.value())
            .stream()
            .map(FolderEntity::toDomain)
            .map(AccessibleFolder::from)
            .filter(AccessibleFolder::isPrivate)
            .map(accessibleFolder -> accessibleFolder.toPrivateFolder(
                folderSubscriptionQuery.readAllByFolder(accessibleFolder.getId())))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<SharedFolder> readAllSharedFolders(AccountId accountId) {
        return folderEntityDslRepository
            .findAllAccessibleFolder(accountId.value())
            .stream()
            .map(FolderEntity::toDomain)
            .map(AccessibleFolder::from)
            .filter(AccessibleFolder::isShared)
            .map(accessibleFolder -> accessibleFolder.toSharedFolder(
                folderSubscriptionQuery.readAllByFolder(accessibleFolder.getId()),
                folderMemberQuery.readAllByFolder(accessibleFolder.getId())))
            .toList();
    }

    private boolean isAccessible(Folder folder, AccountId accountId) {
        return folder.isOwner(accountId.value()) || isSharedFolder(new FolderId(folder.getId()),
            accountId);
    }

    private boolean isSharedFolder(FolderId folderId, AccountId accountId) {
        return folderMemberJpaRepository.existsByFolderIdAndMemberId(folderId.value(),
            accountId.value());
    }

}
