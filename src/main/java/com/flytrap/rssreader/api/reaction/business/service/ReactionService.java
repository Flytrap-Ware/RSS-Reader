package com.flytrap.rssreader.api.reaction.business.service;

import com.flytrap.rssreader.api.member.infrastructure.entity.MemberEntity;
import com.flytrap.rssreader.api.member.infrastructure.repository.MemberEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.reaction.infrastructure.entity.ReactionEntity;
import com.flytrap.rssreader.api.reaction.infrastructure.repository.ReactionEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final PostEntityJpaRepository postRepository;
    private final MemberEntityJpaRepository memberRepository;
    private final ReactionEntityJpaRepository reactionRepository;

    @Transactional
    public Long addReaction(Long postId, Long memberId, String emoji) {

        PostEntity post = postRepository.findById(postId).orElseThrow();
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        return reactionRepository.save(ReactionEntity.create(post, member, emoji)).getId();
    }

    @Transactional
    public void deleteReaction(Long postId, Long memberId) {
        PostEntity post = postRepository.findById(postId).orElseThrow();
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        ReactionEntity reaction = reactionRepository.findByMemberAndPost(member, post)
                .orElseThrow();
        reactionRepository.delete(reaction);
    }
}
