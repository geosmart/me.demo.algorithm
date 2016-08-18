package me.demo.algorithm.sorting;

import me.demo.algorithm.util.PrintUtil;

/**
 * Counting Sort 计数排序
 *
 * 1)找出待排序的数组中最大和最小的元素
 * 2)统计数组中每个值为t的元素出现的次数，存入数组C的第t项
 * 3)对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）
 * 4)反向填充目标数组：将每个元素t放在新数组的第C(t)项，每放一个元素就将C(t)减去1
 *
 * @author geosmart
 * @complex O(n), 当输入的元素是 n 个 0 到 k 之间的整数时，它的运行时间是 Θ(n + k)。
 * @优点 稳定, 因为其待排序元素本身就含有了定位特征，因而不需要比较就可以确定其前后位置,不适合按字母顺序排序人名。
 * @缺点 计数排序对于数据范围很大的数组，需要大量时间和内存，适用性不高。例如：计数排序是用来排序0到100之间的数字的最好的算法
 * @应用场景 投票时记票
 * @see [http://www.cnblogs.com/ttltry-air/archive/2012/08/04/2623302.html]
 */
public class CountingSort {

    public static void countingSort(int[] a) {
        //get max and min
        int max = a[0], min = a[0];
        for (int i = 1; i < a.length; i++) {
            max = (a[i] > max) ? a[i] : max;
            min = (a[i] < min) ? a[i] : min;
        }
        PrintUtil.print("range ( " + min + " , " + max + " )");

        //couting
        int[] c = new int[max - min + 1];
        for (int i = 0; i < a.length; i++) {
            if (a[i] == max) {
                c[c.length - 1] = c[c.length - 1] + 1;
            } else if (a[i] == min) {
                c[0] = c[0] + 1;
            } else {
                c[a[i] - min] = c[a[i] - min] + 1;
            }
        }

        //restore array
        int cIndex = 0;
        for (int i = 0; i < a.length; i++) {
            while (c[cIndex] == 0) {
                cIndex++;
            }
            a[i] = min + cIndex;
            c[cIndex] = c[cIndex] - 1;
        }
    }
}
