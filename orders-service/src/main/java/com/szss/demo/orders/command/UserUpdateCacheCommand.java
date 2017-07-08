package com.szss.demo.orders.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zcg on 2017/6/28.
 */
public class UserUpdateCacheCommand extends HystrixCommand<UserVO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdateCacheCommand.class);

    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("CommandKey");
    private RestTemplate restTemplate;
    private UserVO user;

    public UserUpdateCacheCommand(RestTemplate restTemplate, UserVO user) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("userUpdateCacheCommand")));
        this.restTemplate = restTemplate;
        this.user = user;
    }

    @Override
    protected UserVO run() throws Exception {
        LOGGER.info("thread:" + Thread.currentThread().getName());
        HttpEntity<UserVO> u = new HttpEntity<UserVO>(user);
        UserVO userVO=restTemplate.postForObject("http://users-service/user",u,UserVO.class);
        UserCacheCommand.flushCache(user.getUsername());
        return userVO;
    }

//    @Override
//    protected UserVO getFallback() {
//        UserVO user = new UserVO();
//        user.setId(-1L);
//        user.setUsername("调用失败");
//        return user;
//    }

    @Override
    protected String getCacheKey() {
        return user.getUsername();
    }
}
