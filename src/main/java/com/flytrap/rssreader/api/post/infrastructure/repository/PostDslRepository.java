package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface PostDslRepository {

    Optional<PostSummaryOutput> findById(Long postId);

    List<PostSummaryOutput> findAllByAccount(long accountId, PostFilter postFilter, Pageable pageable);

    List<PostSummaryOutput> findAllByFolder(long memberId, long folderId, PostFilter postFilter, Pageable pageable);

    List<PostSummaryOutput> findAllBySubscription(long memberId, long subscribeId, PostFilter postFilter, Pageable pageable);

    List<PostSummaryOutput> findAllBookmarked(long memberId, PostFilter postFilter, Pageable pageable);
}
