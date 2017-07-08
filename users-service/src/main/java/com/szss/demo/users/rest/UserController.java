package com.szss.demo.users.rest;

import com.szss.demo.users.entry.UserEntry;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by zcg on 2017/6/23.
 */
@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final List<UserEntry> users;

    static {
        users = new ArrayList<>();
        users.add(new UserEntry(1L, "马云", 48));
        users.add(new UserEntry(2L, "马华腾", 46));
        users.add(new UserEntry(3L, "王健林", 55));
        users.add(new UserEntry(4L, "雷军", 47));
    }


    @GetMapping("/users")
    public List<UserEntry> findAll() {
        LOGGER.info("The UserController's findAll method is accessed!");
        return users;
    }

    @GetMapping("/user")
    public UserEntry findById(@RequestParam("id") Long id) {
        LOGGER.info("The UserController's findById method is accessed,param id is {}", id);
        return users.stream().filter(u -> id == u.getId()).findFirst().get();
    }

    @GetMapping("/users/{ids}")
    public List<UserEntry> findByIds(@PathVariable("ids") String ids) {
        LOGGER.info("The UserController's findByIds method is accessed,param id is {}", ids);
        if (StringUtils.isNotEmpty(ids)) {
            final List<String> idList = Arrays.asList(ids.split(","));
            return users.stream().filter(u -> idList.contains(u.getId().toString())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @GetMapping("/user/name/{useranme}")
    public UserEntry findByUsername(@PathVariable("useranme") String useranme) {
        int time = new Random().nextInt(5000);
//    try {
//      Thread.sleep(time);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
        LOGGER.info("The UserController's thread sleep {} ms,an then the UserController's findByUsername method is accessed,param username is {}",
                time, useranme);
        return users.stream().filter(u -> useranme.equals(u.getUsername())).findFirst().get();
    }


    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserEntry update(@RequestBody UserEntry user) {
        LOGGER.info("update {}", user.getUsername());
        for (UserEntry u : users) {
            if (u.getId() == user.getId()) {
                u.setAge(user.getAge());
                u.setUsername(user.getUsername());
                break;
            }
        }
        return user;
    }

}
