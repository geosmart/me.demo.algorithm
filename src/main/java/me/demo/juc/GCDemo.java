package me.demo.juc;

import java.util.UUID;

/***
 * GC回收器演示
 * 默认参数：
 * -XX:InitialHeapSize=527399232 -XX:MaxHeapSize=8438387712 -XX:+UseParallelGC
 * -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation
 * 年轻代
 * 串行GC：-XX:+UseSerialGC
 * 并行GC：-XX:+UseParNewGC
 * 并行回收GC：-XX:+UseParallelGC
 * 老年代
 * 串行GC：-XX:+UseSerialGC
 * 并行回收GC（老年代）：-XX:+UseParallelOldGC
 * 并行标记清除：-XX:+UseConcMarkSweepGC
 * GC参数打印： -XX:+PrintCommandLineFlags -XX:+PrintGCDetails
 *
 */
public class GCDemo {
    public static void main(String[] args) {
        String str = "";
        while (true) {
            str += str + UUID.randomUUID().toString();
            str.intern();
        }
    }

}
