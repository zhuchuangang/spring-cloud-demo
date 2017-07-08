package com.szss.demo.orders.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zcg on 2017/6/26.
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void controllerAnnotationPointcut() {
    }

    @Before("controllerAnnotationPointcut()")
    public void controllerAnnotationPointcutBefore(JoinPoint joinPoint) {
        LOGGER.info("=============aop before===============");
    }

    @After("controllerAnnotationPointcut()")
    public void controllerAnnotationPointcutAfter(JoinPoint joinPoint) {
        LOGGER.info("=============aop after=============");
    }

    @Around("controllerAnnotationPointcut()")
    public Object controllerAnnotationPointcutAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.info("=============aop around=============");
        Object retVal = joinPoint.proceed();
        return retVal;
    }

    @AfterReturning(value = "controllerAnnotationPointcut()", returning = "retVal")
    public void controllerAnnotationPointcutAfterReturning(JoinPoint joinPoint, Object retVal) {
        LOGGER.info("=============aop AfterReturning=============");
    }

    @AfterThrowing(value = "controllerAnnotationPointcut()", throwing = "ex")
    public void controllerAnnotationPointcutAfterThrowing(JoinPoint joinPoint, Exception ex) {
        LOGGER.info("=============aop AfterThrowing=============");
    }
}
