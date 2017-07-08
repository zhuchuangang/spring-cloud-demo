package com.szss.demo.orders.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zcg on 2017/6/23.
 */
@Data
@AllArgsConstructor
public class OrderEntity implements Serializable{
  private Long id;
  private String orderNo;
  private Long userId;
}
