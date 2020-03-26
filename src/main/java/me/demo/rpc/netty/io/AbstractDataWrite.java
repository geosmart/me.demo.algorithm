package me.demo.rpc.netty.io;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import me.demo.common.helper.BytesHelper;
import me.demo.common.exceptions.TimeoutException;

/**
 * 数据写入流，实现DataWrite接口
 *



 */
public abstract class AbstractDataWrite implements DataWrite {
    int timeout;
    String writeCharset;

    protected abstract OutputStream getOutputStream();

    /**
     * AbstractDataWrite
     *
     * @param timeout 写数据超时时间，以毫秒为单位
     */
    public AbstractDataWrite(int timeout) {
        super();
        this.timeout = timeout;
        this.writeCharset = "utf-8";
    }

    /**
     * 获取发送命令的字符集
     */
    public String getWriteCharset() {
        return writeCharset;
    }

    /**
     * 设置发送命令的字符集
     *
     * @param charset 字符编码
     */
    public void setWriteCharset(String charset) {
        this.writeCharset = charset;
    }

    /**
     * 获取写数据超时时间，以毫秒为单位
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * 设置写数据超时时间，以毫秒为单位
     *
     * @param timeout 超时时间
     */

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    abstract protected int write(byte[] b, int offset, int len, int timeout) throws IOException;

    @Override
    public int write(byte[] b, int offset, int len) throws IOException {
        return write(b, offset, len, timeout);
    }

    @Override
    public int write(byte[] b) throws IOException {
        if (b == null)
            throw new NullPointerException();
        return write(b, 0, b.length);
    }

    @Override
    public void writeFully(byte[] b, int offset, int len) throws IOException {
        long prev = System.currentTimeMillis();
        int leftLen = len;
        while (leftLen > 0 && (System.currentTimeMillis() - prev) < timeout) {
            int l = write(b, offset + len - leftLen, leftLen, timeout - (int) (System.currentTimeMillis() - prev));
            if (l > 0) {
                prev = System.currentTimeMillis() - timeout + 1000;
                leftLen -= l;
                if (leftLen <= 0)
                    break;
            }
        }
        if (leftLen > 0) {
            throw new TimeoutException("写数据超时");
        }
    }

    @Override
    public void writeFully(byte[] b) throws IOException {
        if (b == null)
            b = new byte[0];
        writeFully(b, 0, b.length);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        writeByte(v ? -1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        byte[] b = new byte[1];
        b[0] = (byte) v;
        writeFully(b);
    }

    @Override
    public void writeShort(int v, boolean isNetByteOrder) throws IOException {
        byte[] b = BytesHelper.shortToBytes((short) v);
        if (isNetByteOrder)
            b = BytesHelper.bytesReverse(b, 0, b.length);
        writeFully(b);
    }

    @Override
    public void writeInt(int v, boolean isNetByteOrder) throws IOException {
        byte[] b = BytesHelper.intToBytes(v);
        if (isNetByteOrder)
            b = BytesHelper.bytesReverse(b, 0, b.length);
        writeFully(b);
    }

    @Override
    public void writeLong(long v) throws IOException {
        byte[] b = BytesHelper.longToBytes(v);
        writeFully(b);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        byte[] b = BytesHelper.floatToBytes(v);
        writeFully(b);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        byte[] b = BytesHelper.doubleToBytes(v);
        writeFully(b);
    }

    @Override
    public void writeDate(Date v) throws IOException {
        if (v == null)
            v = new Date(0);
        byte[] b = BytesHelper.longToBytes(v.getTime());
        writeFully(b);
    }

    @Override
    public void writePacketByteLen(byte[] v) throws IOException {
        if (v == null)
            v = new byte[0];
        int len = v.length;
        if (len > 255)
            len = 255;
        writeByte(len);
        writeFully(v, 0, len);
    }

    @Override
    public void writePacketShortLen(byte[] v, boolean isNetByteOrder) throws IOException {
        if (v == null)
            v = new byte[0];
        int len = v.length;
        if (len > 65535)
            len = 65535;
        writeShort(len, isNetByteOrder);
        writeFully(v, 0, len);
    }

    @Override
    public void writePacketIntLen(byte[] v, boolean isNetByteOrder) throws IOException {
        if (v == null)
            v = new byte[0];
        int len = v.length;
        if (len > 65535000)
            len = 65535000;
        writeInt(len, isNetByteOrder);
        writeFully(v, 0, len);
    }

    @Override
    public void writeString(String v) throws IOException {
        if (v == null)
            v = "";
        writeFully(v.getBytes(writeCharset));
    }

    @Override
    public void writePacketByteLenString(String v) throws IOException {
        if (v == null)
            v = "";
        writePacketByteLen(v.getBytes(writeCharset));
    }

    @Override
    public void writePacketShortLenString(String v, boolean isNetByteOrder) throws IOException {
        if (v == null)
            v = "";
        writePacketShortLen(v.getBytes(writeCharset), isNetByteOrder);
    }

    @Override
    public void writePacketShortLen(byte[] v) throws IOException {
        writePacketShortLen(v, false);
    }

    @Override
    public void writePacketIntLen(byte[] v) throws IOException {
        writePacketIntLen(v, false);
    }

    @Override
    public void writePacketShortLenString(String v) throws IOException {
        writePacketShortLenString(v, false);
    }

    @Override
    public void writePacketIntLenString(String v, boolean isNetByteOrder) throws IOException {
        if (v == null)
            v = "";
        writePacketIntLen(v.getBytes(writeCharset), isNetByteOrder);
    }

    @Override
    public void writePacketIntLenString(String v) throws IOException {
        writePacketIntLenString(v, false);
    }

    @Override
    public void writeShort(int v) throws IOException {
        writeShort(v, false);
    }

    @Override
    public void writeInt(int v) throws IOException {
        writeInt(v, false);
    }

    @Override
    public void writeByteList(Collection<Byte> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (Byte o : ls)
                writeByte(o);
        }
    }

