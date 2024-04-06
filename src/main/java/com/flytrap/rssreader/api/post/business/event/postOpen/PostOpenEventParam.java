package com.flytrap.rssreader.api.post.business.event.postOpen;

import com.flytrap.rssreader.api.post.infrastructure.entity.OpenEntity;

public record PostOpenEventParam(Long memberId,
                                 Long postId) {

    public static PostOpenEventParam create(Long memberId, Long postId) {
        return new PostOpenEventParam(memberId, postId);
    }

    public OpenEntity toEntity() {
        return OpenEntity.builder()
                .postId(postId)
                .memberId(memberId)
                .build();
    }
}
