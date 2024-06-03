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

    @Column(name = "post_collection_batch_size")
    private Integer postCollectionBatchSize;

    @Column(name = "core_thread_pool_size")
    private Integer coreThreadPoolSize;

    @Builder
    protected AdminSystemEntity(
        Long id, boolean postCollectionEnabled, Long postCollectionDelay,
        Integer postCollectionBatchSize, Integer coreThreadPoolSize
    ) {
        this.id = id;
        this.postCollectionEnabled = postCollectionEnabled;
        this.postCollectionDelay = postCollectionDelay;
        this.postCollectionBatchSize = postCollectionBatchSize;
        this.coreThreadPoolSize = coreThreadPoolSize;
    }

    public AdminSystemAggregate toAggregate() {
        return AdminSystemAggregate.builder()
            .id(new AdminSystemId(id))
            .postCollectionEnabled(postCollectionEnabled)
            .postCollectionDelay(postCollectionDelay)
            .coreThreadPoolSize(coreThreadPoolSize)
            .postCollectionBatchSize(postCollectionBatchSize)
            .build();
    }

    public static AdminSystemEntity from(AdminSystemAggregate adminSystemAggregate) {
        return AdminSystemEntity.builder()
            .id(adminSystemAggregate.getId().value())
            .postCollectionEnabled(adminSystemAggregate.isPostCollectionEnabled())
            .postCollectionDelay(adminSystemAggregate.getPostCollectionDelay())
            .postCollectionBatchSize(adminSystemAggregate.getPostCollectionBatchSize())
            .coreThreadPoolSize(adminSystemAggregate.getCoreThreadPoolSize())
            .build();
    }
}
