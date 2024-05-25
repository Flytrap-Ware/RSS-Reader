package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.BookmarkJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostDslRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenJpaRepository;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssResourceJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostQuery {

    private final PostJpaRepository postJpaRepository;
    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final PostOpenJpaRepository postOpenJpaRepository;
    private final PostDslRepository postDslRepository;
    private final RssResourceJpaRepository rssResourceJpaRepository;

    @Transactional(readOnly = true)
    public Optional<Post> read(PostId postId, AccountId accountId) {

        return postJpaRepository.findById(postId.value())
            .flatMap(postEntity -> rssResourceJpaRepository.findById(postEntity.getId())
                .map(rssSourceEntity -> {
                    boolean isRead = postOpenJpaRepository
                        .existsByAccountIdAndPostId(accountId.value(), postId.value());
                    boolean isBookmark = bookmarkJpaRepository
                        .existsByAccountIdAndPostId(accountId.value(), postId.value());

                    return postEntity.toReadOnly(
                        Open.from(isRead), Bookmark.from(isBookmark), rssSourceEntity);
                })
            );
    }

    @Transactional(readOnly = true)
    public List<Post> readAllByAccount(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {
        return postDslRepository
            .findAllByAccount(accountId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toReadOnly).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllByFolder(AccountId accountId, FolderId folderId, PostFilter postFilter,
        Pageable pageable) {
        return postDslRepository
            .findAllByFolder(accountId.value(), folderId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toReadOnly).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllBySubscription(AccountId accountId, RssSourceId rssSourceId,
        PostFilter postFilter, Pageable pageable) {
        return postDslRepository.findAllBySubscription(accountId.value(),
                rssSourceId.value(), postFilter, pageable).stream()
            .map(PostSummaryOutput::toReadOnly).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllBookmarked(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {
        return postDslRepository.findAllBookmarked(accountId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toReadOnly).toList();
    }

}
