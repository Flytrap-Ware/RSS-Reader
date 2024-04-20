package com.flytrap.rssreader.api.post.infrastructure.implementation;

import com.flytrap.rssreader.api.bookmark.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.domain.PostRead;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenEntityRepository;
import com.flytrap.rssreader.api.subscribe.domain.SubscriptionId;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostReader {

    private final PostEntityJpaRepository postEntityJpaRepository;
    private final BookmarkEntityJpaRepository bookmarkEntityJpaRepository;
    private final PostOpenEntityRepository postOpenEntityRepository;

    @Transactional(readOnly = true)
    public Post read(PostId postId, AccountId accountId) {

        boolean isRead = postOpenEntityRepository.existsByMemberIdAndPostId(accountId.id(),
            postId.id());
        boolean isBookmark = bookmarkEntityJpaRepository.existsByMemberIdAndPostId(accountId.id(),
            postId.id());

        return postEntityJpaRepository.findById(postId.id())
            .orElseThrow(() -> new NoSuchDomainException(Post.class))
            .toDomain(new PostRead(isRead), new Bookmark(isBookmark));
    }

    // TODO: Folder 리팩토링 때 다시 봐야함
    @Transactional(readOnly = true)
    public Map<SubscriptionId, PostSubscribeCountOutput> countPostsInSubscription(SubscriptionId subscriptionId) {

        return postEntityJpaRepository.findSubscribeCounts(List.of(subscriptionId.id()))
            .stream()
            .collect(Collectors.toMap(
                output -> new SubscriptionId(output.getSubscribeId()),
                output -> output
            ));
    }

    // TODO: Folder 리팩토링 때 다시 봐야함
    @Transactional(readOnly = true)
    public Map<SubscriptionId, PostSubscribeCountOutput> countPostsInSubscriptions(List<SubscriptionId> subscriptionIds) {

        return postEntityJpaRepository.findSubscribeCounts(
                subscriptionIds.stream().map(SubscriptionId::id).toList())
            .stream()
            .collect(Collectors.toMap(
                    output -> new SubscriptionId(output.getSubscribeId()),
                    output -> output
            ));
    }
}
