package com.hch.cturebackend.common;


import com.hch.cturebackend.enums.ErrorCode;

public class ResultUtils {

  /**
   * 成功
   *
   * @param data 数据
   * @param <T>  类型
   */
  public static <T> BaseResponse<T> success(T data) {
    return new BaseResponse<>(0, data, "ok");
  }

  /**
   * 失败
   *
   * @param errorCode 失败枚举
   * @return 响应
   */
  public static BaseResponse<?> error(ErrorCode errorCode) {
    return new BaseResponse<>(errorCode);
  }

  /**
   * 失败
   *
   * @param code    状态码
   * @param message 失败信息
   * @return 响应
   */
  public static BaseResponse<?> error(int code, String message) {
    return new BaseResponse<>(code, null, message);
  }

  /**
   * 失败
   *
   * @param errorCode 失败枚举
   * @param message 失败信息
   * @return 响应
   */
  public static BaseResponse<?> error(ErrorCode errorCode, String message){
    return new BaseResponse<>(errorCode.getCode(),null,message);
  }

}
