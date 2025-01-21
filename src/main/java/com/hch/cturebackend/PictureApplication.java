package com.hch.cturebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class PictureApplication {
  public static void main(String[] args) {
    SpringApplication.run(PictureApplication.class,args);
  }
}