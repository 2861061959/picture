package com.hch.cturebackend.controller;

import cn.hutool.core.io.IoUtil;
import com.hch.cturebackend.annotation.AuthCheck;
import com.hch.cturebackend.common.BaseResponse;
import com.hch.cturebackend.common.ResultUtils;
import com.hch.cturebackend.constant.CommonConstant;
import com.hch.cturebackend.enums.ErrorCode;
import com.hch.cturebackend.exception.BusinessException;
import com.hch.cturebackend.manager.CosManager;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/")
public class MainController {

  @Resource
  private CosManager cosManager;

  @GetMapping
  public BaseResponse<String> health() {
    return ResultUtils.success("ok");
  }

  @AuthCheck(mustRole = CommonConstant.DEFAULT_ADMIN)
  @PostMapping("/test/upload")
  public BaseResponse<String> testUpload(@RequestPart("file") MultipartFile multipartFile) {
    // 上传文件
    String filename = multipartFile.getOriginalFilename();
    String filePath = String.format("/test/%s", filename);
    File file = null;
    try {
      file = File.createTempFile(filename, null);
      multipartFile.transferTo(file);
      cosManager.putObject(filePath, file);
      return ResultUtils.success(filePath);
    } catch (Exception e) {
      log.error("upload file failed,filepath = {}", filePath);
      throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
    } finally {
      if (file != null) {
        boolean delete = file.delete();
        if (!delete) {
          log.error("file delete failed,filepath = {}", filePath);
        }
      }
    }
  }

  @AuthCheck(mustRole = CommonConstant.DEFAULT_ADMIN)
  @GetMapping("/test/download")
  public void testDownload(String filepath, HttpServletResponse response) throws IOException {
    COSObjectInputStream cosObjectInputStream = null;
    try {
      COSObject cosObject = cosManager.getObject(filepath);
      cosObjectInputStream = cosObject.getObjectContent();
      byte[] bytes = IoUtil.readBytes(cosObjectInputStream);

      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=" + filepath);
      response.getOutputStream().write(bytes);
      response.getOutputStream().flush();

    } catch (IOException e) {
      log.error("下载文件失败", e);
      throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下载文件失败");
    }finally {
      if(cosObjectInputStream != null){
        cosObjectInputStream.close();
      }
    }
  }
}
