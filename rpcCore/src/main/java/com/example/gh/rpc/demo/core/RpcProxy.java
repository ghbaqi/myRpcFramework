package com.example.gh.rpc.demo.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : gaohui
 * @Date : 2019/3/19 19:45
 * @Description :
 */

@Slf4j
//@Component
public class RpcProxy {

    //    @Value("${registry.address}")
    private String serverAddress;

    //    @Autowired
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serverAddress, ServiceDiscovery serviceDiscovery) {
        this.serverAddress = serverAddress;
        this.serviceDiscovery = serviceDiscovery;
        log.info("RpcProxy 初始化");
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        T proxyInstance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        // 服务发现
                        //                        if (serviceDiscovery != null) {
                        serverAddress = serviceDiscovery.discover();
                        //                        }

                        if (serverAddress == null || serverAddress.equals("")) {
                            throw new RuntimeException("没有发现服务提供者");
                        }

                        String[] arr = serverAddress.split(":");
                        RpcClient rpcClient = new RpcClient(arr[0], Integer.parseInt(arr[1]));
                        RpcResponse response = rpcClient.send(request);

                        //                        if (response.isError()) {
                        //                            throw
                        //                        }

                        return response.getResult();
                    }
                }
        );
        log.info("创建代理对象");
        return proxyInstance;
    }


}
