package me.demo.algorithm.str;

import org.junit.jupiter.api.Test;

/***
 编写一个函数来查找字符串数组中的最长公共前缀。

 如果不存在公共前缀，返回空字符串 ""。

 示例 1:

 输入: ["flower","flow","flight"]
 输出: "fl"
 示例 2:

 输入: ["dog","racecar","car"]
 输出: ""
 解释: 输入不存在公共前缀。
 说明:

 所有输入只包含小写字母 a-z 。

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/longest-common-prefix
 */
public class strPreffix14 {

    @Test
    public void test_ste() {
        String[] strs = new String[]{"f1", "flower", "flow", "flight"};
        System.out.println(longestCommonPrefix(strs));
    }

    /***
     * 最大匹配法
     * 时间复杂度：O(n)，n为所有字符串的长度之和
     * 1. 当字符串数组长度为 0 时则公共前缀为空，直接返回；
     * 2. 令最长公共前缀 pre 的值为第一个字符串，进行初始化；
     * 3. 遍历后面的字符串，依次将其与 pre 进行比较，两两找出公共前缀，最终结果即为最长公共前缀；
     * 4. 如果查找过程中出现了 pre 为空的情况，则公共前缀不存在直接返回；
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length < 1) {
            return "";
        }
        //设第一个字符串为前缀
        String pre = strs[0];
        //遍历其他字符串，如果存在不一致的字符，则更新cur的值（缩短）
        for (int i = 1; i < strs.length; i++) {
            int j = 0;
            for (; j < pre.length() && j < strs[j].length(); j++) {
                if (pre.charAt(j) != strs[j].charAt(j)) {
                    break;
                }
            }
            pre = pre.substring(0, j);
            if (pre.equals("")) {
                return "";
            }
        }
        return pre;
    }

    /***
     * 最小匹配法
     * 时间复杂度：O(n)，n为所有字符串的长度之和
     * 1. 当字符串数组长度为 0 时则公共前缀为空，直接返回；
     * 2. 定义当前字符串遍历指针p，遍历字符串数组，初始化p为0；
     * 3. 令第一个字符为最大前缀，逐个比较字符数组是否都包含该前缀，如果都包含，则指针p前移1位；
     * 4. 如果指针位置为0，则表示没找到匹配的;
     * 5. 根据指针位置返回最大前缀字符串;
     */
    public static String longestCommonPrefix2(String[] strs) {
        if (strs.length < 1) {
            return "";
        }
        //前缀字符指针
        int p = 0;
        //以第一个字符串作为基准
        while (p < strs[0].length()) {
            String cur = strs[0].substring(p, p + 1);

            //遍历数组，判断是否存在前缀字符
            int k = 0;
            for (String str : strs) {
                //越界处理
                if (p > str.length() - 1 || !cur.equals(str.substring(p, p + 1))) {
                    break;
                } else {
                    k++;
                }
            }
            //当前字符(cur)是否与每个字符串(strs)在当前的指定位置(p)都匹配
            if (k != strs.length) {
                break;
            }
            p++;
        }
        if (p == 0) {
            return "";
        }
        return strs[0].substring(0, p);
    }
}
