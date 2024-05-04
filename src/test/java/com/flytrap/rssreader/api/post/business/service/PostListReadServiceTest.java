package com.flytrap.rssreader.api.post.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.folder.domain.AccessibleFolder;
import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.folder.domain.FolderId;
import com.flytrap.rssreader.api.folder.domain.SharedStatus;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderQuery;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostQuery;
import com.flytrap.rssreader.global.exception.domain.ForbiddenAccessFolderException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostListReadServiceTest {

    @InjectMocks
    PostListReadService postListReadService;

    @Mock
    PostQuery postQuery;

    @Mock
    FolderQuery folderQuery;

    @Nested
    @DisplayName("Folder별 Post 목록 조회하기")
    class GetPostsByFolder {

        @Test
        @DisplayName("Folder별 Post 목록을 pageSize만큼 조회할 수 있다.")
        void test_get_posts_by_folder() {
            // Given
            AccountId accountId = new AccountId(1L);
            FolderId folderId = new FolderId(1L);
            PostFilter postFilter = new PostFilter(false, null, null, null);
            int pageSize = 10;
            Pageable pageable = Pageable.ofSize(pageSize);
            AccessibleFolder accessibleFolder = AccessibleFolder.builder()
                .id(folderId).sharedStatus(SharedStatus.PRIVATE)
                .build();
            List<Post> posts = List.of(
                Post.builder().id(new PostId(1L)).build(),
                Post.builder().id(new PostId(2L)).build(),
                Post.builder().id(new PostId(3L)).build(),
                Post.builder().id(new PostId(4L)).build(),
                Post.builder().id(new PostId(5L)).build(),
                Post.builder().id(new PostId(6L)).build(),
                Post.builder().id(new PostId(7L)).build(),
                Post.builder().id(new PostId(8L)).build(),
                Post.builder().id(new PostId(9L)).build(),
                Post.builder().id(new PostId(10L)).build()
            );

            // When
            when(folderQuery.readAccessible(folderId, accountId))
                .thenReturn(accessibleFolder);
            when(postQuery.readAllByFolder(accountId, folderId, postFilter, pageable))
                .thenReturn(posts);
            List<Post> result = postListReadService.getPostsByFolder(accountId, folderId, postFilter, pageable);

            // Then
            assertEquals(pageSize, result.size());
        }

        @Test
        @DisplayName("Account가 접근 불가능한 Folder의 Post 목록은 조회할 수 없다.")
        void test_get_posts_from_inaccessible_folder() {
            // Given
            AccountId accountId = new AccountId(1L);
            FolderId folderId = new FolderId(1L);
            PostFilter postFilter = new PostFilter(false, null, null, null);
            Pageable pageable = Pageable.ofSize(10);
            Folder folder = Folder.builder()
                .id(folderId.value()).isShared(false)
                .build();

            // When
            when(folderQuery.readAccessible(folderId, accountId))
                .thenThrow(new ForbiddenAccessFolderException(folder));

            // Then
            assertThrows(ForbiddenAccessFolderException.class, () -> {
                postListReadService.getPostsByFolder(accountId, folderId, postFilter, pageable);
            });
        }
    }

}
