package me.demo.rpc.netty.protocol;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import me.demo.rpc.netty.pools.AbstractAsyncConnectionPack;
import me.demo.rpc.netty.pools.IAsyncConnectionPack;
import me.demo.rpc.netty.io.DataRead;
import me.demo.rpc.netty.io.DataWrite;
import me.demo.rpc.netty.io.DataWriteStream;
import me.demo.rpc.netty.io.ObjectReadWritable;
import me.demo.rpc.netty.io.ReadWritable;

public class RPCResponse extends AbstractAsyncConnectionPack<Integer> implements ReadWritable {
	private static final long serialVersionUID = 1L;
	int packId;
	boolean isIdlePack;
	Object retValue;
	Throwable exception;

	public RPCResponse() {
		super();
	}

	public RPCResponse(int packId, boolean isIdlePack, Object retValue, Throwable exception) {
		super();
		this.packId = packId;
		this.isIdlePack = isIdlePack;
		this.retValue = retValue;
		this.exception = exception;
	}

	@Override
	public Integer getPackId() {
		return packId;
	}

	@Override
	public IAsyncConnectionPack<Integer> getRecvPack() {
		return null;
	}

	@Override
	public boolean isRequestPack() {
		return false;
	}

	@Override
	public void setRecvPack(IAsyncConnectionPack<Integer> pack) {
	}

	@Override
	public void readFromStream(DataRead stream) throws IOException {
		stream.readInt(false);
		readFromStreamNoIncludeLength(stream);
	}

	public void readFromStreamNoIncludeLength(DataRead stream) throws IOException {
		packId = stream.readInt();
		isIdlePack = stream.readBoolean();
		if (!isIdlePack) {
			try {
				int op = stream.readByte();
				if (op == 0)
					retValue = stream.readObject();
				else {
					String className = stream.readPacketShortLenString();
					ObjectReadWritable obj = (ObjectReadWritable) Class.forName(className).newInstance();
					obj.readFromStream(stream);
					retValue = obj;
				}
				exception = (Throwable) stream.readObject();
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public void writeToStream(DataWrite s) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataWriteStream stream = new DataWriteStream(os, 3000);
		writeToStreamNoIncludeLength(stream);
		byte[] buf = os.toByteArray();
		s.writeInt(buf.length, true);
		s.write(buf);
	}

	public void writeToStreamNoIncludeLength(DataWrite stream) throws IOException {
		stream.writeInt(packId);
		stream.writeBoolean(isIdlePack);
		if (!isIdlePack) {
			try {
				if (retValue == null || !(retValue instanceof ObjectReadWritable)) {
					stream.writeByte(0);
					stream.writeObject(retValue);
				} else {
					stream.writeByte(1);
					stream.writePacketShortLenString(retValue.getClass().getTypeName());
					((ObjectReadWritable) retValue).writeToStream(stream);
				}
				stream.writeObject(exception);
			} catch (IOException e) {
				throw e;
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "RPCResponse [packId=" + packId + ", retValue=" + retValue + ", exception=" + exception + "]";
	}

	public Object getRetValue() {
		return retValue;
	}

	public void setRetValue(Object retValue) {
		this.retValue = retValue;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public void setPackId(int packId) {
		this.packId = packId;
	}

	@Override
	public boolean isIdlePack() {
		return isIdlePack;
	}

}
