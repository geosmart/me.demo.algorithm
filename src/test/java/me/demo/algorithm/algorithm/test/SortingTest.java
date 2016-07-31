package me.demo.algorithm.algorithm.test;

import org.junit.Test;

import me.demo.algorithm.sorting.BubbleSorting;
import me.demo.algorithm.sorting.SortingUtil;

import static me.demo.algorithm.sorting.BubbleSorting.bubSort3;
import static me.demo.algorithm.sorting.InsertSorting.insertSorting;

/**
 * @author geosmart
 */
public class SortingTest {

    @Test
    public void test_bubbleSorting() {
        int[] list = new int[]{5, 3, 1, 4, 2};
        // int[] list = new int[] {2, 1, 3, 4, 5,};
        // bubSort1(list);
        // list = new int[] {5, 3, 1, 4, 2};
        // bubSort2(list);
        // list = new int[] {5, 3, 1, 4, 2};
        bubSort3(list);
        SortingUtil.print(list);
    }


    @Test
    public void test_insertSorting() {
        int[] list = new int[]{21, 25, 49, 25, 16, 8};
        insertSorting(list);

    }


}
