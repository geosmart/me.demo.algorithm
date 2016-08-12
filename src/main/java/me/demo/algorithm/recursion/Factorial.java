package me.demo.algorithm.recursion;

import me.demo.algorithm.util.PrintUtil;

/**
 * 阶乘计算
 * Created by geosmart on 2016/8/11.
 */
public class Factorial {

    public static int factorial(int num) {
        if (num == 1) {
            return 1;
        } else {
            PrintUtil.print(num + "*" + (num - 1));
            return num * factorial(num - 1);
        }
    }
}
