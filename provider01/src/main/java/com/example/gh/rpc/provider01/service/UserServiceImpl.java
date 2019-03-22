package com.example.gh.rpc.provider01.service;

import com.example.gh.api.IUserService;
import com.example.gh.pojo.User;
import com.example.gh.rpc.demo.anno.RpcService;

/**
 * @author : gaohui
 * @Date : 2019/3/22 16:25
 * @Description :
 */


@RpcService(IUserService.class)
public class UserServiceImpl implements IUserService {

    int count;

    @Override
    public User getById(Long id) {

        User user = new User();
        user.setId(id);
        user.setName("jack : " + count++);
        System.out.println("userServiceImpl  getById id = " + id);
        return user;
    }
}
