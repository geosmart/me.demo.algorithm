package me.demo.juc.demo;

import java.util.List;
import java.util.Vector;

/**
 * 测试1、单线程偏向锁与单线程无锁差异，
 * 测试2、多线程偏向锁和多线程关闭偏向锁
 * 开启偏向锁：
 * -XX:+UseBiasedLocking
 * -XX:BiasedLockingStartupDelay=0
 * -Xmx512m
 * -Xms512m
 * 关闭偏向锁：
 * -XX:-UseBiasedLocking
 * -Xmx512m
 * -Xms512m
 *
 * @author biudefu
 * @since 2019/9/9
 */
public class TestBiasedLock {
    private static List<Integer> list = new Vector<Integer>();

    public static void main(String[] args) {
        long tsStart = System.currentTimeMillis();
        new TestBiasedLock().testConcurrentThreadDif();
        System.out.println(Thread.activeCount());
        while (Thread.activeCount() > 2) {

        }
        System.out.println("一共耗费：" + (System.currentTimeMillis() - tsStart) + " ms" + ",集合中元素的数量：" + list.size());
    }

    /**
     * 开启偏向锁：一共耗费：292 ms
     * 关闭偏向锁：一共耗费：469 ms
     * 差了快一倍
     */
    void testSingleThreadDif() {
        for (int i = 0; i < 10000000; i++) {
            list.add(i);
        }
    }

    /**
     * 开启偏向锁：一共耗费：876 ms
     * 关闭偏向锁：一共耗费：694 ms
     */
    void testConcurrentThreadDif() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    list.add(j);
                }
            }, "thread-" + String.valueOf(i)).start();
        }

    }

}