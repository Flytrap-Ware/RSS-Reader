package com.flytrap.rssreader.api.post.infrastructure.entity;

import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Long subscriptionId;

    // TODO: React 추가 하기

    @Builder
    protected PostEntity(Long id, String guid, String title, String thumbnailUrl, String description, Instant pubDate,
                         Long subscriptionId) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.subscriptionId = subscriptionId;
    }

    public static PostEntity from(RssPostsData.RssItemData itemData, Long subscriptionId) {
        return PostEntity.builder()
                .guid(itemData.guid())
                .title(itemData.title())
                .thumbnailUrl(itemData.thumbnailUrl())
                .description(itemData.description())
                .pubDate(itemData.pubDate())
                .subscriptionId(subscriptionId)
                .build();
    }

    public static PostEntity from(PostAggregate postAggregate) {
        return PostEntity.builder()
            .id(postAggregate.getId().value())
            .guid(postAggregate.getGuid())
            .title(postAggregate.getTitle())
            .thumbnailUrl(postAggregate.getThumbnailUrl())
            .description(postAggregate.getDescription())
            .pubDate(postAggregate.getPubDate())
            .subscriptionId(postAggregate.getSubscriptionId().value())
            .build();
    }

    public void updateBy(RssPostsData.RssItemData itemData) {
        this.title = itemData.title();
        this.thumbnailUrl = itemData.thumbnailUrl();
        this.description = itemData.description();
    }

    public Post toDomain(Open open, Bookmark bookmark, SubscribeEntity subscription) {

        if (!Objects.equals(subscription.getId(), subscriptionId)) {
            throw new RuntimeException("정합성 일치하지 않음.");
        }

        return Post.builder()
                .id(new PostId(id))
                .subscribeTitle(subscription.getTitle())
                .guid(guid)
                .title(title)
                .thumbnailUrl(thumbnailUrl)
                .description(description)
                .pubDate(pubDate)
                .open(open)
                .bookmark(bookmark)
                .build();
    }

    public PostAggregate toAggregate(Open open, Bookmark bookmark) {
        return PostAggregate.builder()
            .id(new PostId(id))
            .subscriptionId(new SubscriptionId(subscriptionId))
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
