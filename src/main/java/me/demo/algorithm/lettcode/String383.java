package me.demo.algorithm.lettcode;

import java.util.HashMap;
import java.util.Map;

/***
 给定一个赎金信 (ransom) 字符串和一个杂志(magazine)字符串，判断第一个字符串ransom能不能由第二个字符串magazines里面的字符构成。如果可以构成，返回 true ；否则返回 false。

 (题目说明：为了不暴露赎金信字迹，要从杂志上搜索各个需要的字母，组成单词来表达意思。)

 注意：

 你可以假设两个字符串均只含有小写字母。

 canConstruct("a", "b") -> false
 canConstruct("aa", "ab") -> false
 canConstruct("aa", "aab") -> true

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/ransom-note
 */
class String383 {

    public static void main(String[] args) {
        String letter = "给钱放人";
        String paper = "他放话说：没有人比我有钱！我是不是很给力？";
        boolean res = canConstruct(letter, paper);
        System.out.println(res);
    }

    public static boolean canConstruct(String letter, String paper) {
        if (paper.length() < letter.length()) {
            return false;
        }
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char s : paper.toCharArray()) {
            map.put(s, map.getOrDefault(s, 0) + 1);
        }
        for (char l : letter.toCharArray()) {
            int val = map.getOrDefault(l, 0);
            if (val == 0) {
                return false;
            } else {
                map.put(l, val - 1);
            }
        }
        return true;
    }
}