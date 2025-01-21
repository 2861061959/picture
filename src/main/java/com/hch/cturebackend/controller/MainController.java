package com.hch.cturebackend.controller;


import com.hch.cturebackend.common.BaseResponse;
import com.hch.cturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

  @GetMapping
  public BaseResponse<String> health(){
    return ResultUtils.success("ok");
  }
}
