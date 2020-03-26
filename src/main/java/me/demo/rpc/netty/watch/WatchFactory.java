package me.demo.rpc.netty.watch;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

import me.demo.rpc.netty.sdc.SDCHelper;

/**
 * 监视工厂
 *



 */
public class WatchFactory {
    //    static Logger logger = Logger.getLogger(WatchFactory.class);
    static ConcurrentHashMap<String, Watcher> watchers = new ConcurrentHashMap<String, Watcher>();

    static {
        try {
            JSONObject core = SDCHelper.getCoreConfig();
            if (core != null) {
                JSONArray w = core.getJSONArray("watchers");
                for (int i = 0; i < w.size(); i++) {
                    JSONObject j = w.getJSONObject(i);
                    Watcher c = new Watcher(j.getString("name"), j.getInteger("interval"));
                    watchers.put(j.getString("name"), c);
                    new Thread(c, "watchthread[" + j.getString("name") + "]").start();
                }
            } else {
//                logger.error("No available db connection watcher found, it may cause connection failed problem.");
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 根据名字获取守护者
     *
     * @param name 守护者名称
     * @return 守护者对象
     */
    public static Watcher getWatcher(String name) {
        return watchers.get(name);
    }
}
