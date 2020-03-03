package me.demo.juc;

/***
 * 类加载器示例
 */
public class ClassLoaderDemo {
    public static void main(String[] args) {
        Object obj = new Object();
        //null,即BootstrapClassLoader
        System.out.println(obj.getClass().getClassLoader());

        ClassLoaderDemo demo = new ClassLoaderDemo();
        //sun.misc.Launcher$AppClassLoader
        System.out.println(demo.getClass().getClassLoader());
        //sun.misc.Launcher$ExtClassLoader
        System.out.println(demo.getClass().getClassLoader().getParent());
        //null,即BootstrapClassLoader
        System.out.println(demo.getClass().getClassLoader().getParent().getClass().getClassLoader());
    }
}
