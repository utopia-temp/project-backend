package com.project.backend.common;

/**
 * @author Utopia
 * <p>
 * 常量类
 */
public class Const {
    //当前用户
    public static final String CURRENT_USER = "CURRENT_USER";

    /**
     * 用户角色(轻量级“枚举”)
     */
    public interface Role {
        int ROLE_CUSTOMER = 0;  //用户
        int ROLE_ADMIN = 1; //管理员
    }

    /**
     * 带判断类型(轻量级“枚举”)
     */
    public interface InputType {
        String EMAIL = "email";  //email
        String USERNAME = "username"; //用户名
    }
}
