package me.demo.algorithm.heap;

import java.util.Arrays;

/**
 * 二叉堆打印
 */
public class HeapPrinter {

    public static void dump(Object[] heapArray) {
        System.out.println(stringOfSize(64, '-'));
        Object[] heap = new Object[heapArray.length + 1];
        System.arraycopy(heapArray, 0, heap, 1, heapArray.length);
        int height = log2(heap.length) + 1;
        for (int i = 1, len = heap.length; i < len; i++) {
            Object x = heap[i];
            int level = log2(i) + 1;
            int spaces = (height - level) * 4;
            //FIXME tab calc error
            if (i == len - 1) {
                spaces = (height - level + 1) * 4;
            }

            System.out.print(stringOfSize(spaces, ' '));
            System.out.print(x);

            if ((int) Math.pow(2, level) - 1 == i) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println(stringOfSize(64, '-'));
    }

    private static String stringOfSize(int size, char ch) {
        char[] a = new char[size];
        Arrays.fill(a, ch);
        return new String(a);
    }

    // log with base 2
    private static int log2(int x) {
        // = log(x) with base 10 / log(2) with base 10
        return (int) (Math.log(x) / Math.log(2));
    }

}
