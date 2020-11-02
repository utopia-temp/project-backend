package com.project.backend.dao;

import com.project.backend.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 查询的用户名匹配行数量
     */
    int checkUsername(String username);

    /**
     * 检查用户名、密码与数据库是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 查询后的用户
     */
    User selectUser(@Param("username") String username, @Param("password") String password);
}