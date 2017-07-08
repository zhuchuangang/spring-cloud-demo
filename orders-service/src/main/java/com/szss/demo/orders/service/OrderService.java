package com.szss.demo.orders.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.szss.demo.orders.command.UserCacheCommand;
import com.szss.demo.orders.command.UserCommand;
import com.szss.demo.orders.command.UserObservableCommand;
import com.szss.demo.orders.command.UserUpdateCacheCommand;
import com.szss.demo.orders.entity.OrderEntity;
import com.szss.demo.orders.vo.OrderVO;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by zcg on 2017/6/26.
 */
@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private static final List<OrderEntity> ORDERS;

    static {
        ORDERS = new ArrayList<>();
        ORDERS.add(new OrderEntity(1L, "20170601", 1L));
        ORDERS.add(new OrderEntity(2L, "20170602", 2L));
        ORDERS.add(new OrderEntity(3L, "20170603", 3L));
        ORDERS.add(new OrderEntity(4L, "20170604", 4L));
        ORDERS.add(new OrderEntity(5L, "20170605", 5L));
    }

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;


    /**
     * HystrixCommand的同步实现
     *
     * @param username
     * @return
     */
    public OrderVO findByUsernameSyncCommand(String username) {
        UserVO user = new UserCommand(restTemplate, username).execute();
        LOGGER.info("thread:"+Thread.currentThread().getName());
        if (user != null) {
            Long userId = user.getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user);
            order.setOrders(orders);
            return order;
        }
        return null;
    }

    /**
     * HystrixCommand的异步实现
     *
     * @param username
     * @return
     */
    public OrderVO findByUsernameAsyncCommand(String username) {
        Future<UserVO> future = new UserCommand(restTemplate, username).queue();
        LOGGER.info("thread:"+Thread.currentThread().getName());
        UserVO user = null;
        try {
            user = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (user != null) {
            Long userId = user.getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user);
            order.setOrders(orders);
            return order;
        }
        return null;
    }


    @HystrixCommand(fallbackMethod = "findByUsernameFallback", groupKey = "order")
    public OrderVO findByUsernameSync(String username) {
        ResponseEntity<UserVO> user = restTemplate
                .getForEntity("http://users-service/user/name/{username}", UserVO.class, username);
        LOGGER.info("thread:"+Thread.currentThread().getName()+"  "+user.toString());
        if (user.getBody() != null) {
            Long userId = user.getBody().getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user.getBody());
            order.setOrders(orders);
            return order;
        }
        return null;
    }


    @HystrixCommand(fallbackMethod = "findByUsernameFallback", groupKey = "order",
            commandProperties = {@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")})
    public Future<OrderVO> findByUsernameAsync(String username) {
        return new AsyncResult<OrderVO>() {

            @Override
            public OrderVO invoke() {
                ResponseEntity<UserVO> user = restTemplate
                        .getForEntity("http://users-service/user/name/{username}", UserVO.class, username);
                LOGGER.info("thread:"+Thread.currentThread().getName()+"  "+user.toString());
                if (user.getBody() != null) {
                    Long userId = user.getBody().getId();
                    List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                            .collect(Collectors.toList());
                    OrderVO order = new OrderVO();
                    order.setUser(user.getBody());
                    order.setOrders(orders);
                    return order;
                }
                return null;
            }
        };

    }


    public OrderVO findByUsernameHotObservableCommand(String username) {
        Observable<UserVO> hotObservable = new UserObservableCommand(restTemplate, username).observe();
        UserVO user = hotObservable.toBlocking().single();
        if (user != null) {
            Long userId = user.getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user);
            order.setOrders(orders);
            return order;
        }
        return null;
    }


    public OrderVO findByUsernameColdObservableCommand(String username) {
        Observable<UserVO> coldObservable = new UserObservableCommand(restTemplate, username).toObservable();
        UserVO user = coldObservable.toBlocking().single();
        if (user != null) {
            Long userId = user.getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user);
            order.setOrders(orders);
            return order;
        }
        return null;
    }


    @HystrixCommand(fallbackMethod = "findByUsernameFallback", groupKey = "order", observableExecutionMode = ObservableExecutionMode.EAGER)
    public OrderVO findByUsernameEager(String username) {
        LOGGER.info("thread:"+Thread.currentThread().getName());
        ResponseEntity<UserVO> user = restTemplate
                .getForEntity("http://users-service/user/name/{username}", UserVO.class, username);
        LOGGER.info(user.toString());
        if (user.getBody() != null) {
            Long userId = user.getBody().getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user.getBody());
            order.setOrders(orders);
            return order;
        }
        return null;
    }

    @HystrixCommand(fallbackMethod = "findByUsernameFallback", groupKey = "order",threadPoolKey ="orderThreadPool" ,observableExecutionMode = ObservableExecutionMode.LAZY)
    public OrderVO findByUsernameLazy(String username) {
        LOGGER.info("thread:"+Thread.currentThread().getName());
        ResponseEntity<UserVO> user = restTemplate
                .getForEntity("http://users-service/user/name/{username}", UserVO.class, username);
        LOGGER.info(user.toString());
        if (user.getBody() != null) {
            Long userId = user.getBody().getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user.getBody());
            order.setOrders(orders);
            return order;
        }
        return null;
    }

    /**
     * 缓存
     *
     * @param username
     * @return
     */
    public OrderVO findByUsernameCacheCommand(String username) {
        UserCacheCommand command=new UserCacheCommand(restTemplate, username);
        UserVO user = command.execute();

        LOGGER.info("thread:"+Thread.currentThread().getName()+" from cache is "+command.isResponseFromCache());
        if (user != null) {
            Long userId = user.getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(user);
            order.setOrders(orders);
            return order;
        }
        return null;
    }


    public OrderVO updateCacheCommand(UserVO user) {
        UserUpdateCacheCommand command=new UserUpdateCacheCommand(restTemplate, user);
        UserVO temp = command.execute();

        LOGGER.info("thread:"+Thread.currentThread().getName()+" from cache is "+command.isResponseFromCache());
        if (user != null) {
            Long userId = temp.getId();
            List orders = ORDERS.stream().filter(o -> userId == o.getUserId())
                    .collect(Collectors.toList());
            OrderVO order = new OrderVO();
            order.setUser(temp);
            order.setOrders(orders);
            return order;
        }
        return null;
    }



    public OrderVO findByUsernameFallback(String username, Throwable e) {
        OrderVO order = new OrderVO();
        order.setSuccess(false);
        order.setMessage(e.getMessage());
        return order;
    }
}
