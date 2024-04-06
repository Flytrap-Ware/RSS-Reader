package com.flytrap.rssreader.api.member.business.service;

import com.flytrap.rssreader.api.member.domain.Member;
import com.flytrap.rssreader.api.member.infrastructure.entity.MemberEntity;
import com.flytrap.rssreader.api.member.infrastructure.repository.MemberEntityJpaRepository;
import com.flytrap.rssreader.global.exception.domain.NoSuchDomainException;
import com.flytrap.rssreader.api.auth.infrastructure.external.dto.UserResource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberEntityJpaRepository memberEntityJpaRepository;

    /**
     * Member 정보를 반환합니다.
     * User resource가 Member 테이블에 존재하지 않으면 새로 가입 후 Member 정보를 반환합니다.
     * @param userResource
     * @return Member domain
     */
    @Transactional
    public Member loginMember(UserResource userResource) {

        return memberEntityJpaRepository.findByOauthPk(userResource.getId())
                .orElseGet(() -> joinMember(userResource.toDomainForCreate()))
                .toDomain();
    }

    /**
     * Member 테이블에 새로운 회원을 가입시킵니다.
     * @param member
     * @return
     */
    private MemberEntity joinMember(Member member) {
        return memberEntityJpaRepository.save(MemberEntity.from(member));
    }

    /**
     * 이름으로 회원을 검색합니다.
     * @param name
     * @return Member domain list
     */
    public List<Member> findByName(String name) {
        return memberEntityJpaRepository.findAllByName(name).stream()
                .map(MemberEntity::toDomain)
                .toList();
    }

    /**
     * id로 회원을 검색합니다.
     * @param inviteeId
     * @return Member domain
     * @throws NoSuchDomainException
     */
    public Member findById(long inviteeId) {
        return memberEntityJpaRepository.findById(inviteeId)
                .orElseThrow(() -> new NoSuchDomainException(Member.class))
                .toDomain();
    }

    /**
     * id 목록으로 회원을 검색합니다.
     * @param memberIds
     * @return Member domain list
     */
    public List<Member> findAllByIds(Collection<Long> memberIds) {
        return memberEntityJpaRepository.findAllById(memberIds).stream()
                .map(MemberEntity::toDomain)
                .toList();
    }
}
