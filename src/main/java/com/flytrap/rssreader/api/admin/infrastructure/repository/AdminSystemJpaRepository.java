package com.flytrap.rssreader.api.admin.infrastructure.repository;

import com.flytrap.rssreader.api.admin.infrastructure.entity.AdminSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSystemJpaRepository extends JpaRepository<AdminSystemEntity, Long> {

}
