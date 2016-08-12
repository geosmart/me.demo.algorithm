package me.demo.algorithm.sorting;


import me.demo.algorithm.util.PrintUtil;

/**
 * 合并排序算法(分而治之、各个击破)
 *
 * @author geosmart
 * @复杂度 归并排序的最好、最坏和平均时间复杂度都是O(nlogn)，而空间复杂度是O(n)，比较次数介于(nlogn)/2和(nlogn)-n+1，赋值操作的次数是(2nlogn)。
 * 归并排序算法比较占用内存，但却是效率高且稳定的排序算法。
 * @thought 将 2 个大小为 N/2 的已排序序列合并为一个 N 元素已排序序列仅需要 N 次操作。这个方法叫做合并。
 * @see [http://visualgo.net/sorting]
 */
public class MergeSorting {
    /**
     * 合并排序算法
     * 1)拆分阶段，将序列分为更小的序列
     * 2)排序阶段，把小的序列合在一起（使用合并算法）来构成更大的序列
     */
    public static int[] mergeSorting(int[] arr) {
        if (arr.length == 1) {
            return arr;
        }
        //split array
        int leftPartionSize = arr.length / 2;

        int[] leftPartion = new int[leftPartionSize];
        int[] rightPartion = new int[arr.length - leftPartionSize];
        for (int i = 0; i < leftPartion.length; i++) {
            leftPartion[i] = arr[i];
        }
        for (int i = 0; i < rightPartion.length; i++) {
            rightPartion[i] = arr[leftPartionSize + i];
        }

        //recursion sorting
        int[] leftPartion_sorted = mergeSorting(leftPartion);
        int[] rightPartion_sorted = mergeSorting(rightPartion);

        System.out.println("array---");
        PrintUtil.print(leftPartion_sorted);
        PrintUtil.print(rightPartion_sorted);

        int[] result = new int[leftPartion_sorted.length + rightPartion_sorted.length];
//        merging the 2 small ordered arrays into a big one
        System.out.println("merge---");
        int leftIndex = 0;
        int rightIndex = 0;

        for (int i = 0; i < result.length; i++) {
            if (leftIndex == leftPartion_sorted.length) {
                //result后面的都用rightPartion_sorted填充
                result[i] = rightPartion_sorted[rightIndex];
                rightIndex++;
            } else if (rightIndex == rightPartion_sorted.length) {
                //result后面的都用leftPartion_sorted填充
                result[i] = leftPartion_sorted[leftIndex];
                leftIndex++;
            } else {
                if (leftPartion_sorted[leftIndex] < rightPartion_sorted[rightIndex]) {
                    result[i] = leftPartion_sorted[leftIndex];
                    leftIndex++;
                } else {
                    result[i] = rightPartion_sorted[rightIndex];
                    rightIndex++;
                }
            }
        }
        PrintUtil.print("result---");
        PrintUtil.print(result);
        return result;
    }
}
