package com.flytrap.rssreader.api.member.infrastructure.repository;

import com.flytrap.rssreader.api.member.infrastructure.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberEntityJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByOauthPk(long oauthPk);

    List<MemberEntity> findAllByName(String name);

}
