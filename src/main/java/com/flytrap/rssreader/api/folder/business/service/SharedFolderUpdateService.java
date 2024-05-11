package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.shared_member.infrastructure.entity.FolderMemberEntity;
import com.flytrap.rssreader.api.shared_member.infrastructure.repository.SharedMemberJpaRepository;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharedFolderUpdateService { // todo : rename to folderMemberService

    private final SharedMemberJpaRepository sharedMemberJpaRepository;

    @Transactional(readOnly = true)
    public boolean isExists(Folder folder, long inviteeId) {
        return sharedMemberJpaRepository.existsByFolderIdAndMemberId(folder.getId(),
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

        sharedMemberJpaRepository.delete(
                FolderMemberEntity.of(folder.getId(), id));
    }

    @Transactional
    public void removeFolderMember(Folder folder, Long folderMemberId, long ownerId)
            throws AuthenticationException {

        if (!folder.isOwner(ownerId)) {
            throw new AuthenticationException("폴더의 소유자가 아닙니다.");
        }

        FolderMemberEntity folderMemberEntity = sharedMemberJpaRepository.findByFolderIdAndMemberId(
                        folder.getId(), folderMemberId)
                .orElseThrow(() -> new IllegalArgumentException("초대되지 않은 멤버를 삭제할 수 없습니다."));

        sharedMemberJpaRepository.delete(folderMemberEntity);
    }
}
