package com.szss.demo.orders.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zcg on 2017/6/23.
 */
@Data
public class OrderVO implements Serializable {
    private boolean success=true;
    private String message="查询成功！";
    private UserVO user;
    private List<OrderVO> orders;
}
