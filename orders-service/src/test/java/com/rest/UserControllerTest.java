package com.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zcg on 2017/7/5.
 */
public class UserControllerTest {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1; i++) {
            final int id = i;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    String resource = "http://127.0.0.1:8001/user2/1,2,3,4";
                    ResponseEntity responseEntity = restTemplate.getForEntity(resource, String.class);
                    String str = responseEntity.getBody().toString();
                    System.out.println(str);
                }
            });
        }
    }
}
