package com.hch.cturebackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hch.cturebackend.annotation.AuthCheck;
import com.hch.cturebackend.common.BaseResponse;
import com.hch.cturebackend.common.DeleteRequest;
import com.hch.cturebackend.common.ResultUtils;
import com.hch.cturebackend.constant.CommonConstant;
import com.hch.cturebackend.enums.ErrorCode;
import com.hch.cturebackend.exception.BusinessException;
import com.hch.cturebackend.exception.ThrowUtils;
import com.hch.cturebackend.model.dto.user.UserAddRequest;
import com.hch.cturebackend.model.dto.user.UserLoginRequest;
import com.hch.cturebackend.model.dto.user.UserRegisterRequest;
import com.hch.cturebackend.model.dto.user.UserUpdateRequest;
import com.hch.cturebackend.model.entity.User;
import com.hch.cturebackend.model.vo.LoginUserVO;
import com.hch.cturebackend.model.vo.UserVO;
import com.hch.cturebackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hch.cturebackend.constant.CommonConstant.DEFAULT_PASSWORD;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 用户注册
   *
   * @param registerRequest 注册请求参数
   * @return 新用户Id
   */
  @PostMapping("/register")
  public BaseResponse<Long> userRegister(
    @RequestBody
    @NotNull(message = "输入参数不能为空")
    UserRegisterRequest registerRequest
  ) {
    ThrowUtils.throwIf(registerRequest == null, ErrorCode.PARAMS_ERROR);
    String userAccount = registerRequest.getUserAccount();
    String userPassword = registerRequest.getUserPassword();
    String checkPassword = registerRequest.getCheckPassword();
    long id = userService.userRegister(userAccount, userPassword, checkPassword);
    return ResultUtils.success(id);
  }

  @GetMapping("/get/login")
  public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
    LoginUserVO loginUser = userService.getLoginUser(request);
    return ResultUtils.success(loginUser);
  }

  @PostMapping("/login")
  public BaseResponse<LoginUserVO> userLogin(
    @RequestBody
    @NotNull(message = "参数不能为空")
    UserLoginRequest userLoginRequest,
    HttpServletRequest request
  ) {
    String userAccount = userLoginRequest.getUserAccount();
    String userPassword = userLoginRequest.getUserPassword();
    LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
    return ResultUtils.success(loginUserVO);
  }

  @GetMapping("/logout")
  public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
    ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
    boolean result = userService.userLogout(request);
    return ResultUtils.success(result);
  }

  /**
   * 创建用户
   *
   * @param userAddRequest 用户数据
   * @return 返回
   */
  @PostMapping("/add")
  public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
    ThrowUtils.throwIf(
      userAddRequest == null
      , new BusinessException(ErrorCode.NOT_FOUND_ERROR)
    );
    User user = new User();
    BeanUtil.copyProperties(userAddRequest, user);
    String password = userService.getEncryptPassword(DEFAULT_PASSWORD);
    user.setUserPassword(password);
    boolean result = userService.save(user);
    ThrowUtils.throwIf(result, ErrorCode.OPERATION_ERROR);
    return ResultUtils.success(user.getId());
  }

  /**
   * 根据id获取用户
   *
   * @param id 用户id
   * @return 用户数据
   */
  @GetMapping("/get")
  @AuthCheck(mustRole = CommonConstant.DEFAULT_ADMIN)
  public BaseResponse<User> getUserById(Long id) {
    ThrowUtils.throwIf(id <= 0, new BusinessException(ErrorCode.PARAMS_ERROR));
    User user = userService.getById(id);
    ThrowUtils.throwIf(user == null, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
    return ResultUtils.success(user);
  }

  /**
   * 根据id获取包装类
   *
   * @param id 用户id
   * @return 用户数据
   */
  @GetMapping("/get/vo")
  @AuthCheck(mustRole = CommonConstant.DEFAULT_ADMIN)
  public BaseResponse<UserVO> getUserVOById(Long id) {
    BaseResponse<User> response = getUserById(id);
    User user = response.getData();
    UserVO userVO = new UserVO();
    BeanUtil.copyProperties(user, userVO);
    return ResultUtils.success(userVO);
  }

  @PostMapping("/delete")
  @AuthCheck(mustRole = CommonConstant.DEFAULT_ADMIN)
  public BaseResponse<Boolean> deleteUserById(DeleteRequest deleteRequest) {
    ThrowUtils.throwIf(
      deleteRequest == null
      , new BusinessException(ErrorCode.NOT_FOUND_ERROR)
    );
    boolean result = userService.removeById(deleteRequest.getId());
    ThrowUtils.throwIf(result, ErrorCode.SYSTEM_ERROR);
    return ResultUtils.success(result);
  }


  @PostMapping("/update")
  @AuthCheck(mustRole = CommonConstant.DEFAULT_ADMIN)
  public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest updateRequest) {
    if (updateRequest == null || updateRequest.getId() == null) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    User user = new User();
    BeanUtil.copyProperties(updateRequest, user);
    boolean result = userService.updateById(user);
    ThrowUtils.throwIf(result, new BusinessException(ErrorCode.OPERATION_ERROR));
    return ResultUtils.success(result);
  }


}
