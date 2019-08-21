package me.demo.algorithm.sorting;

import java.util.ArrayList;
import java.util.List;

import me.demo.algorithm.util.PrintUtil;
import me.demo.algorithm.util.SortingUtil;

/**
 * Bucket Sort 桶排序/箱排序，桶排序是基数排序的一种归纳结果
 * 实现步骤[https://zh.wikipedia.org/wiki/桶排序]
 * 1）设置一个定量的数组当作空桶子。
 * 2）寻访序列，并且把项目一个一个放到对应的桶子去。
 * 3）对每个不是空的桶子进行排序。
 * 4）从不是空的桶子里把项目再放回原来的序列中
 *
 * @author geosmart
 * @定义 桶排序（Bucket sort）或所谓的箱排序，是一个排序算法，工作的原理是将数组分到有限数量的桶里。每个桶再个别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排序）。，最后将各个桶中的数据有序的合并起来。
 * @复杂度 当要被排序的数组内的数值是均匀分配的时候，桶排序使用线性时间（Θ(n)）。但桶排序并不是比较排序，他不受到O(n log n)下限的影响。
 * @算法思想 待排序数组A[1...n]内的元素是随机分布在[0, 1)区间内的的浮点数.辅助排序数组B[0....n-1]的每一个元素都连接一个链表.将A内每个元素乘以N(数组规模)取底,并以此为索引插入(插入排序)数组B的对应位置的连表中.
 * 最后将所有的链表依次连接起来就是排序结果.
 * @可视化 https://www.cs.usfca.edu/~galles/visualization/BucketSort.html
 */
public class BucketSort {

    public static void bucketSort(int[] a) {
        //求最大值，最小值
        //get max and min
        int max = a[0], min = a[0];
        for (int i = 1; i < a.length; i++) {
            max = (a[i] > max) ? a[i] : max;
            min = (a[i] < min) ? a[i] : min;
        }
        PrintUtil.print("range ( " + min + " , " + max + " )");
        //TODO 桶数
//        int bucketCount = Double.valueOf(Math.ceil((max - min) / step)).intValue();
        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            buckets.add(i, new ArrayList<Integer>());
        }

        //分桶
        for (int i = 0; i < a.length; i++) {
            int index = a[i] * a.length / (max + 1);
            buckets.get(index).add(a[i]);
        }

        //各桶中进行插入排序
        for (int i = 0; i < buckets.size(); i++) {
            List<Integer> numbers = buckets.get(i);

            if (numbers.size() > 0) {
                //插入排序
                int[] bucketNumbers = SortingUtil.convertListToArray(numbers);
                PrintUtil.print(bucketNumbers);
                InsertSort.insertSorting(bucketNumbers);

                //更新插入排序结果到桶中
                buckets.set(i, SortingUtil.convertArrayToList(bucketNumbers));
            }
        }

        //将桶中已排序数据有序倒到原数组
        int index = 0;
        for (List<Integer> numbers : buckets) {
            for (Integer number : numbers) {
                a[index] = number;
                index++;
            }
        }
    }
}
