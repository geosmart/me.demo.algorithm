package me.demo.algorithm.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author geosmart
 */
public class SortingUtil {

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int[] convertListToArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int j = 0; j < array.length; j++) {
            array[j] = list.get(j);
        }
        return array;
    }

    public static List<Integer> convertArrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < array.length; j++) {
            list.add(array[j]);
        }
        return list;
    }
}
