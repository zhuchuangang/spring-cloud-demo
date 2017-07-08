package com.szss.demo.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by zcg on 2017/6/23.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UsersApplication {

  public static void main(String[] args) {
    SpringApplication.run(UsersApplication.class,args);
  }

}
