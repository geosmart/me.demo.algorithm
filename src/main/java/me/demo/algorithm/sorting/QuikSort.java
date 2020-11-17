package me.demo.algorithm.sorting;

import static me.demo.algorithm.util.SortingUtil.swap;

import me.demo.algorithm.util.PrintUtil;

/**
 * 快速排序（Quicksort）又称划分交换排序（partition-exchange sort）
 *
 * @author geosmart
 * @复杂度 在平均状况下，排序n个项目要Ο(n logn)次比较。在最坏状况下则需要Ο(n2)次比较，但这种状况并不常见。 事实上，快速排序通常明显比其他Ο(n log
 * n)算法更快，因为它的内部循环（inner loop）可以在大部分的架构上很有效率地被实现出来。
 * @1 设置pivot索引，默认第一个；
 * @2 根据pivot的值进行交换拆分（比pivot小的放在左边，其他放在右边）；
 * @3 递归（取pivot>partion>swap）
 * @4 合并拆分交换结果
 * @see [http://visualgo.net/sorting],[http://developer.51cto.com/art/201403/430986.htm]
 */
public class QuikSort {
    public static void main(String[] args) {
        int[] a = new int[]{8, 1, 3, 2, 7, 6, 4};
        quicksort(a, 0, a.length - 1);
        System.out.println(a);
    }

    /**
     * 快速排序-asc
     */
    public static void quicksort(int[] a, int left, int right) {
        if (left >= right) {
            PrintUtil.print("left >= right,return...");
            return;
        }
        PrintUtil.print(a);
        PrintUtil.print("left " + left + ":" + a[left] + " , right  " + (right) + ":" + a[right]);
        int i, j, pivot;
        //基准点
        pivot = a[left];
        //哨兵i
        i = left;
        //哨兵j
        j = right;
        PrintUtil.print("**************\n");
        PrintUtil.print("pivot " + left + ":" + pivot);

        //i==j,哨兵碰面时循环结束;
        // i，j索引值动态改变，需在多处约束i<j避免数组越界
        while (i != j) {
            //顺序很重要，要先从右边开始找：哨兵j当前值（a[j]）若大于pivot就继续往前走（j--），直到找到小于pivot的位置
            while (a[j] >= pivot && i < j) {
                j--;
            }
            //再找左边的：哨兵i当前值（a[i]）若小于pivot就继续往前走（i++），直到找到大于pivot的位置
            while (a[i] <= pivot && i < j) {
                i++;
            }
            //交换两个数在数组中的位置：哨兵i与哨兵j尚未碰面
            if (i < j) {
                PrintUtil.print("swap i(" + i + ":" + a[i] + ")<-> j(" + j + ":" + a[j] + ")");
                swap(a, i, j);
                PrintUtil.print(a);
            }
        }
        PrintUtil.print("swap pivot i(" + i + ":" + a[i] + ")<-> pivot(" + left + ":" + pivot + ")");
        //将基准数归位（即left小于基准，right大于基准）
        a[left] = a[i];
        a[i] = pivot;

        //递归处理左边的
        quicksort(a, left, i - 1);
        //递归处理右边的
        quicksort(a, i + 1, right);
    }

    /**
     * 快速排序-asc
     */
    public static void quicksort_meet(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }
        int i = left, j = right, pivot = a[left];
        while (i != j) {
            while (a[j] >= pivot && i < j) {
                j--;
            }
            while (a[i] <= pivot && i < j) {
                i++;
            }
            if (i < j) {
                swap(a, i, j);
            }
        }
        a[left] = a[i];
        a[i] = pivot;
        quicksort_meet(a, left, i - 1);
        quicksort_meet(a, i + 1, right);
    }

    /**
     * 快速排序-desc
     */
    public static void quicksort_desc(int[] a, int left, int right) {
        if (left >= right) {
            PrintUtil.print("left >= right,return...");
            return;
        }
        PrintUtil.print(a);
        PrintUtil.print("left " + left + ":" + a[left] + " , right  " + (right) + ":" + a[right]);
        int i, j, pivot;
        //基准点
        pivot = a[left];
        //哨兵i
        i = left;
        //哨兵j
        j = right;
        PrintUtil.print("**************\n");
        PrintUtil.print("pivot " + left + ":" + pivot);

        //i==j,哨兵碰面时循环结束;
        // i，j索引值动态改变，需在多处约束i<j避免数组越界
        while (i != j) {
            //顺序很重要，要先从右边开始找：哨兵j当前值（a[j]）若小等于pivot就继续往前走（j--），直到找到大于pivot的位置
            while (a[j] <= pivot && i < j) {
                j--;
            }
            //再找左边的：哨兵i当前值（a[i]）若大等于pivot就继续往前走（i++），直到找到小于pivot的位置
            while (a[i] >= pivot && i < j) {
                i++;
            }
            //交换两个数在数组中的位置：哨兵i与哨兵j尚未碰面
            if (i < j) {
                PrintUtil.print("swap i(" + i + ":" + a[i] + ")<-> j(" + j + ":" + a[j] + ")");
                swap(a, i, j);
                PrintUtil.print(a);
            }
        }
        PrintUtil.print("swap pivot i(" + i + ":" + a[i] + ")<-> pivot(" + left + ":" + pivot + ")");
        //将基准数归位（即left小于基准，right大于基准）
        a[left] = a[i];
        a[i] = pivot;

        //递归处理左边的
        quicksort_desc(a, left, i - 1);
        //递归处理右边的
        quicksort_desc(a, i + 1, right);
    }

    /**
     * 折半快排
     */
    public static void quicksort2(int[] a, int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = a[low + (high - low) / 2];
        PrintUtil.print("**************\n");
        PrintUtil.print(a);
        PrintUtil.print("left " + low + ":" + a[low] + " , right  " + (high) + ":" + a[high]);
        PrintUtil.print("pivot " + (low + (high - low) / 2) + ":" + pivot);

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller then the pivot element ,
            // then get the next element from the left list
            while (a[i] < pivot) {
                i++;
            }
            // If the current value from the right list is larger then the pivot
            // element then get the next element from the right list
            while (a[j] > pivot) {
                j--;
            }

            PrintUtil.print(" i(" + i + ":" + a[i] + ") ,  j(" + j + ":" + a[j] + ")");
            // If we have found a values in the left list which is larger then the pivot element ,
            // and if we have found a value in the right list which is smaller then the pivot element ,
            // then we exchange the values.
            // As we are done we can increase i and j
            if (i <= j) {
                if (i < j) {
                    PrintUtil.print("swap i(" + i + ":" + a[i] + ")<-> j(" + j + ":" + a[j] + ")");
                    swap(a, i, j);
                    PrintUtil.print(a);
                }
                i++;
                j--;
                PrintUtil.print(" i(" + i + ":" + a[i] + ") ,  j(" + j + ":" + a[j] + ")");
            }
        }
        // Recursion
        if (low < j) {
            quicksort2(a, low, j);
        }
        if (i < high) {
            quicksort2(a, i, high);
        }
    }

}
