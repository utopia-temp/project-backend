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
    //映射URL为login.do， 方法为POST
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }
}
