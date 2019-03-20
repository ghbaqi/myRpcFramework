package com.example.gh.rpc.demo.core;

import java.lang.reflect.Method;
import java.util.Map;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : gaohui
 * @Date : 2019/3/17 18:52
 * @Description :
 */
@Slf4j
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, RpcRequest request) throws Exception {

        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        Object result = handle(request);

        response.setResult(result);

        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    private Object handle(RpcRequest request) throws Exception {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        Class<?> serviceBeanClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        //        FastClass serviceFastClass = FastClass.create(serviceBeanClass);
        //        FastMethod method = serviceFastClass.getMethod(methodName, parameterTypes);
        //        Object res = method.invoke(serviceBean, parameters);

        Method method = serviceBeanClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object res = method.invoke(serviceBean, parameters);

        return res;
    }
}
