package me.demo.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.SortedMap;

/**
 * java.nio.Channel 分散与聚集
 */
public class TestChannel2 {
    /***
     * scatter_read_and_gather_write
     * @throws IOException
     */
    @Test
    void test_scatter_read_and_gather_write() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("d:/tmp/1.txt", "rw");
        //1.获取通道
        FileChannel channel1 = raf1.getChannel();
        //2,分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        //3.分散读取
        ByteBuffer[] buffers = {buf1, buf2};
        channel1.read(buffers);

        for (ByteBuffer byteBuffer : buffers) {
            byteBuffer.flip();
        }
        System.out.println(new String(buffers[0].array(), 0, buffers[0].limit()));
        System.out.println(new String(buffers[1].array(), 0, buffers[1].limit()));

        //4. 聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("d:/tmp/2.txt", "rw");
        FileChannel channel2 = raf2.getChannel();
        channel2.write(buffers);

        SortedMap<String, Charset> stringCharsetSortedMap = Charset.availableCharsets();
        for (Map.Entry s : stringCharsetSortedMap.entrySet()) {
            System.out.println(s.getKey() + "=" + s.getValue());
        }
    }

    /***
     * encoder and decoder
     * @throws IOException
     */
    @Test
    void test_encoder_decoder() throws IOException {
        Charset cs1 = Charset.forName("GBK");
        //1.获取编码器
        CharsetEncoder ce = cs1.newEncoder();
        //2.获取解码器
        CharsetDecoder cd = cs1.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("武汉加油！");
        charBuffer.flip();

        //3.encoder编码
        ByteBuffer eBuffer = ce.encode(charBuffer);
        for (int i = 0; i < 10; i++) {
            System.out.println(eBuffer.get());
        }
        //4.decoder解码
        eBuffer.flip();
        CharBuffer dBuffer = cd.decode(eBuffer);
        System.out.println(dBuffer.toString());

        //Charset直接解码
        Charset cs2 = Charset.forName("GBK");
        eBuffer.flip();
        CharBuffer decode = cs2.decode(eBuffer);
        System.out.println(decode.toString());
    }

    @Test
    void print_jdk_charsets() {
        //jdk字符集
        SortedMap<String, Charset> stringCharsetSortedMap = Charset.availableCharsets();
        for (Map.Entry s : stringCharsetSortedMap.entrySet()) {
            System.out.println(s.getKey() + "=" + s.getValue());
        }
    }
}
