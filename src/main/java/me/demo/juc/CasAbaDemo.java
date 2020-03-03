package me.demo.juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/***
 * CAS中的ABA问题演示
 */
public class CasAbaDemo {
    public static void main(String[] args) {
        adbProblem();
        abaProblemResolveByStampedReference();
    }

    private static void abaProblemResolveByStampedReference() {
        AtomicStampedReference<Integer> atomicIn = new AtomicStampedReference<>(5, 0);
        new Thread(() -> {
            int stamp = atomicIn.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 原始版本号=" + stamp);
            atomicIn.compareAndSet(5, 6, 0, 1);
            System.out.println(Thread.currentThread().getName() + "\t 第1次版本号=" + atomicIn.getStamp());
            atomicIn.compareAndSet(6, 5, 1, 2);
            System.out.println(Thread.currentThread().getName() + "\t 第2次版本号=" + atomicIn.getStamp());
            System.out.println(Thread.currentThread().getName() + "\t 当前最新值=" + atomicIn.getReference());
        }).start();

        new Thread(() -> {
            int stamp = atomicIn.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 原始版本号=" + stamp);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean b = atomicIn.compareAndSet(5, 2020, 0, 1);
            System.out.println(Thread.currentThread().getName() + "\t 是否修改成功=" + b);
            if (b) {
                System.out.println(Thread.currentThread().getName() + "\t 当前值=" + atomicIn.getReference());
            }
            System.out.println(Thread.currentThread().getName() + "\t 当前最新值=" + atomicIn.getReference());
        }).start();
    }
    private static void adbProblem() {
        AtomicInteger atomicIn = new AtomicInteger(5);
        new Thread(() -> {
            atomicIn.getAndIncrement();
            atomicIn.compareAndSet(5, 6);
            atomicIn.compareAndSet(6, 5);
            System.out.println(Thread.currentThread().getName() + "\t 当前最新值=" + atomicIn.get());

        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 当前最新值=" + atomicIn.get());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicIn.compareAndSet(5, 2020);
            System.out.println(Thread.currentThread().getName() + "\t 当前值=" + atomicIn.get());
        }).start();
    }
}
