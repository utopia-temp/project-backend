package com.project.backend.controller.admin;

import com.project.backend.common.Const;
import com.project.backend.common.ResponseCode;
import com.project.backend.common.ServerResponse;
import com.project.backend.pojo.User;
import com.project.backend.service.ICategoryService;
import com.project.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Utopia
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    /**
     * 添加商品分类
     *
     * @param session      用户会话
     * @param categoryName 商品分类名称
     * @param parentId     商品分类父类ID
     * @return 服务响应对象
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName,
                                              @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        //判断登录用户不为空
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        //判断登录用户是否拥有管理员权限
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //添加商品分类
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("用户不是管理员，无权限进行此项操作");
        }
    }

    /**
     * 更新商品分类名称
     *
     * @param session      用户会话
     * @param categoryName 商品分类名称
     * @param categoryId   商品分类ID
     * @return 服务响应对象
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session, String categoryName, Integer categoryId) {
        //判断登录用户不能为空
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        //判断登录用户是否拥有管理员权限
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //更新商品分类名称
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        } else {
            return ServerResponse.createByErrorMessage("用户不是管理员，无权限进行此项操作");
        }
    }
}
