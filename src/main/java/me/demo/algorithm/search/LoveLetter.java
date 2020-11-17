package me.demo.algorithm.search;


import java.util.HashMap;
import java.util.Map;

/**
 * 用报纸写情书
 */
public class LoveLetter {
    public static void main(String[] args) {
        String letter = "今天天气真好";
        String paper = "今天没有新闻";
        System.out.println(solve(letter, paper));

        letter = "今天天气真好";
        paper = "今天真好，没有坏天气";
        System.out.println(solve(letter, paper));
    }

    private static boolean solve(String letter, String paper) {
        if (paper.length() < letter.length()) {
            return false;
        }
        Map<Character, Integer> paperMap = new HashMap<>();
        for (char c : paper.toCharArray()) {
            paperMap.put(c, paperMap.getOrDefault(c, 0) + 1);
        }
        for (char c : letter.toCharArray()) {
            Integer cnt = paperMap.getOrDefault(c, 0);
            if (cnt <= 0) {
                return false;
            } else {
                paperMap.put(c, cnt - 1);
            }
        }
        return true;
    }
}
