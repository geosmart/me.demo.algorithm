package me.demo.algorithm.sorting;


import me.demo.algorithm.util.PrintUtil;

/**
 * 选择排序算法
 *
 * @author geosmart
 * @复杂度 稳定，时间复杂度 O(n^2)
 * @see [http://visualgo.net/sorting]
 */
public class SelectSort {
    /**
     * 选择排序
     */
    public static void selectSorting(int[] arr) {
        PrintUtil.print(arr);
        // repeat (numOfElements - 1) times
        for (int i = 0; i < arr.length - 1; i++) {
            System.out.println("minimum- " + arr[i]);
            // for each unsorted element
            int currentMinIndex = i;
            // set the first unsorted element as the minimum
            for (int j = i + 1; j < arr.length - 1; j++) {
                System.out.println("index- "+j + ", element > currentMinimum：" + arr[j + 1] + " < " + arr[currentMinIndex]);
                //element < currentMinimum
                if (arr[j + 1] < arr[currentMinIndex]) {
                    //set element as new minItem
                    currentMinIndex = j + 1;
                }
            }
            //swap minItem with first unsorted position
            System.out.println("currentMinIndex- " + currentMinIndex + ", swap " + arr[i] + " -- " + arr[currentMinIndex]);
            int temp = arr[i];
            arr[i] = arr[currentMinIndex];
            arr[currentMinIndex] = temp;
            PrintUtil.print(arr);
        }
    }
}
