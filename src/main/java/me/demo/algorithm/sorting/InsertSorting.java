package me.demo.algorithm.sorting;


import static me.demo.algorithm.sorting.QuikSorting.print;

/**
 * 插入排序算法
 *
 * @author geosmart
 * @复杂度 平方阶 n*(n-1)/2
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
                    print(arr);
                } else {
                    System.out.println("insert extracted element");
                    arr[j + 1] = extractedItem;
                    print(arr);
                    break;
                }
            }
        }
    }

}