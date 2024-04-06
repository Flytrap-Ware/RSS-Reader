package com.flytrap.rssreader.api.folder.business.facade;

import com.flytrap.rssreader.api.folder.business.service.SharedFolderReadService;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.SharedFolder;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.api.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvitedToFolderFacade {

    private final SharedFolderReadService sharedFolderReadService;
    private final MemberService memberService;

    public List<? extends Folder> addInvitedMembersInFolder(List<? extends Folder> folders) {
        List<Long> folderIds = folders.stream().map(Folder::getId).toList();
        Map<Long, List<Long>> membersInFolders =    // 폴더마다 초대된 멤버 목록
                sharedFolderReadService.findMembersInFolders(folderIds);

        Map<Long, Member> memberInfo = getMemberInformation(membersInFolders);

        return folders.stream()
                .map(folder -> injectMemberInfo(membersInFolders, memberInfo, folder))
                .toList();
    }

    private Folder injectMemberInfo(Map<Long, List<Long>> membersInFolders,
            Map<Long, Member> memberInfo,
            Folder folder) {
        if (folder.getSharedStatus() == SharedStatus.PRIVATE) {
            return folder;
        }
        List<Member> members = membersInFolders.getOrDefault(folder.getId(), List.of()).stream()
                .map(memberInfo::get)
                .toList();
        return SharedFolder.of(folder, members);
    }

    private Map<Long, Member> getMemberInformation(Map<Long, List<Long>> membersInFolders) {
        Set<Long> memberSet = membersInFolders.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        return memberService.findAllByIds(memberSet).stream()
                .collect(Collectors.toMap(Member::getId, member -> member));
    }

}
