package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenEntityRepository;
import com.flytrap.rssreader.api.subscribe.domain.Subscribe;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.api.subscribe.infrastructure.entity.SubscribeEntity;
import com.flytrap.rssreader.api.subscribe.infrastructure.repository.SubscribeEntityJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostQuery {

    private final PostEntityJpaRepository postEntityJpaRepository;
    private final BookmarkEntityJpaRepository bookmarkEntityJpaRepository;
    private final PostOpenEntityRepository postOpenEntityRepository;
    private final PostListReadRepository postListReadRepository;
    private final SubscribeEntityJpaRepository subscriptionEntityJpaRepository;

    @Transactional(readOnly = true)
    public Post read(PostId postId, AccountId accountId) {

        PostEntity postEntity = postEntityJpaRepository.findById(postId.value())
            .orElseThrow(() -> new NoSuchDomainException(Post.class));
        SubscribeEntity subscribeEntity = subscriptionEntityJpaRepository.findById(
                postEntity.getId())
            .orElseThrow(() -> new NoSuchDomainException(Subscribe.class));
        boolean isRead = postOpenEntityRepository.existsByMemberIdAndPostId(
            accountId.value(), postId.value());
        boolean isBookmark = bookmarkEntityJpaRepository.existsByMemberIdAndPostId(
            accountId.value(), postId.value());

        return postEntity.toDomain(Open.from(isRead), Bookmark.from(isBookmark), subscribeEntity);
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
    public List<Post> readAllBySubscription(AccountId accountId, SubscriptionId subscriptionId,
        PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBySubscription(accountId.value(),
                subscriptionId.value(), postFilter, pageable).stream()
            .map(PostSummaryOutput::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> readAllBookmarked(AccountId accountId, PostFilter postFilter,
        Pageable pageable) {
        return postListReadRepository.findAllBookmarked(accountId.value(), postFilter, pageable)
            .stream().map(PostSummaryOutput::toDomain).toList();
    }

    // TODO: Folder 리팩토링 때 다시 봐야함
    @Transactional(readOnly = true)
    public Map<SubscriptionId, PostSubscribeCountOutput> countPostsInSubscription(
        SubscriptionId subscriptionId) {

        return postEntityJpaRepository.countBySubscriptions(List.of(subscriptionId.value()))
            .stream()
            .collect(Collectors.toMap(
                output -> new SubscriptionId(output.getSubscriptionId()),
                output -> output
            ));
    }

    // TODO: Folder 리팩토링 때 다시 봐야함
    @Transactional(readOnly = true)
    public Map<SubscriptionId, PostSubscribeCountOutput> countPostsInSubscriptions(
        List<SubscriptionId> subscriptionIds) {

        return postEntityJpaRepository.countBySubscriptions(
                subscriptionIds.stream().map(SubscriptionId::value).toList())
            .stream()
            .collect(Collectors.toMap(
                output -> new SubscriptionId(output.getSubscriptionId()),
                output -> output
            ));
    }
}
