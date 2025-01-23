package com.hch.cturebackend.model.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
  USER("用户","user"),
  ADMIN("管理员","admin");

  private final String text;

  private final String value;

  UserRoleEnum(String text, String value) {
    this.text = text;
    this.value = value;
  }

  /**
   * 根据值获取枚举
   *
   * @param value 值
   * @return
   */
  public static UserRoleEnum getEnumByValue(String value){
    for (UserRoleEnum userRoleEnum : values()) {
      if(userRoleEnum.value.equals(value)){
        return userRoleEnum;
      }
    }
    return null;
  }
}
