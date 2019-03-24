package com.example.gh.consumer.consumer01.controller;

import com.example.gh.api.IUserService;
import com.example.gh.pojo.User;
import com.example.gh.rpc.demo.core.RpcProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : gaohui
 * @Date : 2019/3/24 15:28
 * @Description :
 */

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private RpcProxy rpcProxy;

    @ResponseBody
    @RequestMapping("/get/{id}")
    public User getUser(@PathVariable("id") Long id) {
        IUserService userService = rpcProxy.create(IUserService.class);
        User user = userService.getById(id);
        return user;
    }
}
