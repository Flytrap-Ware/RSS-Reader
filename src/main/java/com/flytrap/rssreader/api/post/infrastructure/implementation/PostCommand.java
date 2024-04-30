package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.entity.BookmarkEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenEntityRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostCommand {

    private final PostEntityJpaRepository postEntityJpaRepository;
    private final PostOpenEntityRepository postOpenEntityRepository;
    private final BookmarkEntityJpaRepository bookmarkEntityJpaRepository;

    @Transactional(readOnly = true)
    public PostAggregate readAggregate(PostId postId, AccountId accountId) {

        boolean isRead = postOpenEntityRepository.existsByMemberIdAndPostId(
            accountId.value(), postId.value());
        boolean isBookmark = bookmarkEntityJpaRepository.existsByMemberIdAndPostId(
            accountId.value(), postId.value());

        return postEntityJpaRepository.findById(postId.value())
            .orElseThrow(() -> new NoSuchDomainException(PostAggregate.class))
            .toAggregate(Open.from(isRead), Bookmark.from(isBookmark));
    }

    @Transactional
    public void updateOnlyOpen(PostAggregate postAggregate, AccountId accountId) {
        boolean existOpenInDB = postOpenEntityRepository
            .existsByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        if (existOpenInDB && !postAggregate.isOpened()) {
            postOpenEntityRepository
                .deleteByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        } else if (!existOpenInDB && postAggregate.isOpened()) {
            OpenEntity openEntity = OpenEntity.from(accountId, postAggregate.getId());
            postOpenEntityRepository.save(openEntity);
        }
    }

    @Transactional
    public void updateOnlyBookmark(PostAggregate postAggregate, AccountId accountId) {
        boolean existBookmarkInDB = bookmarkEntityJpaRepository
            .existsByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        if (existBookmarkInDB && !postAggregate.isBookmarked()) {
            bookmarkEntityJpaRepository
                .deleteByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        } else if (!existBookmarkInDB && postAggregate.isBookmarked()) {
            BookmarkEntity bookmarkEntity = BookmarkEntity.create(accountId, postAggregate.getId());
            bookmarkEntityJpaRepository.save(bookmarkEntity);
        }
    }

}
