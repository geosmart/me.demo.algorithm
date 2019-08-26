package me.demo.algorithm.algorithm.test;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import java.util.Arrays;

import me.demo.algorithm.heap.PriorityQueue;

/**
 * @author geosmart
 */
public class PriorityQueueTest {

    @Test
    public void test_init_priorityQueue() {
        PriorityQueue queue = new PriorityQueue(Arrays.asList(9, 8, 6, 1, 4, 5, 3, 2, 7));
        System.out.println(JSON.toJSONString(queue));
    }

}
