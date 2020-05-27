package com.cao.xtransaction.aspect;


import com.cao.xtransaction.connection.XConnection;
import com.cao.xtransaction.core.XTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
public class XDataSourceAspect{

    /**
     * 切的是一个接口，所以所有的实现类都会被切到
     * spring肯定会调用这个方法来生成一个本地事务
     * 所以point.proceed()返回的也是一个Connection
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point) throws Throwable {
        if (XTransactionManager.getCurrent() != null) {
            return new XConnection((Connection) point.proceed(), XTransactionManager.getCurrent());
        } else {
            return (Connection) point.proceed();
        }
    }

}
