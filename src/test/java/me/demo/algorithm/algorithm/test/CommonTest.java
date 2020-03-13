package me.demo.algorithm.algorithm.test;

import org.junit.Test;

/**
 * Created by Think on 2016/8/16.
 */
public class CommonTest {

    @Test
    public void test_bit_operator() {
//        System.out.println(10 >> 1);
//        System.out.println(10 << 1);
//        System.out.println(10 >>> 1);
        int[] a = new int[]{3, 7};
        for (int i = 0; i < a.length; i++) {
            System.out.println(i + "-->" + a[i]);
        }
        for (int i = 0; i < a.length; ++i) {
            System.out.println(i + "-->" + a[i]);
        }
    }

    @Test
    public void test_bit_operator2() {
        int hashCode = "CAFEBABE".hashCode();
        System.out.println(hashCode);
        System.out.println(hashCode >>> 16);
        System.out.println(hashCode ^ (hashCode >>> 16));

        System.out.println(Integer.toBinaryString(hashCode));
        System.out.println("0000000000000000" + Integer.toBinaryString(hashCode >>> 16));
        //hashCode的低位与高位异或运算
        System.out.println(Integer.toBinaryString(hashCode ^ hashCode >> 16));

        hashCode = hashCode ^ hashCode >> 16;
        System.out.println((16 - 1) & hashCode);
    }
}
