package me.demo.algorithm.algorithm.test;

import org.junit.jupiter.api.Test;

import me.demo.algorithm.recursion.Factorial;
import me.demo.algorithm.recursion.Fibonacci;
import me.demo.algorithm.util.PrintUtil;

/**
 * @author geosmart
 */
public class RecursionTest {

    @Test
    public void test_fibonacci() {
        String fibs = "";
        for (int i = 0; i < 41; i++) {
            int result = Fibonacci.fibonacci(i);
            fibs += result + ",";
        }
        PrintUtil.print(fibs);
    }

    @Test
    public void test_factorial() {
        int result = Factorial.factorial(4);
        PrintUtil.print(result);
    }
}
