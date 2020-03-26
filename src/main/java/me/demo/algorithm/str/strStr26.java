package me.demo.algorithm.str;

import org.junit.jupiter.api.Test;

/***
 * 实现 strStr() 函数。
 *
 * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
 *
 * 示例 1:
 *
 * 输入: haystack = "hello", needle = "ll"
 * 输出: 2
 * 示例 2:
 *
 * 输入: haystack = "aaaaa", needle = "bba"
 * 输出: -1
 * 说明:
 *
 * 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
 *
 * 对于本题而言，当 needle 是空字符串时我们应当返回 0 。这与C语言的 strstr() 以及 Java的 indexOf() 定义相符。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/implement-strstr
 */
public class strStr26 {

    @Test
    public void test_ste() {
        String a = "mississippi";
        String b = "issippi";
        System.out.println(strStr(a, b));
    }

    public static int strStr(String haystack, String needle) {
        if (needle.isEmpty()) {
            return 0;
        }
        char[] a = haystack.toCharArray();
        char[] b = needle.toCharArray();
        int k = 0;
        for (int i = 0; i < a.length; i++) {
            //找到第一个字符一致
            if (a[i] == b[k]) {
                k = i + 1;
                //遍历第二个字符串比较
                for (int j = 1; j < b.length; j++) {
                    //防止a溢出还没找到b
                    if (k == a.length || b[j] != a[k]) {
                        break;
                    }
                    k++;
                }
                //已找到
                if (k - b.length == i) {
                    return i;
                } else {
                    //未找到，重置b指针
                    k = 0;
                }
            }
        }
        return -1;
    }
}
