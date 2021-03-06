package com.project.backend.controller.portal;

import com.project.backend.common.Const;
import com.project.backend.common.ResponseCode;
import com.project.backend.common.ServerResponse;
import com.project.backend.pojo.User;
import com.project.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Utopia
 */
@Controller
//门户的请求路径，统一前缀为user
@RequestMapping(path = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @param session  用户登录会话
     * @return 服务器响应对象
     */
    //映射URL为login.do，方法为POST
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    /**
     * 用户登出
     *
     * @param session 用户会话
     * @return 服务器响应对象
     */
    //映射URL为logout.do，方法为POST
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return 服务器响应对象
     */
    //映射URL为register.do，方法为POST
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 检查用户名、密码是否存在，防止直接调用接口传参
     *
     * @param input 输入数据
     * @param type  待判断类型
     * @return 服务器响应对象
     */
    //映射URL为check_valid.do，方法为POST
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String input, String type) {
        return iUserService.checkValid(input, type);
    }

    /**
     * 获取用户信息
     *
     * @param session 用户会话
     * @return 服务器响应对象 - 用户信息
     */
    //映射URL为get_user_info.do，方法为POST
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }

        return ServerResponse.createByErrorMessage("用户信息获取失败，用户未登录");
    }

    /**
     * 获取用户密码提示问题
     *
     * @param username 用户名
     * @return 服务器响应对象
     */
    //映射URL为forget_pwd_question.do，方法为POST
    @RequestMapping(value = "forget_pwd_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPwdQuestion(String username) {
        return iUserService.forgetPwdQuestion(username);
    }

    /**
     * 用户密码提示问题答案校验
     *
     * @param username 用户名
     * @param question 密码提示问题
     * @param answer   密码提示问题答案
     * @return 服务器响应对象 - Token
     */
    //映射URL为forget_pwd_question_answer.do，方法为POST
    @RequestMapping(value = "forget_pwd_question_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPwdQuestionAnswer(String username, String question, String answer) {
        return iUserService.forgetPwdQuestionAnswer(username, question, answer);
    }

    /**
     * 通过用户名重置用户密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param forgetToken 重置密码的token
     * @return 服务器响应对象
     */
    //映射URL为forget_reset_password.do，方法为POST
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 登录状态修改密码
     *
     * @param session     用户会话
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return 服务器响应对象
     */
    //映射URL为reset_password_login.do，方法为POST
    @RequestMapping(value = "reset_password_login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPasswordLogin(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return iUserService.resetPasswordLogin(passwordOld, passwordNew, user);
    }

    /**
     * 登录状态更新用户信息
     *
     * @param session 用户会话
     * @param user    用户对象
     * @return 服务器响应对象
     */
    //映射URL为update_information.do，方法为POST
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        //设置用户的id不能被修改
        user.setId(currentUser.getId());

        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            //设置用户名不能被修改，保证返回时username的正确性
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }


    /**
     * 获取用户信息，如用户未登录则强制登录
     *
     * @param session 用户会话
     * @return 服务器相应对象
     */
    //映射URL为get_information.do，方法为POST
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "当前用户未登录，需要强制登录");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
