package com.szss.demo.eureka.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zcg on 2017/6/3.
 */

@RestController
public class HelloController {

    public static final Log LOG = LogFactory.getLog(HelloController.class);

    @Autowired
    private DiscoveryClient client;

    @RequestMapping("/hello")
    public String hello() {
        LOG.info("eureka-client services list:");
        List<ServiceInstance> instances = client.getInstances("eureka-client");
        instances.forEach((i)->{
            LOG.info(i.getServiceId()+"  "+i.getHost()+":"+i.getPort());
        });
        return "Hello World";
    }
}
