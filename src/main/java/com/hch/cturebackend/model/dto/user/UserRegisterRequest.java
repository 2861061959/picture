package com.hch.cturebackend.model.dto.user;

import com.mybatisflex.annotation.Column;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

  /**
   * 账号
   */
  @Size(min = 4,message = "用户账号必须大于4位")
  private String userAccount;

  /**
   * 密码
   */
  @Size(min = 8, message = "用户密码必须大于8位")
  private String userPassword;

  /**
   * 确认密码
   */
  @Size(min = 8, message = "确认密码必须大于8位")
  private String checkPassword;

  @Column(ignore = false)
  private static final long serialVersionUID = 1L;
}
