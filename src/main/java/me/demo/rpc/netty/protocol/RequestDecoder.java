package me.demo.rpc.netty.protocol;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.demo.rpc.netty.io.DataReadStream;

public class RequestDecoder extends ByteToMessageDecoder {

	public RequestDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= RPCRequest.MIN_PACK_BYTES) {
			ByteBufInputStream inStream = new ByteBufInputStream(in);
			DataReadStream stream = new DataReadStream(inStream, 30000);
			RPCRequest r = new RPCRequest();
			r.readFromStream(stream);
			out.add(r);
		}
	}

}
