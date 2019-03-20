package com.example.gh.rpc.demo.core;

import lombok.Data;

/**
 * @author : gaohui
 * @Date : 2019/3/17 18:51
 * @Description :
 */

@Data
public class RpcResponse {

    private String requestId;
    private Throwable error;
    private Object result;
}
