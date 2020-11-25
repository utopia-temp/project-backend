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

    /**
     * 根据用户名重置用户密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param forgetToken 重置密码的token
     * @return 服务器响应对象
     */
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * 登录状态修改密码
     *
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param user        用户对象
     * @return 服务器响应对象
     */
    ServerResponse<String> resetPasswordLogin(String passwordOld, String passwordNew, User user);

    /**
     * 登录状态更新用户信息
     *
     * @param user 用户对象
     * @return 服务响应对象
     */
    ServerResponse<User> updateInformation(User user);

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 服务响应对象
     */
    ServerResponse<User> getInformation(Integer userId);

    /**
     * 判断用户是否是管理员
     *
     * @param user 待判断的用户
     * @return 服务响应对象
     */
    ServerResponse<String> checkAdminRole(User user);
}
