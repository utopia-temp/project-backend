package com.project.backend.service.impl;

import com.project.backend.common.ServerResponse;
import com.project.backend.dao.UserMapper;
import com.project.backend.pojo.User;
import com.project.backend.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Utopia
 */
@Service("iUserService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int count = userMapper.checkUsername(username);
        //检查用户名是否存在
        if (count == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //TODO: 密码MD5加密

        User user = userMapper.selectUser(username, password);
        //校验完用户名后检查密码是否正确
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //校验成功将用户密码置空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }
}
