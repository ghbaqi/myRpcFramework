package com.example.gh.rpc.demo.core;


import com.example.gh.rpc.demo.coder.RpcDecoder;
import com.example.gh.rpc.demo.coder.RpcEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : gaohui
 * @Date : 2019/3/19 19:59
 * @Description :
 */

@Slf4j
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private String host;
    private int    port;
    private final Object obj = new Object();
    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, RpcResponse response) throws Exception {

        this.response = response;
        synchronized (obj) {
            obj.notifyAll();
        }
    }

    public RpcResponse send(RpcRequest request) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();


        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(RpcClient.this);

                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("client 连接成功 server = {} , port = {}", host, port);


            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                obj.wait();
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }

            return response;

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }


}


































