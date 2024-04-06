package com.flytrap.rssreader.api.bookmark.business.service;

import com.flytrap.rssreader.api.bookmark.domain.Bookmark;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import com.flytrap.rssreader.global.exception.domain.NotBelongToMemberException;
import com.flytrap.rssreader.api.bookmark.infrastructure.repository.BookmarkEntityJpaRepository;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkVerifyOwnerService {

    private final BookmarkEntityJpaRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public Bookmark getVerifiedBookmark(SessionMember member, Long bookmarkId) {

       Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
           .orElseThrow(() -> new NoSuchDomainException(Bookmark.class))
           .toDomain();

        verifyOwnership(member, bookmark);

        return bookmark;
    }

    private void verifyOwnership(SessionMember member, Bookmark bookmark) {
        if (bookmark.getMemberId() != member.id()) {
            throw new NotBelongToMemberException(bookmark);
        }
    }
}
