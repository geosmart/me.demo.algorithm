package me.demo.rpc.netty.server;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import me.demo.common.exceptions.RPCException;
import me.demo.common.exceptions.UnexpectedRPCException;
import me.demo.common.util.DateTime;

/**
 * 服务实例池管理
 * 

 *
 */
public class ServiceInstancePool {
	private CopyOnWriteArrayList<ServiceInstance> workInstances = new CopyOnWriteArrayList<ServiceInstance>();
	private CopyOnWriteArrayList<ServiceInstance> idleInstances = new CopyOnWriteArrayList<ServiceInstance>();
	private Class<IRPCService> clazz;
	private String serviceName;
	ConcurrentHashMap<String, Method> loadedMethod = new ConcurrentHashMap<String, Method>();
	private long lastCheckIdleInstancesTime = 0;
	private int overTimeRemoveSeconds = 6;
	private int minInstanceCount = 10;
	private int maxInstanceCount = Integer.MAX_VALUE;

	public ServiceInstancePool(String serviceName, Class<IRPCService> clazz) {
		this.clazz = clazz;
		this.serviceName = serviceName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getServiceName() {
		return serviceName;
	}

	private int getTotalInstanceCount() {
		return idleInstances.size() + workInstances.size();
	}

	private void cleanUnusedInstances() {
		if (DateTime.secondsBetween(lastCheckIdleInstancesTime, System.currentTimeMillis()) > 60) {
			lastCheckIdleInstancesTime = System.currentTimeMillis();
			int needCleanItemCount = getTotalInstanceCount() - minInstanceCount;
			if (needCleanItemCount > 0) {
				Date now = new Date(lastCheckIdleInstancesTime);
				int removedItemCount = 0;
				for (int i = 0; i < idleInstances.size(); i++) {
					ServiceInstance instance = idleInstances.get(i);
					if (instance.getInvokeBeginTime() != null) {
						if (DateTime.secondsBetween(now, instance.getInvokeBeginTime()) > overTimeRemoveSeconds) {
							idleInstances.remove(i);
							instance.service.destroy();
							i--;
							needCleanItemCount--;
							removedItemCount++;
							if (needCleanItemCount <= 0)
								break;
						}
					}
				}
//				logger.debug("remove " + removedItemCount + " no used instance.");
			}
		}
	}

	/**
	 * 获取一个实例
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws RPCException
	 */
	public synchronized ServiceInstance getInstance() throws RPCException {
		cleanUnusedInstances();
		ServiceInstance r;
		if (idleInstances.size() == 0) {
			if (getTotalInstanceCount() >= maxInstanceCount) {
				throw new RPCException("server is to busy.");
			}
			try {
				r = new ServiceInstance(clazz.newInstance(), loadedMethod);
				r.service.init();
			} catch (InstantiationException e) {
				throw new UnexpectedRPCException(e);
			} catch (IllegalAccessException e) {
				throw new UnexpectedRPCException(e);
			}
			workInstances.add(r);
		} else {
			r = idleInstances.remove(0);
		}
		return r;
	}

	/**
	 * 归还一个实例
	 * 
	 * @param o
	 * @return <code>true</code> 移除成功<br>
	 *         <code>false</code> 没有可以移除的对象
	 */
	public synchronized boolean returnInstance(ServiceInstance o) {
		if (workInstances.remove(o)) {
			if (!idleInstances.contains(o))
				idleInstances.add(o);
			return true;
		} else
			return false;
	}
}
