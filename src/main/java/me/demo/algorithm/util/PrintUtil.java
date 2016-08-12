package me.demo.algorithm.util;


/**
 * @author geosmart
 */
public class PrintUtil {

    public static void print(int[] list) {
        String str = "";
        for (int i : list) {
            str += i + "  ";
        }
        System.out.println(str);
        System.out.println("---");
    }

    public static void print(Object  num) {
        System.out.println(num);
    }
}
