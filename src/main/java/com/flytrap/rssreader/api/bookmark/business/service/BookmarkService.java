package com.flytrap.rssreader.api.bookmark.business.service;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.api.bookmark.domain.Bookmark;
import com.flytrap.rssreader.api.bookmark.infrastructure.entity.BookmarkEntity;
import com.flytrap.rssreader.api.bookmark.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.global.exception.domain.DuplicateDomainException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AllArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkEntityJpaRepository bookmarkRepository;

    public Bookmark addBookmark(AccountSession member, Post post) {

        if (existBookmark(member, post)) {
            throw new DuplicateDomainException(Bookmark.class);
        }

        return bookmarkRepository
            .save(BookmarkEntity.create(new AccountId(member.id()), post.getId()))
            .toDomain();
    }

    public void removeBookmark(AccountSession member, Long postId) {

        bookmarkRepository.deleteAllByMemberIdAndPostId(member.id(), postId);
    }

    public boolean existBookmark(AccountSession member, Post post) {
        return bookmarkRepository.existsByMemberIdAndPostId(member.id(), post.getId().value());
    }

}
