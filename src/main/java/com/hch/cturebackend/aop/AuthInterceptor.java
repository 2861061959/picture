package com.hch.cturebackend.aop;

import com.hch.cturebackend.annotation.AuthCheck;
import com.hch.cturebackend.enums.ErrorCode;
import com.hch.cturebackend.exception.BusinessException;
import com.hch.cturebackend.model.entity.User;
import com.hch.cturebackend.model.enums.UserRoleEnum;
import com.hch.cturebackend.model.vo.LoginUserVO;
import com.hch.cturebackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthInterceptor {

  private final UserService userService;

  @Around("@annotation(authcheck)")
  public Object doInterceptor(ProceedingJoinPoint pjp, AuthCheck authCheck) throws Throwable {
    String mustRole = authCheck.mustRole();
    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

    User loginUser = userService.getLoginUser(request);
    UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
    // 不需要权限放行
    if(mustRoleEnum == null){
      return pjp.proceed();
    }
    // 需要权限
    UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
    // 无权限
    if(userRoleEnum == null){
      throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
    }

    if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
      throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
    }
    // 权限通过
    return pjp.proceed();
  }
}
