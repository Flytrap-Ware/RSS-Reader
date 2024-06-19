package com.flytrap.rssreader.api.admin.infrastructure.system;

import com.flytrap.rssreader.api.admin.domain.AdminSystemAggregate;
import com.flytrap.rssreader.api.admin.infrastructure.implementation.AdminSystemCommand;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCollectionThreadPoolExecutor implements CommandLineRunner {

    private final AdminSystemCommand adminSystemCommand;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void run(String... args) throws Exception {
        AdminSystemAggregate adminSystemAggregate = adminSystemCommand.read();

        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("postCollect-");
        threadPoolTaskExecutor.setCorePoolSize(adminSystemAggregate.getCoreThreadPoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(adminSystemAggregate.getCoreThreadPoolSize());
        threadPoolTaskExecutor.initialize();
    }

    /**
     * ThreadPool의 core size를 재지정한다.
     * 현재 작업중인 task가 있다면 모두 수행한 뒤 corePoolSize가 변경되며
     * ThreadPool 내부의 BlockingQueue에 작업 대기중이던 task들은 무시된다.
     *
     * @param corePoolSize 재지정할 ThreadPool의 core size
     */
    public synchronized void resetCorePoolSize(int corePoolSize) {
        if (threadPoolTaskExecutor != null) {
            threadPoolTaskExecutor.shutdown();
        }

        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("postCollect-");
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(corePoolSize);
        threadPoolTaskExecutor.initialize();
    }

    public void execute(Runnable task) {
        threadPoolTaskExecutor.execute(task);
    }

    public <T> CompletableFuture<T> supplyAsync(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, threadPoolTaskExecutor);
    }

    public int getCorePoolSize() {
        return threadPoolTaskExecutor.getCorePoolSize();
    }
}
