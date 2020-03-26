package me.demo.rpc.netty.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.demo.common.helper.StringHelper;
import me.demo.common.exceptions.RPCException;
import me.demo.common.exceptions.ServiceNotFoundException;
import me.demo.rpc.netty.protocol.RPCRequest;
import me.demo.rpc.netty.protocol.RPCResponse;
import me.demo.common.exceptions.UnexpectedRPCException;
import me.demo.rpc.netty.protocol.Service;

/**
 * 服务池
 * 

 *
 */
public class ServicePool {
//	static final Logger logger = Logger.getLogger(ServicePool.class, "biss", "rpcs");
//	static final Logger warnLogger = Logger.getLogger("warnlog", "biss", "rpcs");
	ConcurrentHashMap<String, ServiceInstancePool> registerServices = new ConcurrentHashMap<String, ServiceInstancePool>();
	public static ServicePool defaultInstance = new ServicePool();
	static ServiceJarLoader loader = new ServiceJarLoader(new URL[] {}, ClassLoader.getSystemClassLoader());

	public void load(String path, String libPath) throws IOException, ClassNotFoundException {
//		logger.info("");
//		logger.info("==================[register service beans begin]======================");
		String[] paths = StringHelper.splitToStringArray(libPath, ";");
		for (String p : paths) {
			if (p.trim().isEmpty())
				continue;
			File file = new File(p.trim());

			if (!file.exists()) {
//				logger.warn(file.getAbsolutePath() + " doesn't exists, check it or remove it from configuration.");
				continue;
			} else if (file.isDirectory()) {
				File[] s = file.listFiles();
				if (s == null)
					continue;
				for (File f : s) {
					if (f.getAbsolutePath().endsWith(".jar")) {
						loader.addURL(f.toURI().toURL());
//						logger.info("add " + f.getAbsolutePath() + " to class path.");
					}
				}
			} else if (file.getAbsolutePath().endsWith(".jar")) {
				loader.addURL(file.toURI().toURL());
//				logger.info("add " + file.getAbsolutePath() + " to class path.");
			}
		}
		paths = StringHelper.splitToStringArray(path, ";");
		for (String p : paths) {
			if (p.trim().isEmpty())
				continue;
			File file = new File(p.trim());
			if (!file.exists()) {
//				logger.warn(file.getAbsolutePath() + " doesn't exists, check it or remove it from configuration.");
				continue;
			} else if (file.isDirectory()) {
				File[] s = file.listFiles();
				if (s == null)
					continue;
				for (File f : s) {
					registerService(f);
				}
			} else
				registerService(file);
		}
//		logger.info("==================[register service beans end]========================");
	}

	public void registerService(File f) throws IOException {
		if (f.getAbsolutePath().endsWith(".jar")) {
			JarFile jar = new JarFile(f);
			try {
				loader.addURL(f.toURI().toURL());
//				logger.info("add " + f.getAbsolutePath() + " to class path.");
				Enumeration<JarEntry> en = jar.entries();
				while (en.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) en.nextElement();
					if (jarEntry.getName().endsWith(".class")) {
						String className = jarEntry.getName();
						className = className.substring(0, className.length() - 6);
						className = className.replace("\\", ".");
						className = className.replace("/", ".");
						Class<?> c = Class.forName(className, true, loader);
						Service[] service = c.getAnnotationsByType(Service.class);
						if (service.length > 0) {
							if (IRPCService.class.isAssignableFrom(c)) {
								@SuppressWarnings("unchecked")
								ServiceInstancePool pool = new ServiceInstancePool(service[0].name(),
										(Class<IRPCService>) c);
								registerServices.put(service[0].name(), pool);
//								logger.info(
//										"service [" + service[0].name() + "] registered; bean class:" + c.getName());
							}
						}
					}
				}
			} catch (Throwable e) {
//				logger.error("load jar file [" + f.getAbsolutePath() + "] error:", e);
			} finally {
				jar.close();
			}
		}
	}

	public boolean registerService(String serviceName, Class<IRPCService> clazz) {
		if (registerServices.contains(serviceName))
			return false;
		registerServices.put(serviceName, new ServiceInstancePool(serviceName, clazz));
		return true;
	}

	public boolean deRegisterService(String serviceName) {
		return registerServices.remove(serviceName) != null;
	}

	/**
	 * 获取一个服务实例
	 * 
	 * @param serviceName
	 * @return
	 * @throws RPCException
	 */
	ServiceInstance getServiceInstance(String serviceName) throws RPCException {
		ServiceInstancePool pool = registerServices.get(serviceName);
		if (pool != null) {
			try {
				return pool.getInstance();
			} catch (Throwable e) {
				throw new UnexpectedRPCException(e);
			}
		} else
			throw new ServiceNotFoundException("can't found service [" + serviceName + "]");
	}

	boolean returnServiceInstance(String serviceName, ServiceInstance o) throws RPCException {
		ServiceInstancePool pool = registerServices.get(serviceName);
		if (pool != null) {
			try {
				return pool.returnInstance(o);
			} catch (Throwable e) {
				throw new UnexpectedRPCException(e);
			}
		} else
			throw new ServiceNotFoundException("can't found service [" + serviceName + "]");
	}

	public RPCResponse invokeServiceMethod(RPCRequest req) {
		long beginTime = System.currentTimeMillis();
		RPCResponse response = new RPCResponse(req.getPackId(), false, null, null);
		try {
			ServiceInstance is = getServiceInstance(req.getServiceName());
			if (is == null)
				throw new RPCException("calling a no defined service[" + req.getServiceName() + "]");
			try {
				response.setRetValue(is.invokeServiceMethod(req));
				long ts = System.currentTimeMillis() - beginTime;
//				if (ts >= BissUtil.warnLogConfig.getRpcTimeout()) {
//					warnLogger.warn(new CallLoggerData("rpcs timeout warn", "rpcs", req.toString(), new Date(beginTime),
//							new Date()));
//				}
			} finally {
				returnServiceInstance(req.getServiceName(), is);
			}
		} catch (Throwable e) {
			//			logger.error("call " + req.toString() + " failure, take " + (System.currentTimeMillis() - beginTime)
			//					+ " milliseconds.", e);
			response.setException(e);
		}
		return response;
	}
}
