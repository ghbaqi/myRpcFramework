package com.example.gh.rpc.demo.util;

/**
 * @author : gaohui
 * @Date : 2019/3/17 17:56
 * @Description :
 */
public interface Constant {

    int SESSION_TIME_OUT = 5000;


    /**
     * 踩坑 ！！！  否则会导致路径不一致， 找不到服务
     * <p>
     * 提供者创建节点的地址用 RPC_DATA_PATH
     * <p>
     * 消费者发现服务的地址用 Constant.RPC_ROOT_PATH + "/" + node
     */
    String RPC_ROOT_PATH = "/rpcRootPath";
    String RPC_DATA_PATH = RPC_ROOT_PATH + "/data";
}
