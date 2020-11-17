package me.demo.algorithm.search;


/**
 * 回文检测
 */
public class Palindrome {
    public static void main(String[] args) {
        String str = "A man, a plan, a canal: Panama";
        System.out.println(isPalindrome(str));
        str = "race a car";
        System.out.println(isPalindrome(str));
    }

    /***
     * 双指针解法
     */
    private static boolean isPalindrome(String s) {
        char[] chars = s.toLowerCase().toCharArray();
        int p1 = 0;
        int p2 = chars.length - 1;
        while (p1 < p2) {
            System.out.println(String.format("%s[%s],%s[%s]", p1, chars[p1], p2, chars[p2]));
            if (!Character.isLetterOrDigit(chars[p1])) {
                p1++;
                continue;
            }
            if (!Character.isLetterOrDigit(chars[p2])) {
                p2--;
                continue;
            }
            if (chars[p1] != chars[p2]) {
                return false;
            } else {
                p1++;
                p2--;
            }
        }
        return true;
    }
}
