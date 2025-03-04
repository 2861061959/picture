package com.hch.cturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.hch.cturebackend.constant.CommonConstant;
import com.hch.cturebackend.enums.ErrorCode;
import com.hch.cturebackend.exception.BusinessException;
import com.hch.cturebackend.exception.ThrowUtils;
import com.hch.cturebackend.model.entity.User;
import com.hch.cturebackend.model.enums.UserRoleEnum;
import com.hch.cturebackend.model.vo.LoginUserVO;
import com.hch.cturebackend.model.vo.UserVO;
import com.hch.cturebackend.service.UserService;
import com.hch.cturebackend.mapper.UserMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import static com.hch.cturebackend.constant.CommonConstant.SALT;
import static com.hch.cturebackend.constant.CommonConstant.USER_LOGIN_STATE;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
  implements UserService {

  private final UserMapper userMapper;

  @Override
  public long userRegister(String userAccount, String userPassword, String checkPassword) {
    if (CharSequenceUtil.hasBlank(userAccount, userPassword, checkPassword)) {
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "参数为空");
    }
    if (!userPassword.equals(checkPassword)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
    }

    long count = queryChain().eq(User::getUserAccount, userAccount).count();
    if (count > 0) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
    }
    User user = User.builder()
      .id(1L)
      .userAccount(userAccount)
      .userName("无名")
      .userPassword(getEncryptPassword(userPassword))
      .build();

    // 保存用户
    save(user);
    return user.getId();
  }


  @Override
  public LoginUserVO userLogin(
    String userAccount,
    String userPassword,
    HttpServletRequest request
  ) {
    if (CharSequenceUtil.hasBlank(userAccount, userPassword)) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
    }

    String encryptPassword = getEncryptPassword(userPassword);
    User user = queryChain()
      .eq(User::getUserAccount, userAccount)
      .eq(User::getUserPassword, encryptPassword)
      .one();
    if (user == null) {
      log.info("user login failed,user Account cannot match userPassword");
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在或账号密码错误");
    }
    // sa-token
    //    StpUtil.login(user.getId());
    //    String tokenValue = StpUtil.getTokenValue();
    request.getSession().setAttribute(USER_LOGIN_STATE, user);
    return getLoginUserVO(user);
  }

  /**
   * 将用户数据脱敏成UserVO
   *
   * @param user 用户数据
   * @return 脱敏数据
   */
  @Override
  public LoginUserVO getLoginUserVO(User user) {
    LoginUserVO loginUserVO = new LoginUserVO();
    BeanUtil.copyProperties(user, loginUserVO);
    return loginUserVO;
  }

  @Override
  public String getEncryptPassword(String password) {
    return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
  }


  @Override
  public User getLoginUser(HttpServletRequest request) {
    Object user = request.getSession().getAttribute(USER_LOGIN_STATE);
    if (user == null) {
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未登录");
    }
    LoginUserVO loginUserVO = new LoginUserVO();
    BeanUtil.copyProperties(user, loginUserVO);
    return (User)user;
  }

  @Override
  public boolean userLogout(HttpServletRequest request) {
    Object user = request.getSession().getAttribute(USER_LOGIN_STATE);
    if (user == null) {
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未登录");
    }
    request.getSession().removeAttribute(USER_LOGIN_STATE);
    return true;
  }

  @Override
  public UserVO getUserVO(User user) {
    ThrowUtils.throwIf(user == null, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
    UserVO userVO = new UserVO();
    BeanUtil.copyProperties(user, userVO);
    return userVO;
  }

  @Override
  public List<UserVO> getUserVOList(List<User> userList) {
    ThrowUtils.throwIf(
      CollUtil.isEmpty(userList),
      new BusinessException(ErrorCode.NOT_FOUND_ERROR)
    );
    return userList.stream().map(this::getUserVO).toList();
  }

  @Override
  public boolean isAdmin(User user) {
    return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
  }
}




