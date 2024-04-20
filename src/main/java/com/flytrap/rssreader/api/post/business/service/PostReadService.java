package com.flytrap.rssreader.api.post.business.service;

import com.flytrap.rssreader.api.auth.presentation.dto.AccountSession;
import com.flytrap.rssreader.api.member.domain.AccountId;
import com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEvent;
import com.flytrap.rssreader.api.post.domain.Post;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostReader;
import com.flytrap.rssreader.global.event.PublishEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReadService {

    private final PostReader postReader;

    @PublishEvent(eventType = PostOpenEvent.class,
            params = "#{T(com.flytrap.rssreader.api.post.business.event.postOpen.PostOpenEventParam).create(#accountSession.id(), #postId.id())}")
    public Post getPost(AccountSession accountSession, PostId postId) {
        return postReader.read(postId, new AccountId(accountSession.id()));
    }

}
