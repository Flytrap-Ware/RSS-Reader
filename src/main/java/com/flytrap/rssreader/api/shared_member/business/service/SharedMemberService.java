package com.flytrap.rssreader.api.shared_member.business.service;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderAggregate;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderValidator;
import com.flytrap.rssreader.api.shared_member.domain.SharedMember;
import com.flytrap.rssreader.api.shared_member.domain.SharedMemberCreate;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberCommand;
import com.flytrap.rssreader.api.shared_member.infrastructure.implementation.SharedMemberValidator;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedMemberService {

    private final FolderValidator folderValidator;
    private final FolderCommand folderCommand;
    private final SharedMemberValidator sharedMemberValidator;
    private final SharedMemberCommand sharedMemberCommand;

    @Transactional
    public SharedMember inviteMemberToFolder(
        FolderId folderId, AccountId accountId, AccountId inviteeId
    ) {
        if (!folderValidator.isMyOwnFolder(folderId, accountId))
            throw new IllegalArgumentException("폴더의 주인이 아닙니다."); // TODO: 예외 만들기

        if (accountId.value() == inviteeId.value())
            throw new IllegalArgumentException("폴더의 주인은 추가할 수 없습니다."); // TODO: 예외 만들기

        if (sharedMemberValidator.existsBy(folderId, inviteeId))
            throw new DuplicateDomainException(SharedMember.class);

        SharedMember sharedMember = sharedMemberCommand
            .create(new SharedMemberCreate(folderId, inviteeId));

        FolderAggregate folderAggregate = folderCommand.readAggregate(folderId);
        folderAggregate.toShared();
        folderCommand.update(folderAggregate);

        return sharedMember;
    }

    @Transactional
    public void leaveFolder(FolderId folderId, AccountId accountId) {
        if (folderValidator.isMyOwnFolder(folderId, accountId))
            throw new IllegalArgumentException("폴더 주인은 떠날 수 없습니다."); // TODO: 예외 추가하기

        if (!folderValidator.isAccessibleFolder(folderId, accountId))
            throw new ForbiddenAccessFolderException(Folder.class);

        sharedMemberCommand.deleteBy(folderId, accountId);

        if (sharedMemberValidator.hasNoSharedMembersByFolder(folderId)) {
            FolderAggregate folderAggregate = folderCommand.readAggregate(folderId);
            folderAggregate.toPrivate();
            folderCommand.update(folderAggregate);
        }
    }

    public void removeMemberFromFolder(
        FolderId folderId, AccountId accountId, AccountId inviteeId
    ) {
        if (!folderValidator.isMyOwnFolder(folderId, accountId))
            throw new IllegalArgumentException("폴더의 주인이 아닙니다."); // TODO: 예외 만들기

        if (folderValidator.isMyOwnFolder(folderId, inviteeId))
            throw new IllegalArgumentException("자신을 추방할 수 없습니다."); // TODO: 예외 추가하기

        sharedMemberCommand.deleteBy(folderId, inviteeId);

        if (sharedMemberValidator.hasNoSharedMembersByFolder(folderId)) {
            FolderAggregate folderAggregate = folderCommand.readAggregate(folderId);
            folderAggregate.toPrivate();
            folderCommand.update(folderAggregate);
        }
    }
}
