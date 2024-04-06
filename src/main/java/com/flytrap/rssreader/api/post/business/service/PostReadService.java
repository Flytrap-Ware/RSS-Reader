package com.flytrap.rssreader.api.post.business.service;


import com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEvent;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.infrastructure.output.PostSubscribeCountOutput;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostListReadRepository;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostOpenRepository;
import com.flytrap.rssreader.global.event.PublishEvent;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import com.flytrap.rssreader.api.auth.presentation.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostEntityJpaRepository postEntityJpaRepository;
    private final PostListReadRepository postListReadRepository;
    private final PostOpenRepository postOpenRepository;

    @Transactional(readOnly = true)
    @PublishEvent(eventType = PostOpenEvent.class,
            params = "#{T(com.flytrap.rssreader.service.dto.PostOpenParam).create(#sessionMember.id(), #postId)}")
    public Post getPost(SessionMember sessionMember, Long postId) {

        return postListReadRepository.findById(postId)
                .orElseThrow(() -> new NoSuchDomainException(Post.class))
                .toDomain();
    }

    @Transactional(readOnly = true)
    public Map<Long, PostSubscribeCountOutput> countPosts(List<Long> subscribes) {
        return postEntityJpaRepository.findSubscribeCounts(subscribes).stream()
                .collect(Collectors.toMap(PostSubscribeCountOutput::getSubscribeId, it -> it));
    }

    public Post findById(Long postId) {
        return postEntityJpaRepository.findById(postId)
                .orElseThrow(() -> new NoSuchDomainException(Post.class))
                .toDomain();
    }
}
