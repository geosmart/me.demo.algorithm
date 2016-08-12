package me.demo.algorithm.algorithm.test;

import org.junit.Test;

import me.demo.algorithm.sorting.MergeSorting;
import me.demo.algorithm.sorting.SelectSorting;
import me.demo.algorithm.util.PrintUtil;

import static me.demo.algorithm.sorting.BubbleSorting.bubSort3;
import static me.demo.algorithm.sorting.InsertSorting.insertSorting;

/**
 * @author geosmart
 */
public class SortingTest {

    @Test
    public void test_bubbleSorting() {
        int[] list = new int[]{5, 3, 1, 4, 2};
        bubSort3(list);
        PrintUtil.print(list);
    }

    @Test
    public void test_insertSorting() {
        int[] list = new int[]{5, 3, 1, 4, 2};
        insertSorting(list);

    }

    @Test
    public void test_selectSorting() {
        int[] list = new int[]{5, 3, 1, 4, 2};
        SelectSorting.selectSorting(list);
    }


    @Test
    public void test_mergeSorting() {
        //偶数
        int[] list1 = new int[]{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
        //奇数
//        int[] list2 = new int[]{4, 3, 1, 5, 2};
        MergeSorting.mergeSorting(list1);
    }


}