    @Override
    public void writeShortList(Collection<Short> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (Short o : ls)
                writeShort(o);
        }
    }

    @Override
    public void writeIntList(Collection<Integer> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (Integer o : ls)
                writeInt(o);
        }
    }

    @Override
    public void writeLongList(Collection<Long> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (Long o : ls)
                writeLong(o);
        }
    }

    @Override
    public void writeFloatList(Collection<Float> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (Float o : ls)
                writeFloat(o);
        }
    }

    @Override
    public void writeDoubleList(Collection<Double> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (Double o : ls)
                writeDouble(o);
        }
    }

    @Override
    public <T extends Writable> void writeList(Collection<T> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (T o : ls)
                o.writeToStream(this);
        }
    }

    @Override
    public void writePacketByteLenStringList(Collection<String> ls) throws IOException {
        int c = 0;
        if (ls != null)
            c = ls.size();
        writeInt(c);
        if (ls != null)
            for (String o : ls)
                writePacketByteLenString(o);
    }

    @Override
    public void writePacketShortLenStringList(Collection<String> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (String o : ls)
                writePacketShortLenString(o);
        }
    }

    @Override
    public void writePacketIntLenStringList(Collection<String> ls) throws IOException {
        if (ls == null)
            writeInt(0);
        else {
            writeInt(ls.size());
            for (String o : ls)
                writePacketIntLenString(o);
        }
    }

    public void writeJavaSerializable(Serializable o) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(getOutputStream());
        out.writeObject(o);
    }

    public void writeObject(Object o) throws IOException {
        if (o == null) {
            writeByte(OBJECT_NULL);
        } else {
            String s = o.getClass().getTypeName();
            if (o.getClass().isArray()) {
                s = s.substring(0, s.length() - 2);
                if (s.equals("boolean")) {
                    boolean[] b = (boolean[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_BOOLEAN_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_BOOLEAN_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_BOOLEAN_ARRAY);
                        writeInt(len);
                    }
                    for (boolean v : b) {
                        writeBoolean(v);
                    }
                } else if (s.equals("java.lang.Boolean")) {
                    Boolean[] b = (Boolean[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_BOOLEAN_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_BOOLEAN_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_BOOLEAN_ARRAY);
                        writeInt(len);
                    }
                    for (Boolean v : b) {
                        writeBoolean(v);
                    }
                } else if (s.equals("byte")) {
                    byte[] b = (byte[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_BYTE_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_BYTE_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_BYTE_ARRAY);
                        writeInt(len);
                    }
                    for (byte v : b) {
                        writeByte(v);
                    }
                } else if (s.equals("java.lang.Byte")) {
                    Byte[] b = (Byte[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_BYTE_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_BYTE_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_BYTE_ARRAY);
                        writeInt(len);
                    }
                    for (Byte v : b) {
                        writeByte(v);
                    }
                } else if (s.equals("short")) {
                    short[] b = (short[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_SHORT_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_SHORT_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_SHORT_ARRAY);
                        writeInt(len);
                    }
                    for (short v : b) {
                        writeShort(v);
                    }
                } else if (s.equals("java.lang.Short")) {
                    Short[] b = (Short[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_SHORT_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_SHORT_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_SHORT_ARRAY);
                        writeInt(len);
                    }
                    for (Short v : b) {
                        writeShort(v);
                    }
                } else if (s.equals("int")) {
                    int[] b = (int[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_INT_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_INT_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_INT_ARRAY);
                        writeInt(len);
                    }
                    for (int v : b) {
                        writeInt(v);
                    }
                } else if (s.equals("java.lang.Integer")) {
                    Integer[] b = (Integer[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_INT_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_INT_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_INT_ARRAY);
                        writeInt(len);
                    }
                    for (Integer v : b) {
                        writeInt(v);
                    }
                } else if (s.equals("long")) {
                    long[] b = (long[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_LONG_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_LONG_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_LONG_ARRAY);
                        writeInt(len);
                    }
                    for (long v : b) {
                        writeLong(v);
                    }
                } else if (s.equals("java.lang.Long")) {
                    Long[] b = (Long[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_LONG_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_LONG_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_LONG_ARRAY);
                        writeInt(len);
                    }
                    for (Long v : b) {
                        writeLong(v);
                    }
                } else if (s.equals("float")) {
                    float[] b = (float[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_FLOAT_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_FLOAT_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_FLOAT_ARRAY);
                        writeInt(len);
                    }
                    for (float v : b) {
                        writeFloat(v);
                    }
                } else if (s.equals("java.lang.Float")) {
                    Float[] b = (Float[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_FLOAT_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_FLOAT_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_FLOAT_ARRAY);
                        writeInt(len);
                    }
                    for (Float v : b) {
                        writeFloat(v);
                    }
                } else if (s.equals("double")) {
                    double[] b = (double[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_DOUBLE_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_DOUBLE_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_DOUBLE_ARRAY);
                        writeInt(len);
                    }
                    for (double v : b) {
                        writeDouble(v);
                    }
                } else if (s.equals("java.lang.Double")) {
                    Double[] b = (Double[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_DOUBLE_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_DOUBLE_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_DOUBLE_ARRAY);
                        writeInt(len);
                    }
                    for (Double v : b) {
                        writeDouble(v);
                    }
                } else if (s.equals("char")) {
                    char[] b = (char[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(PRIMITIVE_BYTE_COUNT_CHAR_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(PRIMITIVE_SHORT_COUNT_CHAR_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(PRIMITIVE_INT_COUNT_CHAR_ARRAY);
                        writeInt(len);
                    }
                    for (char v : b) {
                        writeShort(v);
                    }
                } else if (s.equals("java.lang.Character")) {
                    Character[] b = (Character[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_CHAR_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_CHAR_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_CHAR_ARRAY);
                        writeInt(len);
                    }
                    for (Character v : b) {
                        writeInt(v);
                    }
                } else if (s.equals("java.util.Date")) {
                    Date[] b = (Date[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_DATE_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_DATE_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_DATE_ARRAY);
                        writeInt(len);
                    }
                    for (Date v : b) {
                        writeDate(v);
                    }
                } else if (o instanceof ObjectReadWritable[]) {
                    ObjectReadWritable[] b = (ObjectReadWritable[]) o;
                    int len = b.length;
                    if (len <= 255) {
                        writeByte(OBJECT_BYTE_COUNT_READWRITABLE_ARRAY);
                        writeByte(len);
                    } else if (len <= 65535) {
                        writeByte(OBJECT_SHORT_COUNT_READWRITABLE_ARRAY);
                        writeShort(len);
                    } else {
                        writeByte(OBJECT_INT_COUNT_READWRITABLE_ARRAY);
                        writeInt(len);
                    }
                    writePacketShortLenString(s);
                    for (ObjectReadWritable v : b) {
                        v.writeToStream(this);
                    }
                } else {
                    writeByte(OBJECT_SERIALIZE);
                    writeJavaSerializable((Serializable) o);
                }
                // else
                // throw new IOException(o.getClass() +
                // " must be Serializable object array");
            } else if (o instanceof Collection<?>) {
                int len = ((Collection<?>) o).size();
                if (len <= 255) {
                    writeByte(OBJECT_BYTE_COUNT_LIST);
                    writeByte(len);
                } else if (len <= 65535) {
                    writeByte(OBJECT_SHORT_COUNT_LIST);
                    writeShort(len);
                } else {
                    writeByte(OBJECT_INT_COUNT_LIST);
                    writeInt(len);
                }
                writePacketShortLenString(s);
                Collection<?> b = (Collection<?>) o;
                for (Object v : b) {
                    writeObject(v);
                }
            } else if (o instanceof Map<?, ?>) {
                int len = ((Map<?, ?>) o).size();
                if (len <= 255) {
                    writeByte(OBJECT_BYTE_COUNT_MAP);
                    writeByte(len);
                } else if (len <= 65535) {
                    writeByte(OBJECT_SHORT_COUNT_MAP);
                    writeShort(len);
                } else {
                    writeByte(OBJECT_INT_COUNT_MAP);
                    writeInt(len);
                }
                writePacketShortLenString(s);
                Map<?, ?> b = (Map<?, ?>) o;
                Iterator<?> it = b.keySet().iterator();
                while (it.hasNext()) {
                    Object k = it.next();
                    Object v = b.get(k);
                    writeObject(k);
                    writeObject(v);
                }
            } else if (o instanceof Boolean) {
                writeByte(OBJECT_BOOLEAN);
                writeBoolean((Boolean) o);
            } else if (o instanceof Byte) {
                writeByte(OBJECT_BYTE);
                writeByte((Byte) o);
            } else if (o instanceof Short) {
                writeByte(OBJECT_SHORT);
                writeShort((Short) o);
            } else if (o instanceof Integer) {
                writeByte(OBJECT_INTEGER);
                writeInt((Integer) o);
            } else if (o instanceof Long) {
                writeByte(OBJECT_LONG);
                writeLong((Long) o);
            } else if (o instanceof Float) {
                writeByte(OBJECT_FLOAT);
                writeFloat((Float) o);
            } else if (o instanceof Double) {
                writeByte(OBJECT_DOUBLE);
                writeDouble((Double) o);
            } else if (o instanceof Date) {
                writeByte(OBJECT_DATE);
                writeDate((Date) o);
            } else if (o instanceof Character) {
                writeByte(OBJECT_CHAR);
                writeShort((Character) o);
            } else if (o instanceof String) {
                byte[] buf = ((String) o).getBytes("utf-8");
                if (buf.length <= 255) {
                    writeByte(OBJECT_BYTE_LEN_STRING);
                    writeByte(buf.length);
                } else if (buf.length <= 65535) {
                    writeByte(OBJECT_SHORT_LEN_STRING);
                    writeShort(buf.length);
                } else {
                    writeByte(OBJECT_INT_LEN_STRING);
                    writeInt(buf.length);
                }
                write(buf);
            } else if (o instanceof ObjectReadWritable) {
                writeByte(OBJECT_READWRITABLE);
                writePacketShortLenString(s);
                ((ObjectReadWritable) o).writeToStream(this);
            } else if (o instanceof JSONObject) {
                writeByte(OBJECT_JSON);
                byte[] buf = ((JSONObject) o).toString().getBytes("utf-8");
                writeInt(buf.length);
                write(buf);
            } else if (o instanceof Serializable) {
                writeByte(OBJECT_SERIALIZE);
                writeJavaSerializable((Serializable) o);
            } else
                throw new IOException(o.getClass() + " must be Serializable object");
        }
    }

    @Override
    public void writeLongDef(Long v, long def) throws IOException {
        if (v == null)
            writeLong(def);
        else
            writeLong(v);
    }

    @Override
    public void writeByteDef(Byte v, int def) throws IOException {
        if (v == null)
            writeByte(def);
        else
            writeByte(v);
    }

    @Override
    public void writeShortDef(Short v, int def) throws IOException {
        if (v == null)
            writeShort(def);
        else
            writeShort(v);
    }

    @Override
    public void writeIntDef(Integer v, int def) throws IOException {
        if (v == null)
            writeInt(def);
        else
            writeInt(v);
    }

    @Override
    public void writeFloatDef(Float v, float def) throws IOException {
        if (v == null)
            writeFloat(def);
        else
            writeFloat(v);
    }

    @Override
    public void writeDoubleDef(Double v, double def) throws IOException {
        if (v == null)
            writeDouble(def);
        else
            writeDouble(v);
    }

    @Override
    public void writeBooleanDef(Boolean v, boolean def) throws IOException {
        if (v == null)
            writeBoolean(def);
        else
            writeBoolean(v);

    }

    @Override
    public void writeDateDef(Date v, Date def) throws IOException {
        if (v == null)
            writeDate(def);
        else
            writeDate(v);
    }

    public void writePacketIntLenStream(InputStream inputStream, int len) throws IOException {
        byte b[] = new byte[4096];
        if (len > 0) {
            writeInt(len);
            int leftLen = len;
            int readlen;
            int c = leftLen > 4096 ? 4096 : leftLen;
            while ((readlen = inputStream.read(b, 0, c)) > 0) {
                write(b, 0, readlen);
                leftLen -= readlen;
                c = leftLen > 4096 ? 4096 : leftLen;
            }
            if (leftLen > 0)
                throw new IOException("已达流末尾");
        } else {
            int readlen;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            while ((readlen = inputStream.read(b)) > 0) {
                os.write(b, 0, readlen);
            }
            byte[] buf = os.toByteArray();
            writeInt(buf.length);
            write(buf);
        }
    }
}
