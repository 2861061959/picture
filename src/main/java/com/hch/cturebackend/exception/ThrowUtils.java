package com.hch.cturebackend.exception;


import com.hch.cturebackend.enums.ErrorCode;

public class ThrowUtils {


  /**
   * 条件成立抛出异常
   *
   * @param condition 条件
   * @param exception 异常
   */
  public static void throwIf(boolean condition, RuntimeException exception) {
    if (condition) {
      throw exception;
    }
  }

  /**
   * 条件成立抛出异常
   *
   * @param condition 条件
   * @param errorCode 异常信息
   */
  public static void throwIf(boolean condition, ErrorCode errorCode) {
    throwIf(condition, new BusinessException(errorCode));
  }

  /**
   * 条件成立抛出异常
   *
   * @param condition 条件
   * @param errorCode 异常状态码
   * @param message 异常信息
   */
  public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
    throwIf(condition, new BusinessException(errorCode.getCode(), message));
  }
}
