package com.flytrap.rssreader.api.subscribe.infrastructure.entity;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.parser.dto.RssSourceData;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "rss_source")
public class RssSourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2500)
    private String title;

    @Column(length = 2500, nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private BlogPlatform platform;

    @Column
    private Instant lastCollectedAt;

    @Builder
    protected RssSourceEntity(Long id, String title, String url,
                              BlogPlatform platform) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
    }

    public static RssSourceEntity from(RssSourceData rssSourceData) {
        return RssSourceEntity.builder()
                .title(rssSourceData.title())
                .url(rssSourceData.url())
                .platform(rssSourceData.platform())
                .build();
    }

    public static RssSourceEntity from(RssSource rssSource) {
        return RssSourceEntity.builder()
                .id(rssSource.getId().value())
                .title(rssSource.getTitle())
                .url(rssSource.getUrl())
                .platform(rssSource.getPlatform())
                .build();
    }

    /**
     * 이 RssResourceEntity를 새로 추가된 구독을 나타내는 RssResource Domain 객체로 변환합니다.
     * 이 메서드는 구독이 새로 생성되었다는 것을 나타내는 플래그와 함께 RssResource 도메인 객체를 초기화합니다.
     * RssResourceEntity가 데이터베이스에 아직 존재하지 않는 새 구독을 나타낼 때 이 메서드를 사용하세요.
     * (기존에 존재하던 구독일 경우 toExistingRssResourceDomain()으로 변환하세요.)
     *
     * @return 새로 추가된 구독 RssResource Domain 객체
     */
    public RssSource toNewRssSource() {
        return RssSource.of(new RssSourceId(this.id), this.title, this.url, this.platform, true);
    }

    /**
     * 이 RssResourceEntity를 기존에 존재하던 구독을 나타내는 RssResource Domain 객체로 변환합니다.
     * 이 메서드는 구독이 기존에 존재한다는 것을 나타내는 플래그와 함께 RssResource 도메인 객체를 초기화합니다.
     * RssResourceEntity가 데이터베이스에 이미 존재하는 구독을 나타낼 때 이 메서드를 사용하세요.
     * (새로 추가된 구독일 경우 toNewRssResourceDomain()으로 변환하세요.)
     *
     * @return 기존에 존재하던 구독 RssResource Domain 객체
     */
    public RssSource toExistingRssSource() {
        return RssSource.of(new RssSourceId(this.id), this.title, this.url, this.platform, false);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateLastCollectedAt(Instant lastCollectedAt) {
        this.lastCollectedAt = lastCollectedAt;
    }
}
