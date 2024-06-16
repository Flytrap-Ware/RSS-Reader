package com.flytrap.rssreader.api.post.infrastructure.repository;

import com.flytrap.rssreader.api.post.infrastructure.entity.PostEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostMyBatisRepository {

    private final PostMyBatisMapper postMyBatisMapper;

    /**
     * PostEntity를 Bulk Upsert합니다.
     *
     * PostEntity 리스트가 null이 아니거나 비어 있지 않은지 확인합니다.
     * 리스트가 null이거나 비어 있으면 -1을 반환하여 오류 또는 작업이 수행되지 않았음을 나타냅니다.
     *
     * @param postEntities Bulk Upsert할 PostEntity 리스트
     * @return 리스트가 null이거나 비어 있는 경우, 그외 에러가 나면 -1을 반환하고, 그렇지 않으면 bulk upsert된 Entity의 수를 결과를 반환합니다.
     */
    public int bulkUpsert(List<PostEntity> postEntities) {
        if (postEntities == null || postEntities.isEmpty()) {
            return -1;
        }
        return postMyBatisMapper.bulkUpsert(postEntities);
    }
}
