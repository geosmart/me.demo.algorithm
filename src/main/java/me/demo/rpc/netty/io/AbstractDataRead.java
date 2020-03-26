package me.demo.rpc.netty.io;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.demo.common.helper.BytesHelper;
import me.demo.common.exceptions.TimeoutException;

/**
 * 数据读取流，实现DataRead接口
 * 



 * @see DataRead
 * 
 */
public abstract class AbstractDataRead implements DataRead {
	int timeout;

	/**
	 * 构建DataReadStream
	 * @param timeout
	 *            读数据超时，以毫秒为单位
	 */
	public AbstractDataRead(int timeout) {
		super();
		this.timeout = timeout;
	}

	/**
	 * 获取读数据超时时间，以毫秒为单位
	 * 
	 * @return 读数据超时时间
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * 设置读数据超时时间，以毫秒为单位
	 * 
	 * @param timeout
	 *            读数据超时时间
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
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
	 * @param b
	 *            存储读取数据的缓冲区
	 * @param offset
	 *            读取的数据存储的起始偏移量
	 * @param len
	 *            读取的最大字节数
	 * @param timeout
	 *            指定的超时时间
	 * @return 读入缓冲区的字节总数，如果因为已经到达流的末尾而没有更多的数据，则返回 -1。

	 *             如果读数据超时
	 * @throws IOException
	 *             如果发生IO错误
	 */
	abstract protected int read(byte[] b, int offset, int len, int timeout) throws IOException;

	@Override
	public int read(byte[] b, int offset, int len) throws IOException {
		return read(b, offset, len, timeout);
	}

