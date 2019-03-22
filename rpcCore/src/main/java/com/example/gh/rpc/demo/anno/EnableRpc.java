package com.example.gh.rpc.demo.anno;

import com.example.gh.rpc.demo.config.RpcAutoConfiguration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : gaohui
 * @Date : 2019/3/22 15:50
 * @Description :
 */


@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcAutoConfiguration.class)
public @interface EnableRpc {
}
