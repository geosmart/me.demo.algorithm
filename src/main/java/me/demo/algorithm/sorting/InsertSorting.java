package me.demo.algorithm.sorting;


import me.demo.algorithm.util.PrintUtil;
import me.demo.algorithm.util.SortingUtil;

/**
 * 插入排序算法
 *
 * @author geosmart
 * @复杂度 稳定，时间复杂度 O(n^2)
 * @see [http://visualgo.net/sorting]
 */
public class InsertSorting {
    /**
     * 直接插入排序
     */
    public static void insertSorting(int[] arr) {
        int extractedItem;
        //mark first element as sorted,so begin with 1
        for (int i = 1; i < arr.length; i++) {
            extractedItem = arr[i];
            System.out.println("lastSortedIndex: " + (i - 1) + " , extractedItem ： " + i + "- " + extractedItem);
            // for each unsorted element
            for (int j = i - 1; j >= 0; j--) {
                System.out.println(j + " currentSortedElement > extractedElement：" + arr[j] + " > " + extractedItem);
                //currentSortedElement > extractedElement
                if (arr[j] > extractedItem) {
                    System.out.println("move sorted element to the right by 1");
                    arr[j + 1] = arr[j];
                    arr[j] = extractedItem;
                    PrintUtil.print(arr);
                } else {
                    System.out.println("insert extracted element");
                    arr[j + 1] = extractedItem;
                    PrintUtil.print(arr);
                    break;
                }
            }
        }
    }
}
