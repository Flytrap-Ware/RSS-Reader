package com.flytrap.rssreader.api.subscribe.infrastructure.entity;

import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
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
    private String description;

    @Builder
    public FolderSubscribeEntity(Long id, Long folderId, Long subscribeId, String description) {
        this.id = id;
        this.folderId = folderId;
        this.subscribeId = subscribeId;
        this.description = description;
    }

    public static FolderSubscribeEntity from(Subscribe subscribe, Long folderId) {
        return FolderSubscribeEntity.builder()
            .subscribeId(subscribe.getId())
            .folderId(folderId)
            .description("") // FolderSubscribe에서 description은 회원이 수정할 수 있는 설명값.
            // 아직 해당 기능이 구현되지 않았으므로 빈 문자열을 전달해 준다.
            .build();
    }
}

