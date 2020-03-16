package me.demo.algorithm.lettcode;

import java.util.Arrays;

/***
 给定两个排序后的数组 A 和 B，其中 A 的末端有足够的缓冲空间容纳 B。 编写一个方法，将 B 合并入 A 并排序。

 初始化 A 和 B 的元素数量分别为 m 和 n。

 示例:

 输入:
 A = [1,2,3,0,0,0], m = 3
 B = [2,5,6],       n = 3

 输出: [1,2,2,3,5,6]
 说明:
 A.length == n + m

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/sorted-merge-lcci
 */
class ArraySortMerge {

    public static void main(String[] args) {
        int[] A = new int[]{1, 2, 3, 0, 0, 0};
        int[] B = new int[]{2, 5, 6};
        merge_v2(A, 3, B, 3);


//        int[] A = new int[]{2, 0};
//        int[] B = new int[]{1};
//        merge_v2(A, 1, B, 1);

        System.out.println(Arrays.toString(A));
    }

    /**
     * JDK 快排实现
     */
    public static void merge_v0(int[] A, int m, int[] B, int n) {
        System.arraycopy(B, 0, A, m, n);
        Arrays.sort(A);
    }

    /***
     * 逆向双指针法
     * 时间复杂度：O(m+n)
     * 空间复杂度：O(1)
     */
    public static void merge_v2(int[] A, int m, int[] B, int n) {
        if (m == 0) {
            for (int i = 0; i < n; i++) {
                A[i] = B[i];
            }
            return;
        } else if (n == 0) {
            return;
        }
        int i = m + n - 1, pa = m - 1, pb = n - 1;

        while (pa >= 0 || pb >= 0) {
            if (pa == -1) {
                //剩余的元素逐个反向插入
                A[i] = B[pb];
                pb--;
                //System.arraycopy(B, 0, A, 0, pb + 1);
                //break;
            } else if (pb == -1) {
                A[i] = A[pa];
                pa--;
            } else if (A[pa] > B[pb]) {
                A[i] = A[pa];
                pa--;
            } else {
                A[i] = B[pb];
                pb--;
            }
            i--;
        }
    }

    /**
     * 双指针法
     * 时间复杂度：O(m+n)
     * 空间复杂度：O(m+n)
     * 问题：未能利用A的内存空间
     */
    public static void merge_v1(int[] A, int m, int[] B, int n) {
        if (m == 0) {
            for (int k = 0; k < n; k++) {
                A[k] = B[k];
            }
            return;
        } else if (n == 0) {
            return;
        }

        int[] sort = new int[A.length];
        int i = 0, pa = 0, pb = 0;

        while (pa < m || pb < n) {
            if (pa == m) {
                //剩余的元素逐个插入
                sort[i] = B[pb];
                pb++;
            } else if (pb == n) {
                sort[i] = A[pa];
                pa++;
            } else if (A[pa] < B[pb]) {
                sort[i] = A[pa];
                pa++;
            } else {
                sort[i] = B[pb];
                pb++;
            }
            i++;
        }
        for (int k = 0; k < m + n; k++) {
            A[k] = sort[k];
        }
    }
}