package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.entity.BookmarkEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.BookmarkJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostCommand {

    private final PostJpaRepository postJpaRepository;
    private final PostOpenJpaRepository postOpenJpaRepository;
    private final BookmarkJpaRepository bookmarkJpaRepository;

    @Transactional(readOnly = true)
    public PostAggregate readAggregate(PostId postId, AccountId accountId) {

        boolean isRead = postOpenJpaRepository.existsByAccountIdAndPostId(
            accountId.value(), postId.value());
        boolean isBookmark = bookmarkJpaRepository.existsByAccountIdAndPostId(
            accountId.value(), postId.value());

        return postJpaRepository.findById(postId.value())
            .orElseThrow(() -> new NoSuchDomainException(PostAggregate.class))
            .toAggregate(Open.from(isRead), Bookmark.from(isBookmark));
    }

    @Transactional
    public void updateOnlyOpen(PostAggregate postAggregate, AccountId accountId) {
        boolean existOpenInDB = postOpenJpaRepository
            .existsByAccountIdAndPostId(accountId.value(), postAggregate.getId().value());
        if (existOpenInDB && !postAggregate.isOpened()) {
            postOpenJpaRepository
                .deleteByAccountIdAndPostId(accountId.value(), postAggregate.getId().value());
        } else if (!existOpenInDB && postAggregate.isOpened()) {
            OpenEntity openEntity = OpenEntity.from(accountId, postAggregate.getId());
            postOpenJpaRepository.save(openEntity);
        }
    }

    @Transactional
    public void updateOnlyBookmark(PostAggregate postAggregate, AccountId accountId) {
        boolean existBookmarkInDB = bookmarkJpaRepository
            .existsByAccountIdAndPostId(accountId.value(), postAggregate.getId().value());
        if (existBookmarkInDB && !postAggregate.isBookmarked()) {
            bookmarkJpaRepository
                .deleteByAccountIdAndPostId(accountId.value(), postAggregate.getId().value());
        } else if (!existBookmarkInDB && postAggregate.isBookmarked()) {
            BookmarkEntity bookmarkEntity = BookmarkEntity.create(accountId, postAggregate.getId());
            bookmarkJpaRepository.save(bookmarkEntity);
        }
    }

}
