package me.demo.rpc.netty.pools;

public interface AsyncNotifyListener<K> {
	void onReceive(IAsyncConnectionPack<K> pack);

	void onOpenConnectionBegin(AsyncConnection<K> connection);

	void onOpenConnectionEnd(AsyncConnection<K> connection);
}
