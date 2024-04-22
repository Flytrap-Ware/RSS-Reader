package com.flytrap.rssreader.api.post.infrastructure.entity;

import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "rss_post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2500, nullable = false)
    private String guid;

    @Column(length = 2500, nullable = false)
    private String title;

    @Column(length = 2500, nullable = true)
    private String thumbnailUrl;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant pubDate;

    @ManyToOne
    @JoinColumn(name = "subscribe_id")
    private SubscribeEntity subscribe;

    // TODO: React 추가 하기

    @Builder
    protected PostEntity(Long id, String guid, String title, String thumbnailUrl, String description, Instant pubDate,
                         SubscribeEntity subscribe) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.subscribe = subscribe;
    }

    public static PostEntity from(RssPostsData.RssItemData itemData, SubscribeEntity subscribe) {
        return PostEntity.builder()
                .guid(itemData.guid())
                .title(itemData.title())
                .thumbnailUrl(itemData.thumbnailUrl())
                .description(itemData.description())
                .pubDate(itemData.pubDate())
                .subscribe(subscribe)
                .build();
    }

    public void updateBy(RssPostsData.RssItemData itemData) {
        this.title = itemData.title();
        this.thumbnailUrl = itemData.thumbnailUrl();
        this.description = itemData.description();
    }

    public Post toDomain(Open open, Bookmark bookmark) {
        return Post.builder()
                .id(new PostId(id))
                .subscribeTitle(subscribe.getTitle())
                .guid(guid)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .description(description)
                .pubDate(pubDate)
                .open(open)
                .bookmark(bookmark)
                .build();
    }

}
