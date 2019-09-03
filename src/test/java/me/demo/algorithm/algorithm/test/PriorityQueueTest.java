package me.demo.algorithm.algorithm.test;

import com.alibaba.fastjson.JSON;

import me.demo.algorithm.heap.HeapPrinter;
import me.demo.algorithm.heap.PriorityHeap;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.demo.algorithm.heap.PriorityQueue;

/**
 * @author geosmart
 */
public class PriorityQueueTest {

    @Test
    public void test_init_priorityQueue() {
        PriorityQueue queue = new PriorityQueue(Arrays.asList(11, 0, 9, 8, 6, 1, 4, 5, 3, 2, 7));
        System.out.println(JSON.toJSONString(queue));
    }

    private static Integer[] numbers(int size) {
        Integer[] numbers = new Integer[size];
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            int randomInteger = r.nextInt();
            numbers[i] = randomInteger;
        }
        return numbers;
    }

    @Test
    public void test_topK() {
        int k = 10000;
        List<Integer> array = Arrays.asList(numbers(1000000));

        PriorityQueue topKQueue = new PriorityQueue();
        for (int i = 0; i < array.size(); i++) {
            int o = array.get(i);
            Object min = topKQueue.peek();
            if (topKQueue.size() < k) {
                topKQueue.add(o);
            } else if (topKQueue.size() >= k && min != null && o > (int) min) {
                //最小堆大小超过K且当前元素比堆顶大时，移除堆顶元素，并加入新元素
                topKQueue.poll();
                System.out.println(String.format("pop min %s", i));
                topKQueue.add(o);
//                HeapPrinter.dump(topKQueue.toArray());
            } else {
                System.out.println(String.format("skip %s", o));
            }
        }
        HeapPrinter.dump(topKQueue.toArray());
    }
}
