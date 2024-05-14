package com.flytrap.rssreader.api.subscribe.infrastructure.entity;

import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.domain.Subscription;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
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
@Table(name = "subscription")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_id", nullable = false)
    private Long folderId;

    @Column(name = "rss_source_id", nullable = false)
    private Long rssSourceId;

    @Builder
    protected SubscriptionEntity(Long id, Long folderId, Long rssSourceId) {
        this.id = id;
        this.folderId = folderId;
        this.rssSourceId = rssSourceId;
    }

    public Subscription toReadOnly(RssSourceEntity rssSourceEntity) {
        if (!Objects.equals(rssSourceEntity.getId(), rssSourceId)) {
            throw new InconsistentDomainException(Subscription.class);
        }

        return Subscription.builder()
            .id(new SubscriptionId(id))
            .url(rssSourceEntity.getUrl())
            .title(rssSourceEntity.getTitle())
            .platform(rssSourceEntity.getPlatform())
            .rssSourceId(new RssSourceId(rssSourceEntity.getId()))
            .build();
    }

}

