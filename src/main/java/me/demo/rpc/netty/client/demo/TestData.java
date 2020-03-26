package me.demo.rpc.netty.client.demo;

import java.io.IOException;

import me.demo.rpc.netty.io.DataRead;
import me.demo.rpc.netty.io.DataWrite;
import me.demo.rpc.netty.io.ReadWritable;

public class TestData implements ReadWritable {
    private static final long serialVersionUID = 1L;
    String data;

    public TestData() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TestData [data=" + data + "]";
    }


    @Override
    public void writeToStream(DataWrite stream) throws IOException {
        stream.writePacketShortLenString(data);
    }

    @Override
    public void readFromStream(DataRead stream) throws IOException {
        data = stream.readPacketShortLenString();
    }
}
