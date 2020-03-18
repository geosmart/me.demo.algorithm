package me.demo.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * Condition实现3个方法串行执行
 */
public class ReentrantLockConditionTest3 {
    @Test
    public void main() {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.one();
            }
        }, "T1").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.two();
            }
        }, "T2").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.three();
            }
        }, "T3").start();
    }

    /***
     * 共享资源：要求3个方法串行执行
     */
    public class ShareResource {
        //方法阻塞标识
        int number = 1;
        //初始化可重入锁
        final Lock lock = new ReentrantLock();
        //3个条件精准唤醒3个不同的线程来控制3个方法顺序执行
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();

        public void one() {
            try {
                lock.lock();
                if (number != 1) {
                    condition1.await();
                }
                System.out.println(String.format("%s run 1", Thread.currentThread().getName()));
                number = 2;
                condition2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void two() {
            try {
                lock.lock();
                if (number != 2) {
                    condition2.await();
                }
                System.out.println(String.format("%s run 2", Thread.currentThread().getName()));
                number = 3;
                condition3.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void three() {
            try {
                lock.lock();
                if (number != 3) {
                    condition3.await();
                }
                System.out.println(String.format("%s run 3", Thread.currentThread().getName()));
                number = 1;
                condition1.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}