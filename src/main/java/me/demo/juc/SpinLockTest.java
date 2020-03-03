package me.demo.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/***
 * 自旋锁示例
 */
public class SpinLockTest {
    static AtomicReference<Thread> atomicRef = new AtomicReference();

    /***
     * 输出
     * thread A comme invoke myLock
     * thread B comme invoke myLock
     * thread A invoke myUnLock
     * thread B invoke myUnLock
     */
    public static void main(String[] args) throws InterruptedException {
        SpinLockTest spinLock = new SpinLockTest();
        new Thread(() -> {
            spinLock.myLock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            spinLock.myUnLock();
        }, "A").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            spinLock.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
            }
            spinLock.myUnLock();
        }, "B").start();
    }

    public void myLock() {
        Thread t1 = Thread.currentThread();
        System.out.println(String.format("thread %s comme invoke myLock", t1.getName()));
        //循环方式阻塞获取锁
        while (!atomicRef.compareAndSet(null, t1)) {
        }
    }

    public void myUnLock() {
        Thread t1 = Thread.currentThread();
        atomicRef.compareAndSet(t1, null);
        System.out.println(String.format("thread %s invoke myUnLock", t1.getName()));
    }
}