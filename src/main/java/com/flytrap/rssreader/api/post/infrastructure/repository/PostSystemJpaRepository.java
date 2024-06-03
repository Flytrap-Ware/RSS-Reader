package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostSystemJpaRepository extends JpaRepository<PostSystemEntity, Long> {

}
