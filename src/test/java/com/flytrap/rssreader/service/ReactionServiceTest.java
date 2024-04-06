package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.generateMemberEntity;
import static com.flytrap.rssreader.fixture.FixtureFactory.generatePostEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flytrap.rssreader.api.member.infrastructure.entity.MemberEntity;
import com.flytrap.rssreader.api.member.infrastructure.repository.MemberEntityJpaRepository;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.post.infrastructure.repository.PostEntityJpaRepository;
import com.flytrap.rssreader.api.reaction.business.service.ReactionService;
import com.flytrap.rssreader.api.reaction.infrastructure.entity.ReactionEntity;
import com.flytrap.rssreader.api.reaction.infrastructure.repository.ReactionEntityJpaRepository;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ReactionServic 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
public class ReactionServiceTest {

    @Mock
    PostEntityJpaRepository postRepository;

    @Mock
    MemberEntityJpaRepository memberRepository;

    @Mock
    ReactionEntityJpaRepository reactionRepository;

    @InjectMocks
    ReactionService reactionService;

    @Test
    @DisplayName("POST에 이모지 리액션 달기 성공.")
    void post_add_reaction_success() {

        // given
        MemberEntity member = generateMemberEntity();
        PostEntity post = generatePostEntity(1L);

        // when
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(reactionRepository.save(Mockito.any(ReactionEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        reactionService.addReaction(post.getId(), member.getId(), "👍");

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            verify(reactionRepository, times(1)).save(any());
        });
    }
}
