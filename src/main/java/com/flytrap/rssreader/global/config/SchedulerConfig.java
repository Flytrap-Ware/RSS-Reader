package com.flytrap.rssreader.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final Job crawlingJob;
    private final Job statJob; // 통계 Job 추가
    private final JobLauncher jobLauncher;

    // @Scheduled(cron = "${scheduler.cron-job}")
    //크롤링 Job 실행 스케줄러
    @Scheduled(fixedDelay = 1000)
    public void runJob() {
        executeJob(crawlingJob);
    }

    // 통계 Job 실행 스케줄러
    @Scheduled(cron = "0 0 23 * * *") // 매일 오후 11시에 실행
    public void runStatisticsJob() {
        executeJob(statJob);
    }

    private void executeJob(Job job) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        log.debug("작업 시작 시간: {}", formattedDateTime);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        try {
            log.debug("Job in execution: {}", job.getName());
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            log.debug("Job completed with state: {}", jobExecution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

