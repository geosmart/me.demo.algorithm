package me.demo.algorithm.algorithm.test;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import me.demo.algorithm.search.BinarySearch;
import me.demo.algorithm.util.PrintUtil;

/**
 * @author geosmart
 */
public class SearchTest {

    @Test
    public void test_binarysearch() {
        Integer[] list = new Integer[]{1, 2, 3, 4, 5, 7};
        int target = BinarySearch.bsearch(list, 7);
        PrintUtil.print(target);
        target = BinarySearch.bsearch(list, 5);
        PrintUtil.print(target);
    }

    @Test
    public void test_binarysearch_recurse() {
        Integer[] list = new Integer[]{1, 2, 3, 4, 5, 7};
        int target = BinarySearch.bsearch(list, 0, list.length - 1, 9);
        PrintUtil.print(target);
    }


}
