package me.demo.rpc.netty.protocol;


import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.atomic.AtomicInteger;

import me.demo.rpc.netty.io.Writable;
import me.demo.rpc.netty.pools.AbstractAsyncConnectionPack;
import me.demo.rpc.netty.pools.IAsyncConnectionPack;
import me.demo.rpc.netty.io.DataRead;
import me.demo.rpc.netty.io.DataWrite;
import me.demo.rpc.netty.io.DataWriteStream;
import me.demo.rpc.netty.io.ObjectReadWritable;
import me.demo.rpc.netty.io.ReadWritable;

public class RPCRequest extends AbstractAsyncConnectionPack<Integer> implements ReadWritable {
	private static final long serialVersionUID = 1L;
	String serviceName, methodName;
	Object[] args;
	String[] types;
	int packId;
	IAsyncConnectionPack<Integer> recvPack = null;
	static AtomicInteger currentPackId = new AtomicInteger(1);
	public final static int MIN_PACK_BYTES = 7;

	public RPCRequest() {
	}

	public RPCRequest(String serviceName, Method method, Object[] args) throws IOException {
		super();
		packId = currentPackId.getAndIncrement();
		if (packId >= Integer.MAX_VALUE - 100000)
			currentPackId.set(0);
		if (serviceName != null) {
			this.serviceName = serviceName;
			this.methodName = method.getName();
			types = new String[method.getParameterCount()];
			int i = 0;
			for (Parameter p : method.getParameters()) {
				types[i++] = p.getType().getTypeName();
			}
			this.args = args;
		}
	}

	public Method findMethod(Class<?> clazz) {
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(getMethodName()) && m.getParameterCount() == getTypes().length) {
				int i = 0;
				boolean finded = true;
				for (Class<?> c : m.getParameterTypes()) {
					if (!c.getTypeName().equals(getTypes()[i++])) {
						finded = false;
						break;
					}
				}
				if (finded) {
					return m;
				}
			}
		}
		return findMethod(clazz.getSuperclass());
	}

	@Override
	public Integer getPackId() {
		return packId;
	}

	@Override
	public IAsyncConnectionPack<Integer> getRecvPack() {
		return recvPack;
	}

	@Override
	public boolean isRequestPack() {
		return true;
	}

	@Override
	public void setRecvPack(IAsyncConnectionPack<Integer> pack) {
		this.recvPack = pack;
	}

	static public int getMinPackBytes() {
		return MIN_PACK_BYTES;
	}

	@Override
	public void readFromStream(DataRead stream) throws IOException {
		stream.readInt(true);
		packId = stream.readInt();
		try {
			serviceName = (String) stream.readObject();
			methodName = (String) stream.readObject();
			types = (String[]) stream.readObject();
			int c = stream.readShort();
			if (c > 0) {
				args = new Object[c];
				for (int i = 0; i < c; i++) {
					int b = stream.readByte();
					if (b == 0)
						args[i] = stream.readJavaSerializable();
					else if (b == 2)
						args[i] = stream.readObject();
					else {
						String s = stream.readPacketShortLenString();
						ObjectReadWritable a = (ObjectReadWritable) Class.forName(s).newInstance();
						a.readFromStream(stream);
						args[i] = a;
					}
				}
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void writeToStream(DataWrite s) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		DataWriteStream stream = new DataWriteStream(os, 3000);
		stream.writeInt(packId);
		stream.writeObject(serviceName);
		stream.writeObject(methodName);
		stream.writeObject(types);
		stream.writeShort(args == null ? 0 : args.length);
		if (args != null) {
			for (Object o : args) {
				if (o != null) {
					if (o instanceof JSONObject) {
						stream.writeByte(2);
						stream.writeObject(o);
					} else if (o instanceof ObjectReadWritable) {
						stream.writeByte(1);
						stream.writePacketShortLenString(o.getClass().getTypeName());
						((Writable) o).writeToStream(stream);
					} else {
						stream.writeByte(0);
						stream.writeJavaSerializable((Serializable) o);
					}
				} else {
					stream.writeByte(0);
					stream.writeJavaSerializable((Serializable) o);
				}

			}
		}
		//stream.writeObject(args);
		byte[] buf = os.toByteArray();
		s.writeInt(buf.length, true);
		s.write(buf);
	}

	@Override
	public String toString() {
		return "RPCRequest[packId=" + packId + "," + serviceName + "." + methodName + "(" + args + ")]";
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setPackId(int packId) {
		this.packId = packId;
	}

	public String[] getTypes() {
		return types;
	}

	@Override
	public boolean isIdlePack() {
		return serviceName == null;
	}

}
