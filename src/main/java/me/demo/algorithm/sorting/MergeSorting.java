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
        leftPartion = mergeSorting(leftPartion);
        rightPartion = mergeSorting(rightPartion);

        System.out.println("array---");
        PrintUtil.print(leftPartion);
        PrintUtil.print(rightPartion);

        //merging the 2 small ordered arrays into a big
        int[] mergePartion = new int[leftPartion.length + rightPartion.length];
        int leftIndex = 0;
        int rightIndex = 0;
        int mergeIndex = 0;
        while (mergeIndex < mergePartion.length) {
            if (leftIndex == leftPartion.length) {
                //后续都用rightPartion填充
                mergePartion[mergeIndex] = rightPartion[rightIndex];
                rightIndex++;
            } else if (rightIndex == rightPartion.length) {
                //后续都用leftPartion填充
                mergePartion[mergeIndex] = leftPartion[leftIndex];
                leftIndex++;
            } else {
                //每次取leftPartion和rightPartion中最小的copy到mergePartion中
                if (leftPartion[leftIndex] < rightPartion[rightIndex]) {
                    mergePartion[mergeIndex] = leftPartion[leftIndex];
                    leftIndex++;
                } else {
                    mergePartion[mergeIndex] = rightPartion[rightIndex];
                    rightIndex++;
                }
            }
            mergeIndex++;
        }
        System.out.println("merge---");
        PrintUtil.print(mergePartion);
        return mergePartion;
    }
}
