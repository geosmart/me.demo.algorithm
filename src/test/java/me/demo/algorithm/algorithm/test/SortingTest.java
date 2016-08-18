package me.demo.algorithm.algorithm.test;

import org.junit.Test;

import me.demo.algorithm.sorting.CountingSort;
import me.demo.algorithm.sorting.MergeSort;
import me.demo.algorithm.sorting.QuikSort;
import me.demo.algorithm.sorting.SelectSort;
import me.demo.algorithm.util.PrintUtil;

import static me.demo.algorithm.sorting.BubbleSort.bubSort3;
import static me.demo.algorithm.sorting.InsertSort.insertSorting;

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
        SelectSort.selectSorting(list);
    }


    @Test
    public void test_mergeSorting() {
        //偶数
        int[] list1 = new int[]{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
        //奇数
//        int[] list2 = new int[]{4, 3, 1, 5, 2};
        MergeSort.mergeSorting(list1);
    }

    @Test
    public void test_quikSorting() {
        int[] list1 = new int[]{6, 1, 2, 7, 9, 3, 4, 5, 10, 8};
        QuikSort.quicksort2(list1, 0, list1.length - 1);
//        QuikSort.quicksort(list1, 0, list1.length - 1);
//        QuikSort.quicksort_desc(list1, 0, list1.length - 1);
        PrintUtil.print(list1);
    }

    @Test
    public void test_countingSort() {
        int[] list1 = new int[]{6, 1, 2, 7, 9, 3, 4, 5, 10, 8};
        list1 = new int[]{20, 3, 8, 7, 1, 2, 2, 2, 7, 3, 9, 8, 2, 1, 4, 2, 4, 6, 9, 2};
        CountingSort.countingSort(list1);
        PrintUtil.print(list1);
    }
}
