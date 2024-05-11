package com.flytrap.rssreader.api.subscribe.infrastructure.entity;

import com.flytrap.rssreader.api.subscribe.domain.FolderSubscription;
import com.flytrap.rssreader.api.subscribe.domain.FolderSubscriptionId;
import com.flytrap.rssreader.global.exception.domain.InconsistentDomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "folder_subscribe")
public class FolderSubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "subscribe_id", nullable = false)
    private Long subscribeId;

    @Column(length = 2500, nullable = false)
    private String description; // TODO: RssResource로 옮기기, null 허용하기

    @Builder
    protected FolderSubscribeEntity(Long id, Long folderId, Long subscribeId, String description) {
        this.id = id;
        this.folderId = folderId;
        this.subscribeId = subscribeId;
        this.description = description;
    }

    public FolderSubscription toReadOnly(SubscribeEntity rssSourceEntity) {
        if (!Objects.equals(rssSourceEntity.getId(), subscribeId)) {
            throw new InconsistentDomainException(this.getClass());
        }

        return FolderSubscription.builder()
            .id(new FolderSubscriptionId(id))
            .url(rssSourceEntity.getUrl())
            .title(rssSourceEntity.getTitle())
            .platform(rssSourceEntity.getPlatform())
            .build();
    }

}

