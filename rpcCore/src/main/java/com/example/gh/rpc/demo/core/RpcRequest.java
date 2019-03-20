package com.example.gh.rpc.demo.core;

import lombok.Data;

/**
 * @author : gaohui
 * @Date : 2019/3/17 18:51
 * @Description :
 */

@Data
public class RpcRequest {

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}
