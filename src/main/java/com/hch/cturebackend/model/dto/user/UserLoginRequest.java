package com.hch.cturebackend.model.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

  /**
   * 用户账号
   */
  @Size(min = 4,message = "用户账号必须大于4位")
  private String userAccount;

  /**
   * 用户密码
   */
  @Size(min = 8, message = "用户密码必须大于8位")
  private String userPassword;

}
