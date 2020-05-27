package com.cao.xtransaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

//@Component
//@Aspect
public class TimeMonitor {
    @Around("execution(* com.cao.xtransaction.IService.test(..))")
    public void monitor(ProceedingJoinPoint point) throws Throwable{
        System.out.println("开始偷梁换柱");
        point.proceed();
    }
}
