package com.flytrap.rssreader.api.post.infrastructure.entity;

import com.flytrap.rssreader.api.parser.dto.RssPostsData;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.domain.PostIdGenerator;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.global.exception.domain.InconsistentDomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "post")
public class PostEntity {

    @Id
    private String id;

    @Column(length = 2500)
    private String guid;

    @Column(length = 2500)
    private String title;

    @Column(length = 2500)
    private String thumbnailUrl;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "pub_date", columnDefinition = "TIMESTAMP")
    private Instant pubDate;

    @Column(nullable = false)
    private Long rssSourceId;

    @Builder
    protected PostEntity(String id, String guid, String title, String thumbnailUrl, String description, Instant pubDate,
                         Long rssSourceId) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.rssSourceId = rssSourceId;
    }

    public static PostEntity create(RssPostsData.RssItemData itemData, Long rssSourceId) {
        return PostEntity.builder()
                .id(PostIdGenerator.generateString(itemData.pubDate(), rssSourceId))
                .guid(itemData.guid())
                .title(itemData.title())
                .thumbnailUrl(itemData.thumbnailUrl())
                .description(itemData.description())
                .pubDate(itemData.pubDate())
                .rssSourceId(rssSourceId)
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
            .rssSourceId(postAggregate.getRssSourceId().value())
            .build();
    }

    public void updateBy(RssPostsData.RssItemData itemData) {
        this.title = itemData.title();
        this.thumbnailUrl = itemData.thumbnailUrl();
        this.description = itemData.description();
    }

    public Post toReadOnly(Open open, Bookmark bookmark, RssSourceEntity rssSource) {

        if (!Objects.equals(rssSource.getId(), rssSourceId)) {
            throw new InconsistentDomainException(Post.class);
        }

        return Post.builder()
                .id(new PostId(id))
                .subscribeTitle(rssSource.getTitle())
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
            .rssSourceId(new RssSourceId(rssSourceId))
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
