package com.hch.cturebackend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

  /**
   * 当前页号
   */
  private int current = 1;


  /**
   * 页面大小
   */
  private int pageSize = 10;

  /**
   * 排序字段
   */
  private String sortField;

  /**
   * 默认降序排序
   */
  private String sortOrder = "descend";
}
