package me.demo.rpc.netty.protocol;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.demo.rpc.netty.io.DataWriteStream;

public class ResponseEncoder extends MessageToByteEncoder<RPCResponse> {

	public ResponseEncoder() {
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, RPCResponse msg, ByteBuf out) throws Exception {
		try {
			ByteBufOutputStream outStream = new ByteBufOutputStream(out);
			DataWriteStream stream = new DataWriteStream(outStream, 30000);
			msg.writeToStream(stream);
		} catch (Throwable e) {
//			logger.error("code " + msg.toString() + " writeToStream error:", e);
		}
	}

}
