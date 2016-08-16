package me.demo.algorithm.search;


import me.demo.algorithm.util.PrintUtil;

/**
 * 二分查找法/折半查找
 *
 * 1） 搜素过程从数组的中间元素开始，如果中间元素正好是要查找的元素，则搜素过程结束；
 * 2） 如果某一特定元素大于或者小于中间元素，则在数组大于或小于中间元素的那一半中查找，而且跟开始一样从中间元素开始比较。
 * 3） 如果在某一步骤数组为空，则代表找不到。
 * 这种搜索算法每一次比较都使搜索范围缩小一半。
 *
 * @优点 比较次数少，查找速度快，平均性能好；
 * @缺点 要求待查表为有序表(顺序存储结构)，且插入删除困难。如果有多个值相同的元素，只能返回找到的第一条
 * @适用 不经常变动而查找频繁的有序列表。
 *
 * Created by geosmart on 2016/8/16.
 */
public class BinarySearch {

    /**
     * 二分查找-循环实现
     *
     * @param list 输入数组
     * @param key  搜索目标
     * @param <T>  继承Comparable
     */
    public static <T extends Comparable<T>> int bsearch(T[] list, T key) {
        int low = 0;
        int high = list.length - 1;
        PrintUtil.print(list);
        PrintUtil.print("**************\n");
        while (low <= high) {
            PrintUtil.print("low->high: " + low + "->" + high);
            //无符号右移1位，相当于除2；空位补0
            int mid = (low + high) >>> 1;
            PrintUtil.print("mid a(" + mid + ")=" + list[mid]);
            int cmp = list[mid].compareTo(key);
            if (cmp < 0) {
                //midVal比key小，右边继续
                low = mid + 1;
            } else if (cmp > 0) {
                //midVal比key大，左边
                high = mid - 1;
            } else {
                //命中
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    /**
     * 二分查找-递归实现
     *
     * @param list 输入数组
     * @param low  范围最小索引
     * @param high 范围最大索引
     * @param key  搜索目标
     * @param <T>  继承Comparable
     */
    public static <T extends Comparable<T>> int bsearch(T[] list, int low, int high, T key) {
        rangeCheck(list.length, low, high);
        PrintUtil.print("**************\n");
        if (low <= high) {
            PrintUtil.print("low->high: " + low + "->" + high);
            //无符号右移1位，相当于除2；空位补0
            int mid = (low + high) >>> 1;
            PrintUtil.print("mid a(" + mid + ")=" + list[mid]);
            int cmp = list[mid].compareTo(key);
            if (cmp < 0) {
                //midVal比key小，右边继续
                low = mid + 1;
            } else if (cmp > 0) {
                //midVal比key大，左边继续
                high = mid - 1;
            } else {
                //命中
                return mid; // key found
            }
        }else{
            return -1;  // key not found
        }
        // key not found,recurse search
        return bsearch(list, low, high, key);
    }

    private static void rangeCheck(int length, int low, int high) {
        if (low < 0 || high < 0 || length == 0) {
            throw new IllegalArgumentException("params low & length must larger than 0 .");
        }
    }
}
