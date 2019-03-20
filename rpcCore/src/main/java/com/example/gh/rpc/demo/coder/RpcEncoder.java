package com.example.gh.rpc.demo.coder;


import com.example.gh.rpc.demo.util.SerializationUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author : gaohui
 * @Date : 2019/3/17 18:52
 * @Description :
 * <p>
 * 编码 response
 */
public class RpcEncoder extends MessageToByteEncoder {

//    private Class<?> clazz;

//    public RpcEncoder(Class<?> clazz) {
//        this.clazz = clazz;
//    }
//
//    @Override
//    protected void encode(ChannelHandlerContext context, Object obj, ByteBuf out) throws Exception {
//
//        if (clazz.isInstance(obj)) {
//            byte[] bytes = SerializationUtil.serialize(obj);
//            out.writeInt(bytes.length);
//            out.writeBytes(bytes);
//        }
//
//    }


    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }

}
