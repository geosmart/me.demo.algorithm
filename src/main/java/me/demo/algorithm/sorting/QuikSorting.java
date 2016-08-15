package me.demo.algorithm.sorting;

import me.demo.algorithm.util.PrintUtil;
import me.demo.algorithm.util.SortingUtil;

/**
 * 快速排序（Quicksort）又称划分交换排序（partition-exchange sort）
 *
 * @author geosmart
 * @complex 在平均状况下，排序n个项目要Ο(n logn)次比较。在最坏状况下则需要Ο(n2)次比较，但这种状况并不常见。 事实上，快速排序通常明显比其他Ο(n log
 * n)算法更快，因为它的内部循环（inner loop）可以在大部分的架构上很有效率地被实现出来。
 * @see [http://visualgo.net/sorting]
 *
 * //pivot索引，默认第一个 //根据pivot的值 进行交换拆分（比pivot小的放在左边，其他放在右边） //递归（取pivot>partion>swap） //合并拆分交换结果
 */
public class QuikSorting {
    int[] a;

    void quicksort(int left, int right) {
        PrintUtil.print(a);
        PrintUtil.print("left " + left + ":" + a[left] + " , right  " + (right) + ":" + a[right]);

        if (left >= right) {
            PrintUtil.print("left >= right,return...");
            return;
        }
        int i, j, pivot;
        //基准点
        pivot = a[left];
        i = left;
        j = right;
        PrintUtil.print("---\n");
        PrintUtil.print("pivot " + left + ":" + pivot);

        while (i != j) {
            //顺序很重要，要先从右边开始找
            while (a[j] >= pivot && i < j) {
                j--;
            }
            //再找左边的
            while (a[i] <= pivot && i < j) {
                i++;
            }
            //交换两个数在数组中的位置
            if (i < j) {
                PrintUtil.print("swap " + i + ":" + a[i] + "<->" + j + ":" + a[j]);
                SortingUtil.swap(a, i, j);
                PrintUtil.print(a);
            }
        }
        //最终将基准数归位
        a[left] = a[i];
        a[i] = pivot;

        //继续递归处理左边的
        quicksort(left, i - 1);
        //继续递归处理右边的
        quicksort(i + 1, right);
    }


    public void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        this.a = arr;
        quicksort(0, arr.length - 1);
    }
}
