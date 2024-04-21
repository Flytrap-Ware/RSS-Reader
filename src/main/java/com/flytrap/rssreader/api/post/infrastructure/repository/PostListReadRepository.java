package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface PostListReadRepository {

    Optional<Post> findById(Long postId);

    List<Post> findAllByAccount(long accountId, PostFilter postFilter, Pageable pageable);

    List<Post> findAllByFolder(long memberId, long folderId, PostFilter postFilter, Pageable pageable);

    List<Post> findAllBySubscription(long memberId, long subscribeId, PostFilter postFilter, Pageable pageable);

    List<Post> findAllBookmarked(long memberId, PostFilter postFilter, Pageable pageable);
}
