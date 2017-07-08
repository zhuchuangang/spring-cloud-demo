package com.szss.demo.orders.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.szss.demo.orders.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by zcg on 2017/7/5.
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;


    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(commandKey = "findUserById", groupKey = "UserService", threadPoolKey = "userServiceThreadPool")
    public UserVO findById(Long id) {
        ResponseEntity<UserVO> user = restTemplate.getForEntity("http://users-service/user?id={id}", UserVO.class, id);
        return user.getBody();
    }

    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }


    @CacheResult
    @HystrixCommand(commandKey = "findUserById", groupKey = "UserService", threadPoolKey = "userServiceThreadPool")
    public UserVO findById2(@CacheKey("user.id") Long id) {
        ResponseEntity<UserVO> user = restTemplate.getForEntity("http://users-service/user?id={id}", UserVO.class, id);
        return user.getBody();
    }


    @CacheRemove(commandKey = "findUserById")
    @HystrixCommand(commandKey = "updateUser",groupKey = "UserService",threadPoolKey = "userServiceThreadPool")
    public void updateUser(@CacheKey("id")UserVO user){
        restTemplate.postForObject("http://users-service/user",user,UserVO.class);
    }


    @HystrixCollapser(collapserKey = "UserHystrixCollapser", batchMethod = "findAll2", scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            collapserProperties = {@HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")})
    public Future<UserVO> find(Long id) {
        throw new RuntimeException("This method body should not be executed");
    }

    @HystrixCommand(commandKey = "findAll2")
    public List<UserVO> findAll2(List<Long> ids) {
        ParameterizedTypeReference<List<UserVO>> responseType = new ParameterizedTypeReference<List<UserVO>>() {
        };
        String idStr = StringUtils.join(ids, ",");
        ResponseEntity<List<UserVO>> user = restTemplate.exchange("http://users-service/users/{ids}", HttpMethod.GET, null, responseType, idStr);
        return user.getBody();
    }

    public List<UserVO> findAll(String ids) {
        ParameterizedTypeReference<List<UserVO>> responseType = new ParameterizedTypeReference<List<UserVO>>() {
        };
        ResponseEntity<List<UserVO>> user = restTemplate.exchange("http://users-service/users/{ids}", HttpMethod.GET, null, responseType, ids);
        return user.getBody();
    }

}
