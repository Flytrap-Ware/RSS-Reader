package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.bookmark.infrastructure.entity.BookmarkEntity;
import com.flytrap.rssreader.api.bookmark.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostUpdater {

    private final PostEntityJpaRepository postEntityJpaRepository;
    private final PostOpenEntityRepository postOpenEntityRepository;
    private final BookmarkEntityJpaRepository bookmarkEntityJpaRepository;

    // Open과 Bookmark 업데이트는 중요하지 않습니다. Post만 업데이트되면 괜찮으므로, @Transactional이 필요하지 않습니다.
    public void update(PostAggregate postAggregate, AccountId accountId) {
        PostEntity postEntity = PostEntity.from(postAggregate);
        postEntityJpaRepository.save(postEntity);

        boolean existOpenInDB = postOpenEntityRepository
            .existsByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        if (existOpenInDB && !postAggregate.isOpened()) {
            postOpenEntityRepository
                .deleteByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        } else if (!existOpenInDB && postAggregate.isOpened()) {
            OpenEntity openEntity = OpenEntity.from(accountId, postAggregate.getId());
            postOpenEntityRepository.save(openEntity);
        }

        boolean existBookmarkInDB = bookmarkEntityJpaRepository
            .existsByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        if (existBookmarkInDB && !postAggregate.isBookmarked()) {
            bookmarkEntityJpaRepository
                .deleteAllByMemberIdAndPostId(accountId.value(), postAggregate.getId().value());
        } else if (!existBookmarkInDB && postAggregate.isBookmarked()) {
            BookmarkEntity bookmarkEntity = BookmarkEntity.create(accountId, postAggregate.getId());
            bookmarkEntityJpaRepository.save(bookmarkEntity);
        }
    }

}
