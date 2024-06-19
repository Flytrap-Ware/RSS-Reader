package com.flytrap.rssreader.api.post.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "post_system")
public class PostSystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant endTime;

    private Long elapsedTimeMillis;
    private Integer rssCount;
    private Integer postCount;
    private Integer threadCount;
    private Integer parsingFailureCount;
    private Integer insertFailureCount;

    @Builder
    protected PostSystemEntity(Long id, Instant startTime, Instant endTime, Long elapsedTimeMillis,
        Integer rssCount, Integer postCount, Integer threadCount, Integer parsingFailureCount,
        Integer insertFailureCount) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.elapsedTimeMillis = elapsedTimeMillis;
        this.rssCount = rssCount;
        this.postCount = postCount;
        this.threadCount = threadCount;
        this.parsingFailureCount = parsingFailureCount;
        this.insertFailureCount = insertFailureCount;
    }

}
