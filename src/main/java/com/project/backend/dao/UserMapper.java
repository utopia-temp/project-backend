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

    /**
     * 检查email是否存在
     *
     * @param email email
     * @return 查询的email匹配行数量
     */
    int checkEmail(String email);

    /**
     * 查询密码提示问题
     *
     * @param username 用户名
     * @return 密码提示问题
     */
    String selectQuestion(String username);

    /**
     * 判断密码提示问题答案是否正确
     *
     * @param username 用户名
     * @param question 密码提示问题
     * @param answer   问题答案
     * @return 符合条件的行数
     */
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);
}