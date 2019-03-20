package com.example.gh.api;


import com.example.gh.pojo.User;

/**
 * @author : gaohui
 * @Date : 2019/3/17 17:35
 * @Description :
 */
public interface IUserService {

    User getById(Long id);
}
