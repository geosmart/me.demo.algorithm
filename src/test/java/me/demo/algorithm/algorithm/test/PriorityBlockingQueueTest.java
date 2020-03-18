package me.demo.algorithm.algorithm.test;

import com.alibaba.fastjson.JSON;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import me.demo.algorithm.heap.PriorityBlockingQueue;

/**
 * @author geosmart
 */
public class PriorityBlockingQueueTest {

    @Test
    public void test_priorityBlockingQueue() throws InterruptedException {
        PriorityBlockingQueue queue = new PriorityBlockingQueue(Arrays.asList(11, 0, 9, 8, 6, 1, 4, 5, 3, 2, 7));
        System.out.println(JSON.toJSONString(queue));

        System.out.println(String.format("%s queue size[%s]", Thread.currentThread().getName(), queue.size()));
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.add(10 * finalI);
                System.out.println(String.format("%s queue add size[%s]", Thread.currentThread().getName(), queue.size()));
            }).start();
        }
        latch.countDown();
        Thread.currentThread().join();
    }
}
