package me.demo.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * NIO网络通信的3个核心
 * * Channel通道：负责连接；
 * * Buffer缓冲区：负责数据的存取；
 * * Selector选择器：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况；
 * Java NIO 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。
 * 数据会被写到sink通道，从source通道读取
 */
public class TestPipe {

    @Test
    void test_pipe() throws IOException {
        //1.获取管道
        Pipe pipe = Pipe.open();

        //2.将换出去中的数据写入管道
        ByteBuffer buf = ByteBuffer.allocate(1024);
        Pipe.SinkChannel sinkChannel = pipe.sink();
        buf.put("send data to pipe".getBytes());
        buf.flip();
        sinkChannel.write(buf);

        //3.读取管道中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        buf.flip();
        sourceChannel.read(buf);
        System.out.println(new String(buf.array(), 0, buf.limit()));

        //4.关闭管道
        sinkChannel.close();
        sourceChannel.close();
    }
}
