package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.folder.domain.Folder;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostFilter;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSummaryOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class PostListReadService {

    private final PostListReadRepository postListReadRepository;

    public List<Post> getPostsBySubscribe(SessionMember member, Long subscribeId, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllBySubscribe(member.id(), subscribeId, postFilter, pageable)
                .stream()
                .map(PostSummaryOutput::toDomain)
                .toList();
    }

    public List<Post> getPostsByFolder(SessionMember member, Folder folder, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllByFolder(member.id(), folder.getId(), postFilter, pageable)
                .stream()
                .map(PostSummaryOutput::toDomain)
                .toList();
    }

    public List<Post> getPostsByMember(SessionMember member, PostFilter postFilter, Pageable pageable) {
        return postListReadRepository.findAllByMember(member.id(), postFilter, pageable)
                .stream()
                .map(PostSummaryOutput::toDomain)
                .toList();
    }
}
