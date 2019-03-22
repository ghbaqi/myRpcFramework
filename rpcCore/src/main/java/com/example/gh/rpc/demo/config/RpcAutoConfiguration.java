package com.example.gh.rpc.demo.config;

import com.example.gh.rpc.demo.core.RpcServer;
import com.example.gh.rpc.demo.register.ServiceRegistry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : gaohui
 * @Date : 2019/3/22 15:41
 * @Description :
 */


@Configuration
public class RpcAutoConfiguration {

    /**
     * 暴露 rpc 服务的地址
     */
    @Value("${rpc.server.address}")
    private String serverAddress;


    /**
     * zookeeper 注冊中心地址
     */
    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    @Bean
    public RpcServer rpcServer() {
        return new RpcServer(serverAddress, serviceRegistry());
    }

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistry(zookeeperAddress);
    }


//    @Bean
//    public RpcServer rpcServer() {
//        return new RpcServer();
//    }
//
//    @Bean
//    public ServiceRegistry serviceRegistry() {
//        return new ServiceRegistry();
//    }





}
