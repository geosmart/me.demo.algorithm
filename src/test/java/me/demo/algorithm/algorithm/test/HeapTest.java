package me.demo.algorithm.algorithm.test;

import com.alibaba.fastjson.JSON;

import org.junit.Test;

import me.demo.algorithm.heap.PriorityHeap;


public class HeapTest {


    /**
     * 参考 @http://www.programcreek.com/2013/03/hashmap-vs-treemap-vs-hashtable-vs-linkedhashmap/
     */
    @Test
    public void test_heap() {
        Object[] array = new Object[]{1, 2, 3, 7, 4, 5, 6, 8, 9};
//        Object[] array = new Object[]{9, 8, 6, 1, 4, 5, 3, 2, 7};
        PriorityHeap heap = new PriorityHeap(array);

        heap.heapify();
        System.out.println("heapify heap.");

//        heap.concat(0);

        heap.printHeap();
//
//        System.out.println(heap.peak());

        System.out.println(JSON.toJSONString(heap.heapSort()));
//        for (int i = 0; i < array.length; i--) {
//            Object res = heap.pop();
//            System.out.println(String.format("pop[%s]", res));
//            heap.printHeap();
//        }
    }

}
