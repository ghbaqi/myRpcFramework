package com.example.gh.rpc.demo.core;

import com.example.gh.rpc.demo.anno.RpcService;
import com.example.gh.rpc.demo.coder.RpcDecoder;
import com.example.gh.rpc.demo.coder.RpcEncoder;
import com.example.gh.rpc.demo.register.ServiceRegistry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : gaohui
 * @Date : 2019/3/17 18:26
 * @Description :
 */
//@Component
@Slf4j
public class RpcServer implements ApplicationContextAware, InitializingBean {

    //    @Value("${rpc.server.address}")
    private String serverAddress;

    //    @Autowired
    private ServiceRegistry serviceRegistry;


    public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
        System.out.println("RpcServer serverAddress =  " + serverAddress);
        System.out.println("RpcServer serviceRegistry =  " + serviceRegistry);
    }

    /**
     * key : 接口名
     * value ： 服务对象
     */
    private Map<String, Object> handlerMap = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {

        System.out.println("RpcServer  ApplicationContext " + context);

        // 获取 RpcService
        Map<String, Object> serviceBeanMap = context.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap.isEmpty()) {
            log.warn("沒有检测到 rpc service ");
            return;
        }

        for (Object service : serviceBeanMap.values()) {

            // 此处为什么要获取这个 ？  answer = 获取 service 实现类的全路径名称
            String name = service.getClass().getAnnotation(RpcService.class).value().getName();
            //            log.info("name = " + name + "service = " + service);
            handlerMap.put(name, service);
        }


    }

    @Override
    public void afterPropertiesSet() throws Exception {

        System.out.println("RpcServer afterPropertiesSet");

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        ChannelFuture future = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {

                            channel.pipeline()
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    .addLast(new RpcHandler(handlerMap));

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] array = serverAddress.split(":");
            future = bootstrap.bind(array[0], Integer.parseInt(array[1])).sync();
            log.info("rpc server start address = " + serverAddress);


            // 注册服务地址 ？
            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress);
            }

            //            future.channel().close().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            //            log.info("rpc server shut down 111");
            //            work.shutdownGracefully();
            //            boss.shutdownGracefully();
            //            log.info("rpc server shut down 222");
        }

    }
}
























