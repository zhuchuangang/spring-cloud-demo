package com.szss.demo.orders.command;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import com.szss.demo.orders.service.UserService;
import com.szss.demo.orders.vo.UserVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zcg on 2017/7/5.
 */
public class UserCommandCollapser extends HystrixCollapser<List<UserVO>, UserVO, Long> {

    private UserService userService;
    private final Long userId;

    public UserCommandCollapser(UserService userService, Long userId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("UserCommandCollapser"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.userService = userService;
        this.userId = userId;
    }

    @Override
    public Long getRequestArgument() {
        return userId;
    }

    @Override
    protected HystrixCommand<List<UserVO>> createCommand(Collection<CollapsedRequest<UserVO, Long>> collapsedRequests) {
        List<Long> userIds = new ArrayList<>(collapsedRequests.size());
        userIds.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        return new UserBatchCommand(userService, userIds);
    }

    @Override
    protected void mapResponseToRequests(List<UserVO> batchResponse, Collection<CollapsedRequest<UserVO, Long>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<UserVO, Long> collapsedRequest : collapsedRequests) {
            UserVO user = batchResponse.get(count++);
            collapsedRequest.setResponse(user);
        }
    }
}
