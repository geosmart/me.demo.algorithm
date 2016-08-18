package me.demo.algorithm.sorting;


import me.demo.algorithm.util.PrintUtil;

/**
 * 冒泡排序算法
 *
 * @author geosmart
 * @复杂度 平方阶 n*(n-1)/2，稳定
 * @see [http://visualgo.net/sorting]
 */
public class BubbleSort {

    /**
     * 冒泡排序，直接排序，性能较差
     */
    public static void bubSort1(int[] arr) {
        int loopCount = 0;
        int i, j;
        boolean swapped = true;
        for (i = 0; i < arr.length && swapped; i++) {
            swapped = false;
            for (j = i + 1; j < arr.length; j++) {
                loopCount++;
                System.out.println(i + " > " + j + " ? " + arr[i] + ">" + arr[j] + "=" + (arr[i] > arr[j]));
                if (arr[i] > arr[j]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    swapped = true;
                    PrintUtil.print(arr);
                }
            }
            System.out.println("---\n");
        }
        System.out.println("loopCount：" + loopCount);
    }

    /**
     * 冒泡排序，j与j+1互换，往后冒泡
     */
    public static void bubSort2(int[] arr) {
        int loopCount = 0;
        int arrLength = arr.length;
        for (int i = 0; i < arrLength - 1; i++) {
            for (int j = 0; j < arrLength - i - 1; j++) {
                loopCount++;
                System.out.println(j + " > " + (j + 1) + " ? " + arr[j] + ">" + arr[j + 1] + "=" + (arr[j] > arr[j + 1]));
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    PrintUtil.print(arr);
                }
            }
        }
        System.out.println("loopCount：" + loopCount);
    }

    /**
     * 冒泡排序，j与j+1互换，往后冒泡
     */
    public static void bubSort3(int[] arr) {
        int loopCount = 0;
        for (int i = arr.length; i > 1; i--) {
            for (int j = 0; j < i - 1; j++) {
                System.out.println(i + "-" + j);
                loopCount++;
                System.out.println(j + " > " + (j + 1) + " ? " + arr[j] + ">" + arr[j + 1] + "=" + (arr[j] > arr[j + 1]));
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                    PrintUtil.print(arr);
                }
            }
        }
        System.out.println("loopCount：" + loopCount);
    }

}
