package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "rss_post_stat")
public class PostStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant pubDate;

    @Enumerated(EnumType.STRING)
    private BlogPlatform platform;

    @Column
    private Long postCount;

    @Builder
    public PostStat(BlogPlatform platform, Long postCount, Instant pubDate) {
        this.platform = platform;
        this.postCount = postCount;
        this.pubDate = pubDate;
    }
}

