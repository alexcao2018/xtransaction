package com.cao.xtransaction.aspect;


import com.cao.xtransaction.annotation.XTransactional;
import com.cao.xtransaction.core.TransactionType;
import com.cao.xtransaction.core.XTransaction;
import com.cao.xtransaction.core.XTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class XTransactionAspect implements Ordered {


    @Around("@annotation(com.cao.xtransaction.annotation.XTransactional)")
    public void invoke(ProceedingJoinPoint point) {
        // 打印出这个注解所对应的方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        XTransactional lbAnnotation = method.getAnnotation(XTransactional.class);

        String groupId = "";
        if (lbAnnotation.isStart()) {
            groupId = XTransactionManager.createXTransactionGroup();
        } else {
            groupId = XTransactionManager.getCurrentGroupId();
        }

        XTransaction lbTransaction = XTransactionManager.createXTransaction(groupId);

        try {
            // spring会开启mysql事务
            point.proceed();
            XTransactionManager.addXTransaction(lbTransaction, lbAnnotation.isEnd(), TransactionType.commit);
        } catch (Exception e) {
            XTransactionManager.addXTransaction(lbTransaction, lbAnnotation.isEnd(), TransactionType.rollback);
            e.printStackTrace();
        } catch (Throwable throwable) {
            XTransactionManager.addXTransaction(lbTransaction, lbAnnotation.isEnd(), TransactionType.rollback);
            throwable.printStackTrace();
        }
    }


    @Override
    public int getOrder() {
        return 10000;
    }
}
