package me.demo.rpc.netty.server;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import me.demo.rpc.netty.protocol.RPCRequest;
import me.demo.common.exceptions.RPCException;
import me.demo.common.exceptions.UnexpectedRPCException;
import me.demo.common.util.DateTime;

public class ServiceInstance {
    IRPCService service;
    ConcurrentHashMap<String, Method> loadedMethod;
    boolean working;
    Date invokeBeginTime = new Date();
    Date invokeEndTime = null;

    public ServiceInstance(IRPCService service, ConcurrentHashMap<String, Method> loadedMethod) {
        this.service = service;
        this.loadedMethod = loadedMethod;
    }

    public double getInvokeTime() {
        if (invokeBeginTime != null && invokeEndTime != null)
            return DateTime.milliSecondsBetween(invokeEndTime, invokeEndTime);
        else
            return -1;
    }

    public boolean isWorking() {
        return working;
    }

    public Date getInvokeBeginTime() {
        return invokeBeginTime;
    }

    public Date getInvokeEndTime() {
        return invokeEndTime;
    }

    public Object invokeServiceMethod(RPCRequest req) throws Throwable {
        Throwable throwable = null;
        Object retValue = null;
        invokeBeginTime = new Date();
        working = true;
        try {
            String[] types = req.getTypes();
            StringBuffer key = new StringBuffer(req.getMethodName());
            if (types != null) {
                for (String k : types)
                    key.append(";" + k);
            }
            String kstr = key.toString();
            Method method = loadedMethod.get(kstr);
            if (method == null) {
                method = req.findMethod(service.getClass());
                if (method == null)
                    throw new RPCException("calling a no defined interface");
                loadedMethod.put(kstr, method);
            }
            service.invokeBefore(req);
            retValue = method.invoke(service, req.getArgs());
            return retValue;
        } catch (IllegalAccessException e) {
            throwable = e;
            throw new UnexpectedRPCException(e);
        } catch (IllegalArgumentException e) {
            throwable = e;
            throw new UnexpectedRPCException(e);
        } catch (InvocationTargetException e) {
            throwable = e.getCause() != e ? e.getCause() : e;
            throw throwable;
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            invokeEndTime = new Date();
            working = false;
            service.invokeAfter(req, retValue, throwable);
        }
    }
}
