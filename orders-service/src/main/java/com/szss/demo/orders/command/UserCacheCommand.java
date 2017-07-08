package com.szss.demo.orders.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zcg on 2017/6/28.
 */
public class UserCacheCommand extends HystrixCommand<UserVO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCacheCommand.class);

    private static final HystrixCommandKey GETTER_KEY= HystrixCommandKey.Factory.asKey("CommandKey");
    private RestTemplate restTemplate;
    private String username;

    public UserCacheCommand(RestTemplate restTemplate, String username) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("userCacheCommand")).andCommandKey(GETTER_KEY));
        this.restTemplate = restTemplate;
        this.username = username;
    }

    @Override
    protected UserVO run() throws Exception {
        LOGGER.info("thread:" + Thread.currentThread().getName());
        return restTemplate.getForObject("http://users-service/user/name/{username}", UserVO.class, username);
    }

    @Override
    protected UserVO getFallback() {
        UserVO user = new UserVO();
        user.setId(-1L);
        user.setUsername("调用失败");
        return user;
    }

    @Override
    protected String getCacheKey() {
        return username;
    }

    public static void flushCache(String username){
        HystrixRequestCache.getInstance(GETTER_KEY, HystrixConcurrencyStrategyDefault.getInstance()).clear(username);
    }
}
