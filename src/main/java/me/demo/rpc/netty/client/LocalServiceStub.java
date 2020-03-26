package me.demo.rpc.netty.client;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import me.demo.common.exceptions.TimeoutException;
import me.demo.rpc.netty.protocol.RPCRequest;
import me.demo.rpc.netty.protocol.RPCResponse;

public class LocalServiceStub implements InvocationHandler {
    String serviceName;
    RPCTcpConnectionPool pool;
//	static final Logger warnLogger = Logger.getLogger("warnlog", "biss", "rpcclient");

    public LocalServiceStub(RPCTcpConnectionPool pool, String serviceName) {
        super();
        this.serviceName = serviceName;
        this.pool = pool;
    }

    public Object getProxy(Class<?> cls) {
        ClassLoader classLoader = cls.getClassLoader();
        Class<?>[] interfaces = null;
        if (cls.isInterface()) {
            interfaces = new Class[]{cls};
        } else {
            interfaces = cls.getInterfaces();
        }
        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, this);
        return proxy;
    }

    protected void warnLog(long start) {
        try {
//			warnLogger.warn(
//					new CallLoggerData("rpc call time out", serviceName, pool.getName(), new Date(start), new Date()));
        } catch (Throwable ex) {
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        RPCRequest req = new RPCRequest(this.serviceName, method, args);
        RPCResponse r = null;
        TimeoutException te = null;
        try {
            r = (RPCResponse) pool.execute(req);
        } catch (TimeoutException e) {
            te = e;
        }
        if (r == null) {
            te = new TimeoutException("operation timeout");
        }
        long t = System.currentTimeMillis() - start;
        if (te != null) {
//				|| t > BissUtil.warnLogConfig.getRpcTimeout()) {
            warnLog(start);
            if (te != null)
                throw te;
        }
        if (r.getException() != null)
            throw r.getException();
        return r.getRetValue();
    }

}
