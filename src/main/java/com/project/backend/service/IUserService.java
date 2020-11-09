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
     * @param username 用户名
     * @param password 密码
     * @return 服务响应对象
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return 服务响应对象
     */
    ServerResponse<String> register(User user);

    /**
     * 检查用户名、邮箱是否存在
     *
     * @param input 输入数据
     * @param type  待判断类型
     * @return 服务响应对象
     */
    ServerResponse<String> checkValid(String input, String type);

    /**
     * 获取用户密码提示问题
     *
     * @param username 用户名
     * @return 服务响应对象
     */
    ServerResponse<String> forgetPwdQuestion(String username);

    /**
     * 密码提示问题答案
     *
     * @param username 用户名
     * @param question 密码提示问题
     * @param answer   密码提示问题答案
     * @return 服务响应对象 - token
     */
    ServerResponse<String> forgetPwdQuestionAnswer(String username, String question, String answer);
}
