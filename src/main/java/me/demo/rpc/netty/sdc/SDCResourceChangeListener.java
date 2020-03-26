package me.demo.rpc.netty.sdc;

public interface SDCResourceChangeListener {
    /**
     * 资源改变
     * @param action 动作：create,update,delete
     * @param key 资源路径
     * @param isDir 是否是目录
     * @param value 资源内容
     */
    public void resourceChange(String action, String key, boolean isDir, String value);
}
