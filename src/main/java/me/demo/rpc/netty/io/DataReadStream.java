package me.demo.rpc.netty.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据读取流，实现DataRead接口
 *


 * @see DataRead

 */
public class DataReadStream extends AbstractDataRead {
    InputStream inputStream;
    private volatile int readBytes;

    /**
     * 构建DataReadStream
     *
     * @param inputStream 输入流
     * @param timeout     读数据超时，以毫秒为单位
     */
    public DataReadStream(InputStream inputStream, int timeout) {
        super(timeout);
        this.inputStream = inputStream;
    }

    public DataReadStream(byte[] buf, int timeout) {
        super(timeout);
        this.inputStream = new ByteArrayInputStream(buf);
    }

    public DataReadStream(byte[] buf, int offset, int len, int timeout) {
        super(timeout);
        this.inputStream = new ByteArrayInputStream(buf, offset, len);
    }

    /**
     * 获取输入流
     *
     * @return 输入流
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 从所包含的输入流中将 len 个字节读入一个字节数组中。尽量读取 len
     * 个字节，但可能读取较少的字节数，该字节数也可能为零。以整数形式返回实际读取的字节数。
     * 在输入数据可用、检测到文件末尾或抛出异常之前，此方法将阻塞至读取超时。 <br>
     * 如果 b 为 null，则抛出 NullPointerException。 <br>
     * 如果 off 为负，或 len 为负，抑或 off+len 大于数组 b 的长度，则抛出 IndexOutOfBoundsException。 <br>
     * 如果 len 为零，则不读取字节并返回 0；否则至少试图读取一个字节。如果因为该流在文件未尾而无字节可用，则返回 -1
     * 值；否则至少读取一个字节并将其存储到 b 中。 <br>
     * 将读取的第一个字节存储到元素 b[off] 中，将下一个字节存储到 b[off+1] 中，依此类推。读取的字节数至多等于 len。设 k
     * 为实际读取的字节数；这些字节将存储在 b[off] 到 b[off+k-1] 的元素中，b[off+k] 到 b[off+len-1]
     * 的元素不受影响。 <br>
     * 在所有的情况下，b[0] 到 b[off] 的元素和 b[off+len] 到 b[b.length-1] 的元素都不受影响。 <br>
     * 如果因为文件末尾以外的其他原因而无法读取第一个字节，则抛出 IOException。尤其在输入流已关闭的情况下，将抛出 IOException。
     *
     * @param b       存储读取数据的缓冲区
     * @param offset  读取的数据存储的起始偏移量
     * @param len     读取的最大字节数
     * @param timeout 指定的超时时间
     * @return 读入缓冲区的字节总数，如果因为已经到达流的末尾而没有更多的数据，则返回 -1。
     * @throws IOException      如果发生IO错误
     */
    @Override
    protected int read(byte[] b, int offset, int len, int timeout) throws IOException {
        if (b == null)
            throw new NullPointerException();
        if (inputStream == null)
            throw new IOException("尚未指定具体的输入流对象，不允许读数据!");
        int r = inputStream.read(b, offset, len);
        if (r >= 0)
            readBytes += r;
        return r;
    }

    @Override
    public int readBytes() {
        return readBytes;
    }

}