	@Override
	public int read(byte[] b) throws IOException {
		if (b == null)
			throw new NullPointerException();
		return read(b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int offset, int len) throws IOException {
		if (b == null)
			throw new NullPointerException();
		long prev = System.currentTimeMillis();
		int leftLen = len;
		while (leftLen > 0 && (System.currentTimeMillis() - prev) < timeout) {
			int l = read(b, offset + len - leftLen, leftLen, timeout - (int) (System.currentTimeMillis() - prev));
			if (l > 0) {
				prev = System.currentTimeMillis() - timeout + 1000;
				leftLen -= l;
				if (leftLen <= 0)
					break;
			} else if (l < 0)
				throw new EOFException("已达到流末尾");
		}
		if (leftLen > 0) {
			throw new TimeoutException("读数据超时");
		}
	}

	@Override
	public byte[] readFully(int len) throws IOException {
		byte[] b = new byte[len];
		readFully(b, 0, b.length);
		return b;
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		if (b == null)
			throw new NullPointerException();
		readFully(b, 0, b.length);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	@Override
	public byte readByte() throws IOException {
		return readFully(1)[0];
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return BytesHelper.byteToUnsigned(readByte());
	}

	@Override
	public short readShort(boolean isNetByteOrder) throws IOException {
		byte[] b = readFully(2);
		if (isNetByteOrder)
			b = BytesHelper.bytesReverse(b, 0, 2);
		return BytesHelper.bytesToShort(b, 0);
	}

	@Override
	public int readUnsignedShort(boolean isNetByteOrder) throws IOException {
		return BytesHelper.shortToUnsigned(readShort(isNetByteOrder));
	}

	@Override
	public int readInt(boolean isNetByteOrder) throws IOException {
		byte[] b = readFully(4);
		if (isNetByteOrder)
			b = BytesHelper.bytesReverse(b, 0, 4);
		return BytesHelper.bytesToInt(b, 0);
	}

	@Override
	public long readLong() throws IOException {
		byte[] b = readFully(8);
		return BytesHelper.bytesToLong(b, 0);
	}

	@Override
	public float readFloat() throws IOException {
		int r = readInt(false);
		return Float.intBitsToFloat(r);
	}

	@Override
	public double readDouble() throws IOException {
		long r = readLong();
		return Double.longBitsToDouble(r);
	}

	@Override
	public Date readDate() throws IOException {
		return new Date(readLong());
	}

	@Override
	public byte[] readPacketByteLen() throws IOException {
		int len = readUnsignedByte();
		return readFully(len);
	}

	@Override
	public byte[] readPacketShortLen(boolean isNetByteOrder) throws IOException {
		int len = readUnsignedShort(isNetByteOrder);
		return readFully(len);
	}

	@Override
	public byte[] readPacketIntLen(boolean isNetByteOrder) throws IOException {
		int len = readInt(isNetByteOrder);
		return readFully(len);
	}

	@Override
	public void skipBytes(int n) throws IOException {
		readFully(n);
	}

	@Override
	public String readString(int len) throws IOException {
		return new String(readFully(len), "utf-8");
	}

	@Override
	public String readPacketByteLenString() throws IOException {
		return new String(readPacketByteLen(), "utf-8");
	}

	@Override
	public String readPacketShortLenString(boolean isNetByteOrder) throws IOException {
		return new String(readPacketShortLen(isNetByteOrder), "utf-8");
	}

	@Override
	public byte[] readln(byte[] eofs, boolean returnIncludeEofs) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(readFully(eofs.length));
		long prev = System.currentTimeMillis();
		while ((System.currentTimeMillis() - prev) < timeout) {
			byte[] b = stream.toByteArray();
			if (BytesHelper.memcmp(b, b.length - eofs.length, eofs, 0, eofs.length) == 0) {
				int len = b.length;
				if (!returnIncludeEofs)
					len -= eofs.length;
				byte[] ret = new byte[len];
				System.arraycopy(b, 0, ret, 0, len);
				return ret;
			}
			try {
				stream.write(readFully(1));
			} catch (EOFException e) {
				int len = b.length;
				if (!returnIncludeEofs)
					len -= eofs.length;
				byte[] ret = new byte[len];
				System.arraycopy(b, 0, ret, 0, len);
				return ret;
			}
		}
		throw new TimeoutException("读数据超时");
	}

	@Override
	public byte[] readPacketShortLen() throws IOException {
		return readPacketShortLen(false);
	}

	@Override
	public byte[] readPacketIntLen() throws IOException {
		return readPacketIntLen(false);
	}

	@Override
	public String readPacketShortLenString() throws IOException {
		return new String(readPacketShortLen(false), "utf-8");
	}

	@Override
	public String readPacketIntLenString(boolean isNetByteOrder) throws IOException {
		return new String(readPacketIntLen(false), "utf-8");
	}

	@Override
	public String readPacketIntLenString() throws IOException {
		return readPacketIntLenString(false);
	}

	@Override
	public short readShort() throws IOException {
		return readShort(false);
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return readUnsignedShort(false);
	}

	@Override
	public int readInt() throws IOException {
		return readInt(false);
	}

	@Override
	public List<Byte> readByteList() throws IOException {
		List<Byte> r = new ArrayList<Byte>();
		int c = readInt(false);
		for (int i = 0; i < c; i++)
			r.add(readByte());
		return r;
	}

	@Override
	public List<Short> readShortList() throws IOException {
		List<Short> r = new ArrayList<Short>();
		int c = readInt(false);
		for (int i = 0; i < c; i++)
			r.add(readShort());
		return r;
	}

	@Override
	public List<Integer> readIntList() throws IOException {
		List<Integer> r = new ArrayList<Integer>();
		int c = readInt(false);
		for (int i = 0; i < c; i++)
			r.add(readInt());
		return r;
	}

	@Override
	public List<Long> readLongList() throws IOException {
		List<Long> r = new ArrayList<Long>();
		int c = readInt(false);
		for (int i = 0; i < c; i++)
			r.add(readLong());
		return r;
	}

	@Override
	public List<Float> readFloatList() throws IOException {
		List<Float> r = new ArrayList<Float>();
		int c = readInt(false);
		for (int i = 0; i < c; i++)
			r.add(readFloat());
		return r;
	}

	@Override
	public List<Double> readDoubleList() throws IOException {
		List<Double> r = new ArrayList<Double>();
		int c = readInt(false);
		for (int i = 0; i < c; i++)
			r.add(readDouble());
		return r;
	}

	@Override
	public <T extends Readable> List<T> readList(Class<T> clazz) throws IOException {
		List<T> r = new ArrayList<T>();
		int c = readInt(false);
		for (int i = 0; i < c; i++) {
			T t;
			try {
				t = clazz.newInstance();
			} catch (InstantiationException e) {
				throw new IOException(e);
			} catch (IllegalAccessException e) {
				throw new IOException(e);
			}
			t.readFromStream(this);
			r.add(t);
		}

		return r;
	}

	@Override
	public List<String> readPacketByteLenStringList() throws IOException {
		List<String> ls = new ArrayList<String>();
		int c = readInt();
		for (int i = 0; i < c; i++)
			ls.add(readPacketByteLenString());
		return ls;
	}

	@Override
	public List<String> readPacketShortLenStringList() throws IOException {
		List<String> ls = new ArrayList<String>();
		int c = readInt();
		for (int i = 0; i < c; i++)
			ls.add(readPacketShortLenString());
		return ls;
	}

	@Override
	public List<String> readPacketIntLenStringList() throws IOException {
		List<String> ls = new ArrayList<String>();
		int c = readInt();
		for (int i = 0; i < c; i++)
			ls.add(readPacketIntLenString());
		return ls;
	}

	protected abstract InputStream getInputStream();

	public Serializable readJavaSerializable() throws IOException, ClassNotFoundException {
		ObjectInputStream out = new ObjectInputStream(getInputStream());
		return (Serializable) out.readObject();
	}

	/**
	 * 读入一个对象
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Object readObject()
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		int flag = readByte();
		switch (flag) {
		case OBJECT_NULL:
			return null;
		case PRIMITIVE_BOOLEAN:
		case OBJECT_BOOLEAN:
			return readByte() != 0;
		case PRIMITIVE_BYTE:
		case OBJECT_BYTE:
			return readByte();
		case PRIMITIVE_CHAR:
		case OBJECT_CHAR:
			return (char) readShort(false);
		case PRIMITIVE_SHORT:
		case OBJECT_SHORT:
			return readShort(false);
		case PRIMITIVE_INTEGER:
		case OBJECT_INTEGER:
			return readInt(false);
		case PRIMITIVE_LONG:
		case OBJECT_LONG:
			return readLong();
		case PRIMITIVE_DOUBLE:
		case OBJECT_DOUBLE:
			return readDouble();
		case PRIMITIVE_FLOAT:
		case OBJECT_FLOAT:
			return readFloat();
		case OBJECT_DATE:
			return readDate();
		case OBJECT_READWRITABLE: {
			ObjectReadWritable r = (ObjectReadWritable) Class
					.forName(readPacketShortLenString()).newInstance();
			r.readFromStream(this);
			return r;
		}
		case OBJECT_SERIALIZE:
			return readJavaSerializable();
		case OBJECT_JSON: {
			int len = readInt();
			try {
				return JSON.parseObject(readString(len));
			} catch (JSONException e) {
				throw new IOException(e);
			}
		}
		case OBJECT_BYTE_LEN_STRING:
		case OBJECT_SHORT_LEN_STRING:
		case OBJECT_INT_LEN_STRING: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_LEN_STRING:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_LEN_STRING:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			return readString(len);
		}
		case OBJECT_BYTE_COUNT_MAP:
		case OBJECT_SHORT_COUNT_MAP:
		case OBJECT_INT_COUNT_MAP: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_MAP:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_MAP:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			@SuppressWarnings("unchecked")
			Map<Object, Object> map = (Map<Object, Object>) Class.forName(readPacketShortLenString()).newInstance();
			for (int i = 0; i < len; i++) {
				map.put(readObject(), readObject());
			}
			return map;
		}
		case OBJECT_BYTE_COUNT_LIST:
		case OBJECT_SHORT_COUNT_LIST:
		case OBJECT_INT_COUNT_LIST: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_LIST:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_LIST:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			@SuppressWarnings("unchecked")
			Collection<Object> c = (Collection<Object>) Class.forName(readPacketShortLenString()).newInstance();
			for (int i = 0; i < len; i++) {
				c.add(readObject());
			}
			return c;
		}
		case OBJECT_BYTE_COUNT_READWRITABLE_ARRAY:
		case OBJECT_SHORT_COUNT_READWRITABLE_ARRAY:
		case OBJECT_INT_COUNT_READWRITABLE_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_READWRITABLE_ARRAY:
				len = readByte();
				break;
			case OBJECT_SHORT_COUNT_READWRITABLE_ARRAY:
				len = readShort();
				break;
			default:
				len = readInt();
			}
			Class<?> clazz = Class.forName(readPacketShortLenString());
			Object o = Array.newInstance(clazz, len);
			for (int i = 0; i < len; i++) {
				Object v = clazz.newInstance();
				((ObjectReadWritable) v).readFromStream(this);
				Array.set(o, i, v);
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_DATE_ARRAY:
		case OBJECT_SHORT_COUNT_DATE_ARRAY:
		case OBJECT_INT_COUNT_DATE_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_DATE_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_DATE_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Date[] o = new Date[len];
			for (int i = 0; i < len; i++) {
				o[i] = readDate();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_CHAR_ARRAY:
		case OBJECT_SHORT_COUNT_CHAR_ARRAY:
		case OBJECT_INT_COUNT_CHAR_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_CHAR_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_CHAR_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Character[] o = new Character[len];
			for (int i = 0; i < len; i++) {
				o[i] = (char) readShort();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_CHAR_ARRAY:
		case PRIMITIVE_SHORT_COUNT_CHAR_ARRAY:
		case PRIMITIVE_INT_COUNT_CHAR_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_CHAR_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_CHAR_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			char[] o = new char[len];
			for (int i = 0; i < len; i++) {
				o[i] = (char) readShort();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_DOUBLE_ARRAY:
		case OBJECT_SHORT_COUNT_DOUBLE_ARRAY:
		case OBJECT_INT_COUNT_DOUBLE_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_DOUBLE_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_DOUBLE_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Double[] o = new Double[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Double) readDouble();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_DOUBLE_ARRAY:
		case PRIMITIVE_INT_COUNT_DOUBLE_ARRAY:
		case PRIMITIVE_SHORT_COUNT_DOUBLE_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_DOUBLE_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_DOUBLE_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			double[] o = new double[len];
			for (int i = 0; i < len; i++) {
				o[i] = (double) readDouble();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_FLOAT_ARRAY:
		case OBJECT_SHORT_COUNT_FLOAT_ARRAY:
		case OBJECT_INT_COUNT_FLOAT_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_FLOAT_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_FLOAT_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Float[] o = new Float[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Float) readFloat();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_FLOAT_ARRAY:
		case PRIMITIVE_SHORT_COUNT_FLOAT_ARRAY:
		case PRIMITIVE_INT_COUNT_FLOAT_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_FLOAT_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_FLOAT_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			float[] o = new float[len];
			for (int i = 0; i < len; i++) {
				o[i] = (float) readFloat();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_LONG_ARRAY:
		case OBJECT_SHORT_COUNT_LONG_ARRAY:
		case OBJECT_INT_COUNT_LONG_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_LONG_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_LONG_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Long[] o = new Long[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Long) readLong();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_LONG_ARRAY:
		case PRIMITIVE_SHORT_COUNT_LONG_ARRAY:
		case PRIMITIVE_INT_COUNT_LONG_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_LONG_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_LONG_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			long[] o = new long[len];
			for (int i = 0; i < len; i++) {
				o[i] = (long) readLong();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_INT_ARRAY:
		case OBJECT_SHORT_COUNT_INT_ARRAY:
		case OBJECT_INT_COUNT_INT_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_INT_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_INT_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Integer[] o = new Integer[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Integer) readInt();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_INT_ARRAY:
		case PRIMITIVE_SHORT_COUNT_INT_ARRAY:
		case PRIMITIVE_INT_COUNT_INT_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_INT_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_INT_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			int[] o = new int[len];
			for (int i = 0; i < len; i++) {
				o[i] = (int) readInt();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_SHORT_ARRAY:
		case OBJECT_SHORT_COUNT_SHORT_ARRAY:
		case OBJECT_INT_COUNT_SHORT_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_SHORT_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_SHORT_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Short[] o = new Short[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Short) readShort();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_SHORT_ARRAY:
		case PRIMITIVE_SHORT_COUNT_SHORT_ARRAY:
		case PRIMITIVE_INT_COUNT_SHORT_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_SHORT_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_SHORT_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			short[] o = new short[len];
			for (int i = 0; i < len; i++) {
				o[i] = (short) readShort();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_BYTE_ARRAY:
		case OBJECT_SHORT_COUNT_BYTE_ARRAY:
		case OBJECT_INT_COUNT_BYTE_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_BYTE_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_BYTE_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Byte[] o = new Byte[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Byte) readByte();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_BYTE_ARRAY:
		case PRIMITIVE_SHORT_COUNT_BYTE_ARRAY:
		case PRIMITIVE_INT_COUNT_BYTE_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_BYTE_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_BYTE_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			byte[] o = new byte[len];
			for (int i = 0; i < len; i++) {
				o[i] = (byte) readByte();
			}
			return o;
		}
		case OBJECT_BYTE_COUNT_BOOLEAN_ARRAY:
		case OBJECT_SHORT_COUNT_BOOLEAN_ARRAY:
		case OBJECT_INT_COUNT_BOOLEAN_ARRAY: {
			int len;
			switch (flag) {
			case OBJECT_BYTE_COUNT_BOOLEAN_ARRAY:
				len = readUnsignedByte();
				break;
			case OBJECT_SHORT_COUNT_BOOLEAN_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			Boolean[] o = new Boolean[len];
			for (int i = 0; i < len; i++) {
				o[i] = (Boolean) readBoolean();
			}
			return o;
		}
		case PRIMITIVE_BYTE_COUNT_BOOLEAN_ARRAY:
		case PRIMITIVE_SHORT_COUNT_BOOLEAN_ARRAY:
		case PRIMITIVE_INT_COUNT_BOOLEAN_ARRAY: {
			int len;
			switch (flag) {
			case PRIMITIVE_BYTE_COUNT_BOOLEAN_ARRAY:
				len = readUnsignedByte();
				break;
			case PRIMITIVE_SHORT_COUNT_BOOLEAN_ARRAY:
				len = readUnsignedShort();
				break;
			default:
				len = readInt();
			}
			boolean[] o = new boolean[len];
			for (int i = 0; i < len; i++) {
				o[i] = (boolean) readBoolean();
			}
			return o;
		}
		default:
			throw new IOException("Wrong data format");
		}
	}
}
