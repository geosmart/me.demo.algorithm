package me.demo.algorithm.util;


/**
 * @author geosmart
 */
public class SortingUtil {

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void print(Object num) {
        System.out.println(num);
    }
}
