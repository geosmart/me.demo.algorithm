package me.demo.rpc.netty.io;

/**
 * 用于传递完整的Object流对象，此对象中所有的成员变量都必须通过流式读入读取，包括可能为null的变量。如果有一个成员变量未流式写入，就可能不会传至RPC服务端

 *
 */
public interface ObjectReadWritable extends ReadWritable {

}
