package me.demo.nio;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import jodd.util.RandomStringUtil;

/**
 * NIO网络通信的3个核心-TCP协议
 * * Channel通道：负责连接；
 * * Buffer缓冲区：负责数据的存取；
 * * Selector选择器：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况；
 */
public class TestNonBlcokingNIO {
    private String host = "127.0.0.1";
    private int port = 9090;

    /**
     * 客户端
     *
     * @throws IOException
     */
    @RepeatedTest(100)
    void client() throws IOException {
        //1.获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress(host, port));
        //2.切换非阻塞模式
        sChannel.configureBlocking(false);
        //3. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //4.发送数据给服务端
        byte[] str = (new Date().toString() + ",nina say:" + RandomStringUtil.randomAlpha(10) + "\n").getBytes();
        buf.put(str);
        buf.flip();
        sChannel.write(buf);
        buf.clear();

        //5.关闭通道
        sChannel.close();
    }

    /**
     * 服务端
     *
     * @throws IOException
     */
    @Test
    void server() throws IOException {
        //1.获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2.切换非阻塞模式
        ssChannel.configureBlocking(false);
        //3.绑定连接
        ssChannel.bind(new InetSocketAddress(host, port));
        //4.获取选择器
        Selector selector = Selector.open();
        //5.将通道注册到选择器上,并指定监听事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);


        //6.轮询式的获取选择器上已经准备就绪的事件
        while (selector.select() > 0) {
            //7.获取当前选择器中所有注册的选择键（已就绪的监听事件）
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //8.获取准备就绪的事件
                if (selectionKey.isAcceptable()) {
                    //9.若接收就绪，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();
                    //10.切换非阻塞模式
                    sChannel.configureBlocking(false);
                    //11.将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    //获取当前选择器上读就绪状态的通道
                    SocketChannel schannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = schannel.read(buf)) != -1) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                    schannel.close();
                }
                //取消选择键SelectionKey
                iterator.remove();
            }
        }
    }
}
