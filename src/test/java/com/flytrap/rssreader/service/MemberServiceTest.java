package com.flytrap.rssreader.service;

import static com.flytrap.rssreader.fixture.FixtureFactory.generateMemberEntity;
import static com.flytrap.rssreader.fixture.FixtureFactory.generateUserResource;
import static com.flytrap.rssreader.fixture.FixtureFields.MemberFields;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

import java.util.Optional;

import com.flytrap.rssreader.api.member.business.service.MemberService;
import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.api.member.infrastructure.repository.MemberEntityJpaRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Member 서비스 로직 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberEntityJpaRepository memberEntityJpaRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("기존에 존재하는 멤버인 경우 회원을 저장 하지 않고 멤버를 반환한다.")
    void loginMemberWithExistMember() {
        // given
        when(memberEntityJpaRepository.findByOauthPk(1L))
                .thenReturn(Optional.of(generateMemberEntity()));

        // when
        Member existMember
                = memberService.loginMember(generateUserResource());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(existMember.getId()).isEqualTo(MemberFields.id);
            softAssertions.assertThat(existMember.getEmail()).isEqualTo(MemberFields.email);
            softAssertions.assertThat(existMember.getProfile()).isEqualTo(MemberFields.profile);
            verify(memberEntityJpaRepository, times(0)).save(any());
        });
    }

    @Test
    @DisplayName("기존에 존재하지 않는 멤버 DB에 저장한 후 멤버를 반환한다.")
    void loginMemberWithNotExistMember() {
        //given
        when(memberEntityJpaRepository.findByOauthPk(1L))
                .thenReturn(Optional.empty());

        when(memberEntityJpaRepository.save(any()))
                .thenReturn(generateMemberEntity());

        // when
        Member newMember
                = memberService.loginMember(generateUserResource());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(newMember.getId()).isEqualTo(MemberFields.id);
            softAssertions.assertThat(newMember.getEmail()).isEqualTo(MemberFields.email);
            softAssertions.assertThat(newMember.getProfile()).isEqualTo(MemberFields.profile);
            verify(memberEntityJpaRepository, times(1)).save(any());
        });
    }
}
