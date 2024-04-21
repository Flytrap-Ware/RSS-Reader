package com.flytrap.rssreader.api.reaction.infrastructure.repository;

import com.flytrap.rssreader.api.account.infrastructure.entity.AccountEntity;
import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import com.flytrap.rssreader.api.reaction.infrastructure.entity.ReactionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionEntityJpaRepository extends JpaRepository<ReactionEntity, Long> {

    Optional<ReactionEntity> findByMemberAndPost(AccountEntity member, PostEntity post);
}
