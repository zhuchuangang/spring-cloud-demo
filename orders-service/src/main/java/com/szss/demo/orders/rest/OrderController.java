package com.szss.demo.orders.rest;

import com.szss.demo.orders.service.OrderService;
import com.szss.demo.orders.vo.OrderVO;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Single;

import java.util.concurrent.ExecutionException;

/**
 * Created by zcg on 2017/6/23.
 */
@RestController
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * HystrixCommand的同步实现
     *
     * @param username
     * @return
     */
    @GetMapping("/order-sync-command/{username}")
    public Single<OrderVO> findByUsernameSyncCommand(
            @PathVariable("username") String username) {
        LOGGER.info("The OrderController's findByUsernameSyncCommand method is accessed,param username is {}，thread is {}", username, Thread.currentThread().getName());
        return Single.just(orderService.findByUsernameSyncCommand(username));
    }


    /**
     * HystrixCommand的异步实现
     *
     * @param username
     * @return
     */
    @GetMapping("/order-async-command/{username}")
    public Single<OrderVO> findByUsernameAsyncCommand(
            @PathVariable("username") String username) {
        LOGGER.info("The OrderController's findByUsernameAsyncCommand method is accessed,param username is {}，thread is {}", username, Thread.currentThread().getName());
        return Single.just(orderService.findByUsernameAsyncCommand(username));
    }


    /**
     * \@HystrixCommand标注的同步实现
     *
     * @param username
     * @return
     */
    @GetMapping("/order-sync/{username}")
    public Single<OrderVO> findByUsernameSync(
            @PathVariable("username") String username) {
        LOGGER.info("The OrderController's findByUsernameSync method is accessed,param username is {}，thread is {}", username, Thread.currentThread().getName());
        return Single.just(orderService.findByUsernameSync(username));
    }

    /**
     * \@HystrixCommand标注的异步实现
     *
     * @param username
     * @return
     */
    @GetMapping("/order-async/{username}")
    public OrderVO findByUsernameAsync(
            @PathVariable("username") String username) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameAsync method is accessed,param username is {}，thread is {}", username, Thread.currentThread().getName());
        return orderService.findByUsernameAsync(username).get();
    }


    /**
     * 继承HystrixObservableCommand的实现的HotObservable
     *
     * @param username
     * @return
     */
    @GetMapping("/order-hot-observable-command/{username}")
    public OrderVO findByUsernameHotObservableCommand(
            @PathVariable("username") String username) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameHotObservableCommand method is accessed,param username is {}，thread is {}", username, Thread.currentThread().getName());
        return orderService.findByUsernameHotObservableCommand(username);
    }


    /**
     * 继承HystrixObservableCommand的实现的ColdObservable
     *
     * @param username
     * @return
     */
    @GetMapping("/order-cold-observable-command/{username}")
    public OrderVO findByUsernameColdObservableCommand(
            @PathVariable("username") String username) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameColdObservableCommand method is accessed,param username is {}，thread is {}", username, Thread.currentThread().getName());
        return orderService.findByUsernameColdObservableCommand(username);
    }


    /**
     * \@HystrixCommand标注的HotObservable
     *
     * @param username
     * @return
     */
    @GetMapping("/order-eager/{username}")
    public OrderVO findByUsernameEager(
            @PathVariable("username") String username) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameEager method is accessed,param username is {},thread is {}", username, Thread.currentThread().getName());
        return orderService.findByUsernameEager(username);
    }

    /**
     * \@HystrixCommand标注的ColdObservable
     *
     * @param username
     * @return
     */
    @GetMapping("/order-lazy/{username}")
    public OrderVO findByUsernameLazy(
            @PathVariable("username") String username) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameLazy method is accessed,param username is {},thread is {}", username, Thread.currentThread().getName());
        return orderService.findByUsernameLazy(username);
    }


    @GetMapping("/order-cache/{username}")
    public OrderVO findByUsernameCache(
            @PathVariable("username") String username) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameCache method is accessed,param username is {},thread is {}", username, Thread.currentThread().getName());
        orderService.findByUsernameCacheCommand(username);
        return orderService.findByUsernameCacheCommand(username);
    }


    @PostMapping(value = "/order-update-cache", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public OrderVO findByUsernameUpdateCache(UserVO user) throws ExecutionException, InterruptedException {
        LOGGER.info("The OrderController's findByUsernameUpdateCache method is accessed,param username is {},thread is {}", user.getUsername(), Thread.currentThread().getName());
        return orderService.updateCacheCommand(user);
    }

}
