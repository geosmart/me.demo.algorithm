package me.demo.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * NIO网络通信的3个核心
 * * Channel通道：负责连接；
 * * Buffer缓冲区：负责数据的存取；
 * * Selector选择器：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况；
 */
public class TestBlcokingNIO1 {

    /***
     * 客户端
     * @throws IOException
     */
    @Test
    void client() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("d:/tmp/1.jpg"), StandardOpenOption.READ);

        //1.获取通道建立连接
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9090));
        //2.分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //3.读取本地文件并发送到服务端
        while (inChannel.read(buf) != -1) {
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
        }
        //4.关闭通道
        socketChannel.close();
        inChannel.close();
    }

    /***
     * 服务端-阻塞等待请求
     * @throws IOException
     */
    @Test
    void server() throws IOException {
        FileChannel outChannel = FileChannel.open(Paths.get("d:/tmp/2.jpg"), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.READ);

        //1.获取通道建立连接
        ServerSocketChannel ssocketChannel = ServerSocketChannel.open();
        //2.绑定连接端口号
        ssocketChannel.bind(new InetSocketAddress(9090));

        //3.获取客户端连接的通道
        SocketChannel sChannel = ssocketChannel.accept();
        //4.分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //5.接收客户端数据，并保存到本地
        while (sChannel.read(buf) != -1) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        //6.关闭通道
        outChannel.close();
        sChannel.close();
    }
}
