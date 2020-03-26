package me.demo.juc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/***
 * 设计一个Java程序：10个线程并发访问一个List[0-100]，如何做到顺序输出
 */
public class ArrayListLock {
    @Test
    public void main() {
        ShareResource shareResource = new ShareResource();
        for (int i = 0; i < 10; i++) {
            new Thread(shareResource::print, "T" + i).start();
        }
    }

    /***
     * 共享资源：要求3个方法串行执行
     */
    public class ShareResource {
        int index;
        List<Integer> list = new ArrayList<>();

        public ShareResource() {
            for (int i = 0; i < 100; i++) {
                list.add(i);
            }
        }

        public synchronized void print() {
            for (int i = 0; i < 10; i++) {
                System.out.println(list.get(index));
                index++;
            }
        }
    }
}