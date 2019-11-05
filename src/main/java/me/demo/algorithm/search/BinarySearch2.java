package me.demo.algorithm.search;

public class BinarySearch2 {

    public static void main(String[] args) {
        int a[] = new int[]{1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int[] search = new int[]{0, 1, 2, 7, 11};
        for (int s : search) {
            System.out.println(String.format("begin Rsearch[%s]", s));
            int idx = bsearch_recurse(a, 0, a.length - 1, s);
            System.out.println(String.format("end Rsearch[%s],index[%s]", s, idx));
        }

        for (int s : search) {
            System.out.println(String.format("begin Lsearch[%s]", s));
            int idx = bsearch_while(a, s);
            System.out.println(String.format("end Lsearch[%s],index[%s]", s, idx));
        }
    }

    /***
     * 递归实现二分查找
     * @param a 目标数组
     * @param from 开始索引
     * @param to 结果索引
     * @param tar 查找目标
     * @return 目标索引，查不到返回-1
     */
    public static int bsearch_recurse(int[] a, int from, int to, int tar) {
        System.out.println(String.format("search from[%s],to[%s]", from, to));
        if (from > to || tar < a[from] || tar > a[to]) {
            return -1;
        } else if (tar == a[from]) {
            return from;
        } else if (tar == a[to]) {
            return to;
        } else {
            int m = (to + from) / 2;
            if (tar > a[m]) {
                return bsearch_recurse(a, m, to, tar);
            } else if (tar < a[m]) {
                return bsearch_recurse(a, from, m, tar);
            } else {
                return m;
            }
        }
    }


    /***
     *循环实现二分查找
     * @param a 目标数组
     * @param tar 查找目标
     * @return 目标索引，查不到返回-1
     */
    public static int bsearch_while(int[] a, int tar) {
        int from = 0;
        int to = a.length - 1;
        if (to < 0 || tar < a[from] || tar > a[to]) {
            return -1;
        } else if (tar == a[from]) {
            return from;
        } else if (tar == a[to]) {
            return to;
        } else {
            int m = 0;
            while (from <= to) {
                m = (to + from) / 2;
                System.out.println(String.format("search from[%s],to[%s]", from, to));
                if (tar < a[m]) {
                    to = m - 1;
                } else if (tar > a[m]) {
                    from = m + 1;
                } else {
                    return m;
                }
            }
        }
        return -1;
    }
}
