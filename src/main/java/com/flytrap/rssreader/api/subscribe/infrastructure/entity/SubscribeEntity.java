package com.flytrap.rssreader.api.subscribe.infrastructure.entity;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.parser.dto.RssSubscribeData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "rss_subscribe")
public class SubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2500, nullable = false)
    private String title;

    @Column(length = 2500, nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private BlogPlatform platform;

    @Builder
    protected SubscribeEntity(Long id, String title, String url,
            BlogPlatform platform) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.platform = platform;
    }

    public static SubscribeEntity from(RssSubscribeData rssSubscribeData) {
        return SubscribeEntity.builder()
                .title(rssSubscribeData.title())
                .url(rssSubscribeData.url())
                .platform(rssSubscribeData.platform())
                .build();
    }

    public static SubscribeEntity from(Subscribe subscribe) {
        return SubscribeEntity.builder()
            .id(subscribe.getId())
            .title(subscribe.getTitle())
            .url(subscribe.getUrl())
            .platform(subscribe.getPlatform())
            .build();
    }

    /**
     * 이 SubscribeEntity를 새로 추가된 구독을 나타내는 Subscribe Domain 객체로 변환합니다.
     * 이 메서드는 구독이 새로 생성되었다는 것을 나타내는 플래그와 함께 Subscribe 도메인 객체를 초기화합니다.
     * SubscribeEntity가 데이터베이스에 아직 존재하지 않는 새 구독을 나타낼 때 이 메서드를 사용하세요.
     * (기존에 존재하던 구독일 경우 toExistingSubscribeDomain()으로 변환하세요.)
     *
     * @return 새로 추가된 구독 Subscribe Domain 객체
     */
    public Subscribe toNewSubscribeDomain() {
        return Subscribe.of(this.id, this.title, this.url, this.platform, true);
    }

    /**
     * 이 SubscribeEntity를 기존에 존재하던 구독을 나타내는 Subscribe Domain 객체로 변환합니다.
     * 이 메서드는 구독이 기존에 존재한다는 것을 나타내는 플래그와 함께 Subscribe 도메인 객체를 초기화합니다.
     * SubscribeEntity가 데이터베이스에 이미 존재하는 구독을 나타낼 때 이 메서드를 사용하세요.
     * (새로 추가된 구독일 경우 toNewSubscribeDomain()으로 변환하세요.)
     *
     * @return 기존에 존재하던 구독 Subscribe Domain 객체
     */
    public Subscribe toExistingSubscribeDomain() {
        return Subscribe.of(this.id, this.title, this.url, this.platform, false);
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
