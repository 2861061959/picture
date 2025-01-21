package com.hch.cturebackend.exception;

import com.hch.cturebackend.common.BaseResponse;
import com.hch.cturebackend.common.ResultUtils;
import com.hch.cturebackend.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = BusinessException.class)
  public BaseResponse<?> businessException(BusinessException e) {
    log.error("BusinessException", e);
    return ResultUtils.error(e.getCode(), e.getMessage());
  }

  @ExceptionHandler(value = RuntimeException.class)
  public BaseResponse<?> runtimeExceptionHandler(RuntimeException runtimeException) {
    log.error("runtimeException", runtimeException);
    return ResultUtils.error(ErrorCode.SYSTEM_ERROR, runtimeException.getMessage());
  }
}
