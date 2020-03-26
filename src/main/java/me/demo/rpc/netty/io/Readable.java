package me.demo.rpc.netty.io;


import java.io.IOException;
import java.io.Serializable;

/**
 * 具备可读能力的接口
 *



 */
public interface Readable extends Serializable {
    /**
     * 从输入流中读取数据到接口中。适用读取Writable#writeToStream写入的数据
     *
     * @param stream 输入流
     * @throws IOException 如果发生IO错误
     */
    void readFromStream(DataRead stream) throws IOException;
}
