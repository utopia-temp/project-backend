package com.project.backend.controller.portal;

import com.project.backend.common.Const;
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
    //映射URL为logout.do，方法为GET
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
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
    //映射URL为checkValid.do，方法为POST
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String input, String type) {
        return iUserService.checkValid(input, type);
    }

}
