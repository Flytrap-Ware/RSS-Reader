package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostListReadRepository {

    Optional<PostSummaryOutput> findById(Long postId);

    List<PostSummaryOutput> findAllBySubscribe(long memberId, long subscribeId, PostFilter postFilter, Pageable pageable);

    List<PostSummaryOutput> findAllByFolder(long memberId, long folderId, PostFilter postFilter, Pageable pageable);

    List<PostSummaryOutput> findAllByMember(long memberId, PostFilter postFilter, Pageable pageable);

    List<PostSummaryOutput> findAllBookmarks(long memberId, PostFilter postFilter, Pageable pageable);
}
