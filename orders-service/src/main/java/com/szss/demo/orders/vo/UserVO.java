package com.szss.demo.orders.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zcg on 2017/6/23.
 */
@Data
public class UserVO implements Serializable {
  private Long id;
  private String username;
  private int age;
}
