package com.szss.demo.orders.rest;

import com.szss.demo.orders.command.UserCommandCollapser;
import com.szss.demo.orders.service.UserService;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by zcg on 2017/7/5.
 */
@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserService userService;


    @GetMapping("/user")
    public UserVO findById(@RequestParam("id") Long id) {
        userService.findById(id);
        userService.findById(id);
        return userService.findById(id);
    }


    @PostMapping("/user")
    public String updateUser(UserVO user) {
        userService.updateUser(user);
        return "SUCCESS";
    }

    @GetMapping("/user2")
    public UserVO findById2(@RequestParam("id") Long id) {
        userService.findById2(id);
        userService.findById2(id);
        return userService.findById2(id);
    }

    @GetMapping("/user/{ids}")
    public List<UserVO> findByIds(@PathVariable("ids") String ids) throws ExecutionException, InterruptedException {
        List<UserVO> list = new ArrayList<>();
        List<Future> futures = new ArrayList<>();
        if (ids != null) {
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {
                Future<UserVO> future = new UserCommandCollapser(userService, Long.parseLong(idArray[i])).queue();
                futures.add(future);
            }
        }
        for (Future<UserVO> future : futures) {
            list.add(future.get());
        }
        return list;
    }

    @GetMapping("/user2/{ids}")
    public List<UserVO> findByIds2(@PathVariable("ids") String ids) throws ExecutionException, InterruptedException {
        List<UserVO> list = new ArrayList<>();
        List<Future> futures = new ArrayList<>();
        if (ids != null) {
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {
                Future<UserVO> future = userService.find(Long.parseLong(idArray[i]));
                futures.add(future);
            }
        }
        for (Future<UserVO> future : futures) {
            list.add(future.get());
        }
        return list;
    }

}
