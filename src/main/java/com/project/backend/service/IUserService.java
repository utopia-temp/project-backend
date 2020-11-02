package com.project.backend.service;

import com.project.backend.common.ServerResponse;
import com.project.backend.pojo.User;

/**
 * @author Utopia
 */
public interface IUserService {

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);
}
