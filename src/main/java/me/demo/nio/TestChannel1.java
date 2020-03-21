package me.demo.nio;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * java.nio.Channel文件复制的单元测试
 * 耗时排行
 * test_copy_file_by_path()：0.6s
 * test_copy_file_by_channel()：1.2s
 * test_copy_file_by_stream()：2.5s
 * test_copy_file_by_mappedByteBuffer():2.7s
 */
public class TestChannel1 {

    /**
     * 1.利用通道复制文件（非直接缓冲区）
     *
     * @throws IOException
     */
    @Test
    void test_copy_file_by_stream() throws IOException {
        FileInputStream fis = new FileInputStream("d:/tmp/1.flv");
        FileOutputStream fos = new FileOutputStream("d:/tmp/1_bak.flv");

        //1.获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        //2.分配缓冲区（缓冲区越大复制越快）
        ByteBuffer buf = ByteBuffer.allocateDirect(10240);

        //3.将inChannel中的数据写入Buffer
        while (inChannel.read(buf) != -1) {
            //切换缓冲区为读模式
            buf.flip();
            //4.将缓冲区数据写入outChannel
            outChannel.write(buf);
            //清空缓冲区
            buf.clear();
        }
        //关闭通道
        outChannel.close();
        inChannel.close();
        //关闭流
        fis.close();
        fos.close();
    }

    /**
     * 2.利用直接缓冲区复制文件（内存映射文件）
     *
     * @throws IOException
     */
    @Test
    void test_copy_file_by_mappedByteBuffer() throws IOException {
        //1.获取通道
        FileChannel inChannel = FileChannel.open(Paths.get("d:/tmp/1.flv"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("d:/tmp/2_bak.flv"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        //通道中获取数据，并读取到内存映射文件（直接缓冲区）
        MappedByteBuffer inMapByteBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapByteBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //对缓冲区进行数据的读取操作
        byte[] dst = new byte[inMapByteBuf.capacity()];
        inMapByteBuf.get(dst);
        outMapByteBuf.put(dst);

        //关闭通道
        outChannel.close();
        inChannel.close();
    }

    /**
     * 3.通道之间的数据传输（直接缓冲区）
     *
     * @throws IOException
     */
    @Test
    void test_copy_file_by_channel() throws IOException {
        //1.获取通道
        FileChannel inChannel = FileChannel.open(Paths.get("d:/tmp/1.flv"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("d:/tmp/3_bak.flv"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        inChannel.transferTo(0, inChannel.size(), outChannel);
        //outChannel.transferFrom(inChannel, 0, inChannel.size());

        //关闭通道
        outChannel.close();
        inChannel.close();
    }


    /**
     * 3.File.copy()（Path 到 Path，InputStream 到 Path 和 Path 到 OutputStream）
     *
     * @throws IOException
     */
    @Test
    void test_copy_file_by_path() throws IOException {
        Files.copy(Paths.get("d:/tmp/1.flv"), Paths.get("d:/tmp/4_bak.flv"), LinkOption.NOFOLLOW_LINKS);
    }
}
