package me.demo.rpc.netty.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class ServiceJarLoader extends URLClassLoader {
	/** URLClassLoader的addURL方法 */
	private static Method sAddURL = initAddMethod();

	private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();

	/**
	 * 初始化addUrl 方法.
	 * 
	 * @return 可访问addUrl方法的Method对象
	 */
	private static Method initAddMethod() {
		try {
			Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			add.setAccessible(true);
			return add;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ServiceJarLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public ServiceJarLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public ServiceJarLoader(URL[] urls) {
		super(urls);
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
		try {
			sAddURL.invoke(classloader, new Object[] { url });
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
	}

}