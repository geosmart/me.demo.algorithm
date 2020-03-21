package me.demo.nio;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * java.nio.ByteBuffer单元测试
 */
public class TestBuffer {

    @Test
    public void test_buffer() {
        //分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("buffer allocate 1024");
        //核心参数
        System.out.println(String.format("position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        //存储数据
        String src = "HelloNIO";
        buf.put(src.getBytes());
        System.out.println(String.format("put,position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        //读取数据
        buf.flip();
        System.out.println(String.format("flip,position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        byte[] dst = new byte[src.length()];
        buf.get(dst);
        System.out.println("get,src:" + new String(dst));
        System.out.println(String.format("get,position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        //重复读数据
        buf.rewind();
        System.out.println(String.format("rewind,position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        //清空缓冲区，实际但缓冲区中数据还在，只是指针重置了，数据处于‘被遗忘’状态
        buf.clear();
        System.out.println(String.format("clear,position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
    }

    @Test
    public void test_mark() {
        String src = "abcde";
        //分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("buffer allocate 1024");
        buf.put(src.getBytes());
        buf.flip();
        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        System.out.println(String.format("position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        System.out.println(new String(dst, 0, 2));
        //mark标记
        buf.mark();
        buf.get(dst, 2, 2);
        System.out.println(new String(dst, 2, 2));
        System.out.println(String.format("position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        //reset()：恢复到mark的位置
        buf.reset();
        System.out.println(String.format("position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        //remainning()：获取缓冲区中可操作的数据
        if (buf.hasRemaining()) {
            System.out.println(String.format("remain[%s]", buf.remaining()));
        }
    }

    @Test
    public void test_direct_buffer() {
        String src = "abcde";
        //分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        System.out.println("buffer allocate 1024");
        buf.put(src.getBytes());
        buf.flip();
        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        System.out.println(String.format("position[%s],limit[%s].capacity[%s]",
                buf.position(), buf.limit(), buf.capacity()));
        System.out.println(new String(dst, 0, 2));
    }
}
