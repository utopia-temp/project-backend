package com.project.backend.service.impl;

import com.project.backend.common.Const;
import com.project.backend.common.ServerResponse;
import com.project.backend.dao.UserMapper;
import com.project.backend.pojo.User;
import com.project.backend.service.IUserService;
import com.project.backend.util.MD5Util;
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

        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectUser(username, md5Password);
        //校验完用户名后检查密码是否正确
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //校验成功将用户密码置空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> register(User user) {
        //复用checkValid()方法检查用户名是否存在
        ServerResponse<String> checkedResponse = checkValid(user.getUsername(), Const.InputType.USERNAME);
        if (!checkedResponse.isSuccess()) {
            return checkedResponse;
        }
        //复用checkValid()方法检查email是否存在
        checkedResponse = checkValid(user.getEmail(), Const.InputType.EMAIL);
        if (!checkedResponse.isSuccess()) {
            return checkedResponse;
        }

        //设置用户吗默认角色
        user.setRole(Const.Role.ROLE_CUSTOMER);

        //密码MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        //向数据库中插入该用户
        int count = userMapper.insert(user);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 检查用户名、email是否存在
     *
     * @param input 输入数据
     * @param type  待判断类型
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> checkValid(String input, String type) {
        //待判断类型不为Blank
        if (StringUtils.isNotBlank(type)) {
            //开始校验

            //判断email是否存在
            if (Const.InputType.EMAIL.equals(input)) {
                int count = userMapper.checkEmail(input);
                if (count > 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
            //判断用户名是否存在
            if (Const.InputType.USERNAME.equals(input)) {
                int count = userMapper.checkUsername(input);
                if (count > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验通过");
    }
}
