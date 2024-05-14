package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.BookmarkJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenJpaRepository;
import com.flytrap.rssreader.api.subscribe.domain.RssSource;
import com.flytrap.rssreader.api.subscribe.domain.RssSourceId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.RssSourceEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.RssResourceJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
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
    private final PostListReadRepository postListReadRepository;
    private final RssResourceJpaRepository rssResourceJpaRepository;

    @Transactional(readOnly = true)
    public Post read(PostId postId, AccountId accountId) {

        PostEntity postEntity = postJpaRepository.findById(postId.value())
            .orElseThrow(() -> new NoSuchDomainException(Post.class));
        RssSourceEntity rssSourceEntity = rssResourceJpaRepository.findById(
                postEntity.getId())
            .orElseThrow(() -> new NoSuchDomainException(RssSource.class));
        boolean isRead = postOpenJpaRepository.existsByAccountIdAndPostId(
            accountId.value(), postId.value());
        boolean isBookmark = bookmarkJpaRepository.existsByAccountIdAndPostId(
            accountId.value(), postId.value());

        return postEntity.toDomain(Open.from(isRead), Bookmark.from(isBookmark),
            rssSourceEntity);
    }

    @Transactional(readOnly = true)
    public List<Post> readAllByAccount(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {
        return postListReadRepository
            .findAllByAccount(accountId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllByFolder(AccountId accountId, FolderId folderId, PostFilter postFilter,
        Pageable pageable) {
        return postListReadRepository
            .findAllByFolder(accountId.value(), folderId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllBySubscription(AccountId accountId, RssSourceId rssSourceId,
        PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBySubscription(accountId.value(),
                rssSourceId.value(), postFilter, pageable).stream()
            .map(PostSummaryOutput::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllBookmarked(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {
        return postListReadRepository.findAllBookmarked(accountId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toDomain).toList();
    }

}
