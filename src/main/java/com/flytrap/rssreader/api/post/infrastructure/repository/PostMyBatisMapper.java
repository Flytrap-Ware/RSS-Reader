package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMyBatisMapper {
    int bulkUpsert(List<PostEntity> postEntities);
}
