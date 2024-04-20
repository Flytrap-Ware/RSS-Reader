package com.flytrap.rssreader.api.folder.domain;

import com.flytrap.rssreader.api.post.infrastructure.output.OpenPostCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.global.model.DefaultDomain;
import com.flytrap.rssreader.global.model.Domain;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Domain(name = "folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder implements DefaultDomain {

    private Long id;
    private String name;
    private Long memberId;
    private SharedStatus sharedStatus;
    private Boolean isDeleted;
    private final List<FolderSubscribe> subscribes = new ArrayList<>();

    @Builder
    protected Folder(Long id, String name, Long memberId, Boolean isShared, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.memberId = memberId;
        this.sharedStatus = SharedStatus.from(isShared);
        this.isDeleted = isDeleted;
    }

    public static Folder of(Long id, String name, Long memberId, Boolean isShared,
            Boolean isDeleted) {
        return Folder.builder()
                .id(id)
                .name(name)
                .memberId(memberId)
                .isShared(isShared)
                .isDeleted(isDeleted)
                .build();
    }

    public static Folder create(String name, long member) {
        return Folder.builder()
                .name(name)
                .memberId(member)
                .isShared(false)
                .isDeleted(false)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isShared() {
        return sharedStatus == SharedStatus.SHARED;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void toShare() {
        this.sharedStatus = SharedStatus.SHARED;
    }

    public void toPrivate() {
        this.sharedStatus = SharedStatus.PRIVATE;
    }

    public boolean isOwner(long id) {
        return this.memberId == id;
    }

    public void addSubscribe(FolderSubscribe subscribe) {
        this.subscribes.add(subscribe);
    }

    public void addAllSubscribes(List<FolderSubscribe> subscribes) {
        this.subscribes.addAll(subscribes);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Folder && ((Folder) obj).getId().equals(this.id);
    }

    public List<Long> getSubscribeIds() {
        return subscribes.stream()
                .map(FolderSubscribe::getId)
                .toList();
    }

    public void addUnreadCountsBySubscribes(Map<SubscriptionId, PostSubscribeCountOutput> countsPost, Map<SubscriptionId, OpenPostCountOutput> countsOpen) {
        for (FolderSubscribe subscribe : subscribes) {

            PostSubscribeCountOutput subscribePostCount = countsPost.get(new SubscriptionId(subscribe.getId()));
            OpenPostCountOutput openPostCount = countsOpen.get(new SubscriptionId(subscribe.getId()));
            int totalCount = 0;
            int openCount = 0;

            if (openPostCount != null) openCount = openPostCount.getPostCount();
            if (subscribePostCount != null) totalCount = subscribePostCount.getPostCount();

            subscribe.addUnreadCount(totalCount, openCount);
        }
    }

    public int getUnreadCount() {
        return subscribes.stream()
                .mapToInt(FolderSubscribe::getUnreadCount)
                .sum();
    }
}
