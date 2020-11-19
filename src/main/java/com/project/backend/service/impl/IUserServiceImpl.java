package com.project.backend.service.impl;

import com.project.backend.common.Const;
import com.project.backend.common.ServerResponse;
import com.project.backend.common.TokenCache;
import com.project.backend.dao.UserMapper;
import com.project.backend.pojo.User;
import com.project.backend.service.IUserService;
import com.project.backend.util.MD5Util;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    /**
     * 获取用户密码提示问题
     *
     * @param username 用户名
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> forgetPwdQuestion(String username) {
        ServerResponse<String> response = checkValid(username, Const.InputType.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String question = userMapper.selectQuestion(username);
        //isNotBlank(" ") = false
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("用户未设置密码找回问题");
    }

    /**
     * 密码提示问题答案
     *
     * @param username 用户名
     * @param question 密码提示问题
     * @param answer   密码提示问题答案
     * @return 服务响应对象 - token
     */
    @Override
    public ServerResponse<String> forgetPwdQuestionAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //利用UUID生成token
            String forgetToken = UUID.randomUUID().toString();
            //将token设置到Guava本地缓存中
            TokenCache.set(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("密码提示问题答案错误");
    }

    /**
     * 根据用户名重置用户密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param forgetToken 重置密码的token
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //判断传回token不为空
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，需要传递token");
        }

        if (checkValid(username, Const.InputType.USERNAME).isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //获取本地token
        String localToken = TokenCache.getValue(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(localToken)) {
            return ServerResponse.createByErrorMessage("本地token无效或过期");
        }

        //对比本地token和传回的token
        if (StringUtils.equals(localToken, forgetToken)) {
            //新密码MD5加密
            String md5PasswordNew = MD5Util.MD5EncodeUtf8(passwordNew);

            int count = userMapper.updatePasswordByUsername(username, md5PasswordNew);
            if (count > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            ServerResponse.createByErrorMessage("token校验错误，请重新获取重置密码的token");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登录状态下修改密码
     *
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param user        用户对象
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<String> resetPasswordLogin(String passwordOld, String passwordNew, User user) {
        /*判断旧密码输入是否匹配，
         *传入userId进行双重认证，防止撞库破解密码
         */
        int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (count == 0) {
            return ServerResponse.createByErrorMessage("旧密码输入错误");
        }

        //设置用户新密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        //数据库更新新密码
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }

        return ServerResponse.createByErrorMessage("密码修改失败");
    }

    /**
     * 登录状态下更新用户信息
     *
     * @param user 用户对象
     * @return 服务响应对象
     */
    @Override
    public ServerResponse<User> updateInformation(User user) {
        //保证email地址未被其他用户使用
        int count = userMapper.checkEmailByUserId(user.getUsername(), user.getId());
        if (count > 0) {
            return ServerResponse.createByErrorMessage("此email已存在");
        }

        User updateUser = new User();
        //setId进行update操作，否则就没有id，会变成新增操作
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("登录状态下，用户信息更新成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("登录状态下，用户信息更新失败");
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 服务器相应对象
     */
    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User currentUser = userMapper.selectByPrimaryKey(userId);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("此用户不存在");
        }

        //将用户密码置为空
        currentUser.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(currentUser);
    }
}
