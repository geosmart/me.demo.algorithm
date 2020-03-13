package me.demo.juc;

public class VolatileTest {
    private VolatileTest() {
    }

    //延迟初始化实现:内部静态类只有在调用时才会加载
    private static final class InstanceHolder {
        private InstanceHolder() {
        }
        //JVM在类的初始化阶段给出的线程安全性保证
        static final VolatileTest instance = new VolatileTest();
    }
    //公有实例化方法
    public static VolatileTest getInstance1() {
        return InstanceHolder.instance;
    }

    //volatile标识可见性
    private static volatile VolatileTest instance = null;

    //DLC双重检查加锁实现
    public static VolatileTest getInstance2() {
        //其他线程可能更改instance
        if (instance == null) {
            synchronized (VolatileTest.class) {
                //其他线程可能在上锁前更改instance
                if (instance == null) {
                    instance = new VolatileTest();
                    //此时如果instance没有设置volatile保证有序性，其他线程可能读取到一个部分构造的对象
                }
            }
        }
        return instance;
    }

    private volatile int num = 99;

    public void test() {
        num = num + 100;
        System.out.println(num);
    }

}