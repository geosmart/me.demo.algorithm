package me.demo.algorithm.recursion;

import me.demo.algorithm.util.PrintUtil;

/**
 * 斐波纳契数列以递归的方法定义：
 * F（0）=0，
 * F（1）=1，
 * F（n）=F(n-1)+F(n-2)（n≥2，n∈N*）
 * Created by geosmart on 2016/8/11.
 */
public class Fibonacci {

    public static int fibonacci(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
//            PrintUtil.print((n-1) + "+" + (n - 2));
            return fibonacci(n-1)+fibonacci(n-2);
        }
    }
}
