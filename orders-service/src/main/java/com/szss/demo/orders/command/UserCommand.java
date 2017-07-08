package com.szss.demo.orders.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zcg on 2017/6/28.
 */
public class UserCommand extends HystrixCommand<UserVO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCommand.class);
    private RestTemplate restTemplate;
    private String username;

    public UserCommand(RestTemplate restTemplate, String username) {
        super(HystrixCommandGroupKey.Factory.asKey("userCommand"));
        this.restTemplate = restTemplate;
        this.username = username;
    }

    @Override
    protected UserVO run() throws Exception {
        LOGGER.info("thread:"+Thread.currentThread().getName());
        return restTemplate.getForObject("http://users-service/user/name/{username}", UserVO.class, username);
    }

    @Override
    protected UserVO getFallback() {
        UserVO user= new UserVO();
        user.setId(-1L);
        user.setUsername("调用失败");
        return user;
    }
}
