package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.infrastructure.entity.SharedFolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.SharedFolderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;

@Service
@RequiredArgsConstructor
public class SharedFolderUpdateService { // todo : rename to folderMemberService

    private final SharedFolderJpaRepository sharedFolderJpaRepository;

    @Transactional
    public void invite(Folder folder, long inviteeId) throws AuthenticationException {

        if (folder.isOwner(inviteeId)) {
            throw new AuthenticationException("폴더의 소유자에게는 폴더를 공유할 수 없습니다.");
        }

        if (isExists(folder, inviteeId)) {
            throw new DuplicateKeyException("이미 초대된 폴더입니다.");
        }

        sharedFolderJpaRepository.save(
                SharedFolderEntity.of(folder.getId(), inviteeId));
    }

    @Transactional(readOnly = true)
    public boolean isExists(Folder folder, long inviteeId) {
        return sharedFolderJpaRepository.existsByFolderIdAndMemberId(folder.getId(),
                inviteeId);
    }

    @Transactional
    public void leave(Folder folder, long id) {

        if (!isExists(folder, id)) {
            throw new IllegalArgumentException("공유된 폴더가 아닙니다.");
        }
        if (folder.isOwner(id)) {
            throw new IllegalArgumentException("폴더의 소유자는 폴더를 나갈 수 없습니다.");
        }

        sharedFolderJpaRepository.delete(
                SharedFolderEntity.of(folder.getId(), id));
    }

    @Transactional
    public void removeFolderMember(Folder folder, Long folderMemberId, long ownerId)
            throws AuthenticationException {

        if (!folder.isOwner(ownerId)) {
            throw new AuthenticationException("폴더의 소유자가 아닙니다.");
        }

        SharedFolderEntity sharedFolderEntity = sharedFolderJpaRepository.findByFolderIdAndMemberId(
                        folder.getId(), folderMemberId)
                .orElseThrow(() -> new IllegalArgumentException("초대되지 않은 멤버를 삭제할 수 없습니다."));

        sharedFolderJpaRepository.delete(sharedFolderEntity);
    }
}
