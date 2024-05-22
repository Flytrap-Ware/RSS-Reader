package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatEntityJpaRepository extends JpaRepository<PostStatEntity, Long> {

}
