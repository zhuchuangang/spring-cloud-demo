package com.szss.demo.orders.command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.szss.demo.orders.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zcg on 2017/6/28.
 */
public class UserObservableCommand extends HystrixObservableCommand<UserVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserObservableCommand.class);
    private RestTemplate restTemplate;
    private String username;

    public UserObservableCommand(RestTemplate restTemplate, String username) {
        super(HystrixCommandGroupKey.Factory.asKey("userObservable"));
        this.restTemplate = restTemplate;
        this.username = username;
    }

    @Override
    protected Observable<UserVO> construct() {
        return Observable.create(new Observable.OnSubscribe<UserVO>() {
            @Override
            public void call(Subscriber<? super UserVO> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        LOGGER.info("thread:"+Thread.currentThread().getName());
                        UserVO user = restTemplate.getForObject("http://users-service/user/name/{username}", UserVO.class, username);
                        subscriber.onNext(user);
                        subscriber.onCompleted();
                    }catch (Exception e){
                        subscriber.onError(e);
                    }
                }
            }
        });
    }

    @Override
    protected Observable<UserVO> resumeWithFallback() {
        UserVO user=new UserVO();
        user.setId(-1L);
        user.setUsername("调用失败");
        return Observable.just(user);
    }
}
