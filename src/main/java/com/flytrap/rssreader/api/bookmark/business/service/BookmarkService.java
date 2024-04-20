package com.flytrap.rssreader.api.bookmark.business.service;

import com.flytrap.rssreader.api.bookmark.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import com.flytrap.rssreader.api.bookmark.infrastructure.entity.BookmarkEntity;
import com.flytrap.rssreader.api.bookmark.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class BookmarkService {

    private final PostListReadRepository postListReadRepository;
    private final BookmarkEntityJpaRepository bookmarkRepository;

    public List<Post> getBookmarks(AccountSession member, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBookmarks(member.id(), postFilter, pageable)
            .stream()
            .map(PostSummaryOutput::toDomain)
            .toList();
    }

    public Bookmark addBookmark(AccountSession member, Post post) {

        if (existBookmark(member, post)) {
            throw new DuplicateDomainException(Bookmark.class);
        }

        return bookmarkRepository.save(BookmarkEntity.create(member.id(), post.getId().id())).toDomain();
    }

    public void removeBookmark(AccountSession member, Long postId) {

        bookmarkRepository.deleteAllByMemberIdAndPostId(member.id(), postId);
    }

    public boolean existBookmark(AccountSession member, Post post) {
        return bookmarkRepository.existsByMemberIdAndPostId(member.id(), post.getId().id());
    }

}
