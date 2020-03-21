package me.demo.nio;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import jodd.util.RandomStringUtil;

/**
 * NIO网络通信的3个核心-UDP协议
 * * Channel通道：负责连接；
 * * Buffer缓冲区：负责数据的存取；
 * * Selector选择器：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况；
 */
public class TestNonBlcokingNIO2 {
    private String host = "127.0.0.1";
    private int port = 9090;

    /**
     * 客户端
     *
     * @throws IOException
     */
    @RepeatedTest(10)
    void send() throws IOException {
        //1.获取通道
        DatagramChannel dc = DatagramChannel.open();
        //2.切换非阻塞模式
        dc.configureBlocking(false);

        //3. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //4.发送数据给服务端
        byte[] str = (new Date().toString() + ",nina say:" + RandomStringUtil.randomAlpha(10) + "\n").getBytes();
        buf.put(str);
        buf.flip();
        dc.send(buf, new InetSocketAddress(host, port));
        buf.clear();

        dc.close();
    }

    /**
     * 服务端
     *
     * @throws IOException
     */
    @Test
    void recieve() throws IOException {
        //1.获取通道
        DatagramChannel dc = DatagramChannel.open();
        //2.切换非阻塞模式
        dc.configureBlocking(false);
        //3.绑定连接
        dc.bind(new InetSocketAddress(host, port));
        //4.获取选择器
        Selector selector = Selector.open();
        //5.将通道注册到选择器上,并指定监听事件
        dc.register(selector, SelectionKey.OP_READ);

        //6.轮询式的获取选择器上已经准备就绪的事件
        while (selector.select() > 0) {
            //7.获取当前选择器中所有注册的选择键（已就绪的监听事件）
            Set<SelectionKey> sk = selector.selectedKeys();
            Iterator<SelectionKey> iterator = sk.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //8.获取准备就绪的事件
                if (selectionKey.isReadable()) {
                    //获取当前选择器上读就绪状态的通道
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    dc.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    iterator.remove();
                }
            }
        }
    }
}
