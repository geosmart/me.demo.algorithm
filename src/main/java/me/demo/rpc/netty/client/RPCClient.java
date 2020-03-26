package me.demo.rpc.netty.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.demo.common.exceptions.RPCException;
import me.demo.common.helper.StringHelper;
import me.demo.rpc.netty.sdc.SDCHelper;
import me.demo.rpc.netty.sdc.SDCResourceChangeListener;

public class RPCClient {
    //    static Logger logger = Logger.getLogger(RPCClient.class, "biss", "rpc");
    static ConcurrentHashMap<String, RPCConnectionPoolConfig> poolMap = new ConcurrentHashMap<String, RPCConnectionPoolConfig>();

    static class RPCConnectionPoolConfig {
        String name;
        int idle = 60000;
        int dataTimeout = 180000;
        int connectionTimeout = 30000;
        int connectionsPerServer = 1;
        RPCTcpConnectionPool pool;
        SDCResourceChangeListener listener;


        public RPCConnectionPoolConfig(String name, JSONObject el) throws IOException {
            this.name = name;
            idle = (int) el.getOrDefault("idle", 60) * 1000;
            dataTimeout = (int) el.getOrDefault("datatimeout", 180) * 1000;
            connectionTimeout = (int) el.getOrDefault("connection_timeout", 30) * 1000;
            connectionsPerServer = (int) el.getOrDefault("connections_per_server", connectionsPerServer);
            pool = new RPCTcpConnectionPool(name, idle, connectionTimeout, dataTimeout);
            addListener();
        }

        void addListener() throws IOException {
            listener = (action, key, isDir, value) -> {
//                logger.debug("action: {} , key: {}, dir: {}, value: {}", action, key, isDir, value);
                try {
                    if (action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("expire")) {
                        pool.removeConnections(parseKey(key));
                    } else if (action.equalsIgnoreCase("set") || action.equalsIgnoreCase("update")) {
                        pool.addConnections(parseKey(key), connectionsPerServer);
                    }
                } catch (Throwable e) {
                }
            };
            SDCHelper.addResourceChangeListener("rpcservice/" + name, listener);
        }

        InetSocketAddress parseKey(String key) {
            int index = key.lastIndexOf("/");
            if (index > 0)
                key = key.substring(index + 1);
            String[] s = StringHelper.splitToStringArray(key, ":");
            return new InetSocketAddress(s[0].trim(), Integer.valueOf(s[1].trim()));
        }

        void addConnections(String key) {
            pool.addConnections(parseKey(key), connectionsPerServer);
        }
    }

    static public <T> T lookup(String serverName, Object caller, String serviceName, Class<T> serviceCls)
            throws RPCException {
        RPCConnectionPoolConfig config = poolMap.get(serverName);
        if (config == null) {
            config = getSDCPool(serverName);
        }
        if (config == null)
            throw new RPCException("not config[" + serverName + "]");
        return config.pool.getRemoteService(serviceName, serviceCls);
    }


    synchronized static RPCConnectionPoolConfig getSDCPool(String name) throws RPCException {
        try {
            Map<String, String> map = SDCHelper.gets("rpcservice/" + name, false);
            RPCConnectionPoolConfig pool = null;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (pool == null) {
                    pool = new RPCConnectionPoolConfig(name, JSON.parseObject(entry.getValue()));
                    poolMap.put(name, pool);
                }
                pool.addConnections(entry.getKey());
            }
            return pool;
        } catch (IOException e) {
            throw new RPCException(e);
        } catch (JSONException e) {
            throw new RPCException(e);
        }
    }


    static {
        try {
            init();
        } catch (Throwable e) {
//            logger.error("init rpc client error: ", e);
        }
    }

    /**
     * 装入配置并初始化
     */
    public static void init() throws IOException {
    }

    private RPCClient() {
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++)
            new Thread(() -> {
                try {
//                    final TestBeanRemote remote = lookup("db", null,
//                            "udcredit-biss-services/test",
//                            TestBeanRemote.class);
//                    for (int i1 = 0; i1 < 10000; i1++) {
//                        remote.delete(0L);
//                        // remote.delete(1L);
//                        // List<?> ls = remote.query("asdfasdf");
//                        // for (Object o : ls) {
//                        // System.out.println(o);
//                        // }
//                    }
                } catch (Exception e) {
//                        logger.error("error:", e);
                }
            }).start();
    }
}
