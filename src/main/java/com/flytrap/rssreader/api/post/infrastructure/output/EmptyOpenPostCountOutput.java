package com.flytrap.rssreader.api.post.infrastructure.output;

/**
 * 빈 OpenPostCount
 * 테이블에 레코드가 없을때 사용합니다.
 */
public class EmptyOpenPostCountOutput implements OpenPostCountOutput {
    @Override
    public long getSubscribeId() {
        return 0;
    }

    @Override
    public int getPostCount() {
        return 0;
    }
}
