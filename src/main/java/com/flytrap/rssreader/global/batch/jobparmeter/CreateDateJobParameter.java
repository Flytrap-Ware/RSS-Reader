package com.flytrap.rssreader.global.batch.jobparmeter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateDateJobParameter {

    private LocalDate pubDate;

    @Value("#{jobParameters[pubDate]}")
    public void setCreateDate(String pubDate) {
        this.pubDate = LocalDate.parse(pubDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
