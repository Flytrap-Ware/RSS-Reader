package com.flytrap.rssreader.api.post.domain;

import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.global.exception.domain.InconsistentDomainException;
import com.flytrap.rssreader.global.model.Domain;
import com.flytrap.rssreader.global.model.DefaultDomain;
import java.time.Instant;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
@Domain(name = "post")
public class PostAggregate implements DefaultDomain {

    private final PostId id;
    private final String guid;
    private final String title;
    private final String thumbnailUrl;
    private final String description;
    private final Instant pubDate;
    private final RssSourceId rssSourceId;
    private Open open;
    private Bookmark bookmark;

    @Builder
    protected PostAggregate(PostId id, String guid, String title, String thumbnailUrl,
        String description,
        Instant pubDate, RssSourceId rssSourceId, Open open, Bookmark bookmark) {
        this.id = id;
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.pubDate = pubDate;
        this.rssSourceId = rssSourceId;
        this.open = open;
        this.bookmark = bookmark;
    }

    public boolean isOpened() {
        return this.open.flag();
    }

    public void markAsOpened() {
        this.open = Open.MARKED;
    }

    public void unmarkAsOpened() {
        this.open = Open.UNMARKED;
    }

    public boolean isBookmarked() {
        return this.bookmark.flag();
    }

    public void markAsBookmarked() {
        this.bookmark = Bookmark.MARKED;
    }

    public void unmarkAsBookmarked() {
        this.bookmark = Bookmark.UNMARKED;
    }

    public Post toReadOnly(RssSource rssSource) {
        if (!Objects.equals(rssSource.getId(), rssSourceId.value())) {
            throw new InconsistentDomainException(PostAggregate.class);
        }

        return Post.builder()
            .id(id)
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
}
