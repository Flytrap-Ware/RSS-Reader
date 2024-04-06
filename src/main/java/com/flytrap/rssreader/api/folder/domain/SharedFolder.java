package com.flytrap.rssreader.api.folder.domain;


import com.flytrap.rssreader.api.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

public class SharedFolder extends Folder { // TODO: shared folder 따로 구분하는 것이 좋은가?

    private List<Member> invitedMembers = new ArrayList<>();

    protected SharedFolder(Long id, String name, Long memberId, Boolean isDeleted, List<Member> invitedMembers, List<FolderSubscribe> subscribes) {
        super(id, name, memberId, true, isDeleted);
        super.addAllSubscribes(subscribes);
        this.invitedMembers = invitedMembers;
    }

    public void invite(Member member) {
        invitedMembers.add(member);
    }

    public List<Member> getInvitedMembers() {
        return List.of(invitedMembers.toArray(new Member[0]));
    }

    public static SharedFolder of (Folder folder, List<Member> invitedMembers) {
        return new SharedFolder(folder.getId(), folder.getName(), folder.getMemberId(), folder.getIsDeleted(), invitedMembers, folder.getSubscribes());
    }

}
