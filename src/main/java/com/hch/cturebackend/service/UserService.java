package com.hch.cturebackend.service;

import com.hch.cturebackend.model.entity.User;
import com.hch.cturebackend.model.vo.LoginUserVO;
import com.hch.cturebackend.model.vo.UserVO;
import com.mybatisflex.core.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author sanhe
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-01-22 17:47:33
 */
public interface UserService extends IService<User> {

  /**
   * 用户注册
   *
   * @param userAccount 用户账号
   * @param userPassword 用户密码
   * @param checkPassword 确认密码
   * @return 新用户id
   */
  long userRegister(String userAccount, String userPassword, String checkPassword);


  /**
   * 用户登录
   *
   * @param userAccount 登录账号
   * @param userPassword 登录密码
   * @param request http的request对象
   * @return 脱敏后的用户数据
   */
  LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

  /**
   *  获取用户信息
   * @param user 用户信息
   * @return 用户信息
   */
  LoginUserVO getLoginUserVO(User user);


  /**
   * 加密密码
   *
   * @param password 密码
   * @return 加密后的密码
   */
  String getEncryptPassword(String password);

  /**
   * 获取用户信息
   *
   * @param request http的request对象
   * @return 脱敏后的用户信息
   */
  User getLoginUser(HttpServletRequest request);

  /**
   * 用户注销
   *
   * @param request httpRequest对象
   * @return 成功失败
   */
  boolean userLogout(HttpServletRequest request);

  /**
   * 将User转成userVO
   *
   * @param user 用户数据
   * @return userVO
   */
  UserVO getUserVO(User user);

  /**
   * 获取多个用户VO
   *
   * @param userList 用户
   * @return List<UserVO>
   */
  List<UserVO> getUserVOList(List<User> userList);


  /**
   * 是否为管理员
   *
   * @param user
   * @return
   */
  boolean isAdmin(User user);

}
