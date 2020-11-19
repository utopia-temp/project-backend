package com.project.backend.controller.admin;

import com.project.backend.common.Const;
import com.project.backend.common.ServerResponse;
import com.project.backend.pojo.User;
import com.project.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author Utopia
 */
@Controller
//后台的请求路径，统一前缀为manage/user
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登录方法
     * 复用IUserService的login方法
     *
     * @param username 用户名
     * @param password 用户密码
     * @param session  用户会话
     * @return 服务器响应对象
     */
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            //判断用户角色是否为管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("登录用户非管理员");
            }
        }
        return response;
    }
}
