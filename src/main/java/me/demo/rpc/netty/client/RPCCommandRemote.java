package me.demo.rpc.netty.client;


import com.alibaba.fastjson.JSONObject;

import me.demo.common.exceptions.RPCException;


/**
 * 通用RPC命令接口，用于RPC解耦<br>
 * 由于是命令协议形式，服务提供者需要公开command,params,返回值的具体协议文档

 * @version 4.0
 */

public interface RPCCommandRemote {
	/**
	 * 调用RPC远程命令
	 * @param command 命令字
	 * @param params 参数
	 * @return 调用结果
	 */
	JSONObject call(String command, JSONObject params) throws RPCException;
}
