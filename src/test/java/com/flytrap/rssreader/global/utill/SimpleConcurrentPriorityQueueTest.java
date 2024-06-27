package com.flytrap.rssreader.global.utill;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.flytrap.rssreader.api.post.infrastructure.system.CollectPriority;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class SimpleConcurrentPriorityQueueTest {

    @Test
    void 요소들이_우선순위에_맞게_삽입_인출_된다() {
        SimplePriorityQueue<TestVo> priorityQueue = new SimpleConcurrentPriorityQueue<>();

        final int loopTime = 50;
        int index = 0;
        while (index < loopTime) {
            priorityQueue.add(new TestVo(index++ + ""), CollectPriority.LOW);
        }

        priorityQueue.add(new TestVo("Fist"), CollectPriority.HIGH);
        priorityQueue.add(new TestVo("Second"), CollectPriority.HIGH);
        priorityQueue.add(new TestVo("Third"), CollectPriority.HIGH);

        assertAll(
            () -> assertThat(priorityQueue.poll().value).isEqualTo("Fist"),
            () -> assertThat(priorityQueue.poll().value).isEqualTo("Second"),
            () -> assertThat(priorityQueue.poll().value).isEqualTo("Third"),
            () -> {
                for (int i = 0; i < loopTime; i++) {
                    assertThat(priorityQueue.poll().value).isEqualTo(i + "");
                }
            }
        );
    }

    @Test
    void 동시_삽입이_가능하다() throws ExecutionException, InterruptedException {
        SimplePriorityQueue<TestVo> priorityQueue = new SimpleConcurrentPriorityQueue<>();

        final int threadSize = 4;
        final int itemCount = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(threadSize);

        Runnable producer = () -> {
            for (int i = 0; i < itemCount; i++) {
                if (i % 2 == 0) {
                    priorityQueue.add(new TestVo(i + ""), CollectPriority.LOW);
                } else {
                    priorityQueue.add(new TestVo(i + ""), CollectPriority.HIGH);
                }
            }
        };

        var futures = new CompletableFuture[threadSize];
        for (int i = 0; i < threadSize; i++) {
            futures[i] = CompletableFuture.runAsync(producer, executor);
        }
        CompletableFuture.allOf(futures).get();

        assertThat(priorityQueue.size()).isEqualTo(itemCount * threadSize);
    }

    @Test
    void 동시_인출이_가능하다() throws ExecutionException, InterruptedException {
        SimplePriorityQueue<TestVo> priorityQueue = new SimpleConcurrentPriorityQueue<>();

        final int threadSize = 4;
        final int itemCount = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(threadSize);

        for (int i = 0; i < threadSize * itemCount; i++) {
            if (i % 2 == 0) {
                priorityQueue.add(new TestVo(i + ""), CollectPriority.LOW);
            } else {
                priorityQueue.add(new TestVo(i + ""), CollectPriority.HIGH);
            }
        }

        Runnable consumer = () -> {
            for (int i = 0; i < itemCount; i++) {
                TestVo testVo = priorityQueue.poll();
            }
        };

        var futures = new CompletableFuture[threadSize];
        for (int i = 0; i < threadSize; i++) {
            futures[i] = CompletableFuture.runAsync(consumer, executor);
        }
        CompletableFuture.allOf(futures).get();

        assertThat(priorityQueue.size()).isEqualTo(0);
    }

    record TestVo(String value) { }
}