package com.flytrap.rssreader.api.folder.business.service;

import com.flytrap.rssreader.api.folder.infrastructure.entity.SharedFolderEntity;
import com.flytrap.rssreader.api.folder.infrastructure.repository.SharedFolderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedFolderReadService {

    private final SharedFolderJpaRepository sharedFolderJpaRepository; // Todo: folderMemberJpaRepository

    @Transactional(readOnly = true)
    public long countAllMembersByFolder(long folderId) {
        return sharedFolderJpaRepository.countAllByFolderId(folderId);
    }

    /**
     * 멤버Id로 초대된 폴더Id 목록을 반환합니다.
     * @param memberId
     * @return List<FolderId>
     */
    @Transactional(readOnly = true)
    public List<Long> findFoldersInvited(long memberId) {
        List<SharedFolderEntity> shared = sharedFolderJpaRepository.findAllByMemberId(
                memberId);

        return shared.stream()
                .map(SharedFolderEntity::getFolderId)
                .toList();
    }

    /**
     * 폴더Id와 멤버Id로 초대 여부를 반환합니다.
     * @param folderId
     * @param memberId
     */
    @Transactional(readOnly = true)
    public void verifyFolderInvited(long folderId, long memberId) {
        if (!sharedFolderJpaRepository.existsByFolderIdAndMemberId(folderId, memberId)) {
            throw new IllegalArgumentException("공유된 폴더가 아닙니다.");
        }
    }

    /**
     * 폴더Id마다 초대된 멤버리스트 Map을 반환합니다.
     * @param invitedFolderIds
     * @return Map<FolderId, List<MemberId>>
     */
    @Transactional(readOnly = true)
    public Map<Long, List<Long>> findMembersInFolders(List<Long> invitedFolderIds) {
        return sharedFolderJpaRepository.findAllByFolderIdIn(invitedFolderIds).stream()
                .collect(Collectors.groupingBy(SharedFolderEntity::getFolderId,
                        Collectors.mapping(SharedFolderEntity::getMemberId, Collectors.toList())));
    }
}
