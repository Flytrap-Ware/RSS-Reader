package com.flytrap.rssreader.api.admin.infrastructure.entity;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.domain.AdminSystemId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "admin_system")
public class AdminSystemEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_collection_enabled", nullable = false)
    private boolean postCollectionEnabled;

    @Column(name = "post_collection_delay")
    private Long postCollectionDelay;

    @Builder
    protected AdminSystemEntity(
        Long id, boolean postCollectionEnabled, Long postCollectionDelay
    ) {
        this.id = id;
        this.postCollectionEnabled = postCollectionEnabled;
        this.postCollectionDelay = postCollectionDelay;
    }

    public AdminSystemAggregate toAggregate() {
        return AdminSystemAggregate.builder()
            .id(new AdminSystemId(id))
            .postCollectionEnabled(postCollectionEnabled)
            .postCollectionDelay(postCollectionDelay)
            .build();
    }

    public static AdminSystemEntity from(AdminSystemAggregate adminSystemAggregate) {
        return new AdminSystemEntity(
            adminSystemAggregate.getId().value(),
            adminSystemAggregate.isPostCollectionEnabled(),
            adminSystemAggregate.getPostCollectionDelay()
        );
    }
}
