package me.demo.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * NIO网络通信的3个核心
 * * Channel通道：负责连接；
 * * Buffer缓冲区：负责数据的存取；
 * * Selector选择器：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况；
 */
public class TestBlcokingNIO2 {
    private String host = "127.0.0.1";
    private int port = 9090;

    /***
     * 客户端
     * @throws IOException
     */
    @Test
    void client() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("d:/tmp/1.jpg"), StandardOpenOption.READ);

        //1.获取通道建立连接
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress(host, port));
        //2.分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //3.读取本地文件并发送到服务端
        while (inChannel.read(buf) != -1) {
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        //表示客户端发送完成
        sChannel.shutdownOutput();

        //接收服务端的反馈
        int len;
        while ((len = sChannel.read(buf)) != -1) {
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }
        //4.关闭通道
        sChannel.close();
        inChannel.close();
    }

    /***
     * 服务端
     * @throws IOException
     */
    @Test
    void server() throws IOException {
        Files.deleteIfExists(Paths.get("d:/tmp/2.jpg"));
        FileChannel outChannel = FileChannel.open(Paths.get("d:/tmp/2.jpg"), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.READ);

        //1.获取通道建立连接
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2.绑定连接端口号
        ssChannel.bind(new InetSocketAddress(port));

        //3.获取客户端连接的通道（阻塞等待）
        SocketChannel sChannel = ssChannel.accept();
        //4.分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //5.接收客户端数据，并保存到本地
        while (sChannel.read(buf) != -1) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        //发送反馈给客户端
        buf.put("武汉加油！".getBytes());
        buf.flip();
        sChannel.write(buf);

        //6.关闭通道
        outChannel.close();
        sChannel.close();
        ssChannel.close();
    }
}
