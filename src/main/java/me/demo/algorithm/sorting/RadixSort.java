package me.demo.algorithm.sorting;

/**
 * Radix Sort 基数排序
 *
 * @author geosmart
 * @定义 是一种非比较型整数排序算法，其原理是将整数按位数切割成不同的数字，然后按每个位数分别比较。
 * @实现 将所有待比较数值（正整数）统一为同样的数位长度，数位较短的数前面补零。然后，从最低位开始，依次进行一次排序。这样从最低位排序一直到最高位排序完成以后，数列就变成一个有序序列。
 * 基数排序的方式可以采用LSD（Least significant digital）或MSD（Most significant digital），LSD的排序方式由键值的最右边开始，而MSD则相反，由键值的最左边开始。
 * @复杂度 时间复杂度是O(k·n)，其中n是排序元素个数，k是数字位数。注意这不是说这个时间复杂度一定优于O(n·log(n))，k的大小取决于数字位的选择（比如比特位数），和待排序数据所属数据类型的全集的大小；k决定了进行多少轮处理，而n是每轮处理的操作数目。
 * @应用场景 由于整数也可以表达字符串（比如名字或日期）和特定格式的浮点数，所以基数排序也不是只能使用于整数。
 * @see [https://zh.wikipedia.org/wiki/%E5%9F%BA%E6%95%B0%E6%8E%92%E5%BA%8F]
 */
public class RadixSort {

    public static void radixSort(int[] a) {
        //求max
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            max = a[i] > max ? a[i] : max;
        }
        //求最大位数
        int bits = String.valueOf(max).length();

        //以LSD从低位到高位迭代排序，每次排序都是在上次排序的基础上进行
        for (int i = 1; i <= bits; i++) {
            int[] bitArray = new int[a.length];
            for (int j = 0; j < a.length; j++) {
                bitArray[j] = getBitVal(a[j], i);
            }
            //对特定位数值进行计数排序
            CountingSort.countingSort(bitArray);

            int index = 0;
            int[] result = new int[a.length];
            //根据特定位上已排序值，还原数组
            for (int j = 0; j < bitArray.length; j++) {
                //在原数组中找到第一个 位数为bitArray[j]的
                for (int k = 0; k < a.length; k++) {
                    //-1表示已参与过比较
                    if (a[k] != -1) {
                        int bitVal = getBitVal(a[k], i);
                        if (bitVal == bitArray[j]) {
                            result[index] = a[k];
                            index++;
                            //将已还原的置为-1
                            a[k] = -1;
                            break;
                        }
                    }
                }
            }
            //将当前位上排序结果复制到源数组
            for (int j = 0; j < result.length; j++) {
                a[j] = result[j];
            }
        }
    }

    /**
     * 获取正整数第i位的值
     *
     * @param num 正整数
     * @param i   第几位
     */
    private static int getBitVal(int num, int i) {
        double remain = num / Double.valueOf(Math.pow(10, i));
        if (remain > 0) {
            return Integer.valueOf((String.valueOf(remain).split("\\.")[1]).substring(0, 1));
        } else {
            return 0;
        }
    }
}
