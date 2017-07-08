package com.szss.demo.orders.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.szss.demo.orders.service.UserService;
import com.szss.demo.orders.vo.UserVO;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcg on 2017/7/5.
 */
public class UserBatchCommand extends HystrixCommand<List<UserVO>> {

    private UserService userService;
    private List<Long> ids;

    public UserBatchCommand(UserService userService, List<Long> ids) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserBatchCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("findAll")));
        this.userService = userService;
        this.ids = ids;
    }

    @Override
    protected List<UserVO> run() throws Exception {
        if (ids != null && !ids.isEmpty()) {
            return userService.findAll(StringUtils.join(ids.toArray(),","));
        }
        return new ArrayList<>();
    }
}
