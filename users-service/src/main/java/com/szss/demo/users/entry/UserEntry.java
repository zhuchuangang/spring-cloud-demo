package com.szss.demo.users.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zcg on 2017/6/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntry implements Serializable{
  private Long id;
  private String username;
  private int age;
}
