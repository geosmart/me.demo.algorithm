package me.demo.rpc.netty.watch;

/**
 * 监视任务
 * 



 * 
 */
public interface WatchTask {
	/**
	 * 监控
	 */
	public void watch() throws InterruptedException;
}
