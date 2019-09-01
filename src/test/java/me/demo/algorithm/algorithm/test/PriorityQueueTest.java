package me.demo.algorithm.algorithm.test;

import com.alibaba.fastjson.JSON;
import me.demo.algorithm.heap.PriorityHeap;
import me.demo.algorithm.heap.PriorityQueue;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;


public class PriorityQueueTest {


    /**
     * 参考 @http://www.programcreek.com/2013/03/hashmap-vs-treemap-vs-hashtable-vs-linkedhashmap/
     */
    @Test
    public void test_priorityQueue() {
        Object[] array = new Object[]{0, 4, 1, 5, 6, 2, 3};
//        Object[] array = new Object[]{9, 8, 6, 1, 4, 5, 3, 2, 7};
        PriorityQueue queue = new PriorityQueue(Arrays.asList(array));

        Iterator remove1 = queue.iterator();
            remove1.remove();
        boolean remove = queue.remove(5);
        System.out.println(remove);
        System.out.println(JSON.toJSONString(queue));
    }

}
