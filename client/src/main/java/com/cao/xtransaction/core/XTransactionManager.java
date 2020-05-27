package com.cao.xtransaction.core;

import com.alibaba.fastjson.JSONObject;
import com.cao.xtransaction.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class XTransactionManager {

    private static NettyClient nettyClient;

    private static ThreadLocal<XTransaction> current = new ThreadLocal();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal();

    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        XTransactionManager.nettyClient = nettyClient;
    }

    public static Map<String, XTransaction> LB_TRANSACION_MAP = new HashMap();

    /**
     * 创建事务组，并且返回groupId
     * @return
     */
    public static String createXTransactionGroup() {
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", groupId);
        jsonObject.put("cmd", "create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组");

        currentGroupId.set(groupId);
        return groupId;
    }

    /**
     * 创建分布式事务
     * @param groupId
     * @return
     */
    public static XTransaction createXTransaction(String groupId) {
        String transactionId = UUID.randomUUID().toString();
        XTransaction XTransaction = new XTransaction(groupId, transactionId);
        LB_TRANSACION_MAP.put(groupId, XTransaction);
        current.set(XTransaction);
        addTransactionCount();

        System.out.println("创建事务");

        return XTransaction;
    }

    /**
     * 添加事务到事务组
     * @param XTransaction
     * @param isEnd
     * @param transactionType
     * @return
     */
    public static XTransaction addXTransaction(XTransaction XTransaction, Boolean isEnd, TransactionType transactionType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", XTransaction.getGroupId());
        jsonObject.put("transactionId", XTransaction.getTransactionId());
        jsonObject.put("transactionType", transactionType);
        jsonObject.put("cmd", "add");
        jsonObject.put("isEnd", isEnd);
        jsonObject.put("transactionCount", XTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务");
        return XTransaction;
    }

    public static XTransaction debugXTransaction(XTransaction XTransaction, Boolean isEnd, TransactionType transactionType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", XTransaction.getGroupId());
        jsonObject.put("transactionId", XTransaction.getTransactionId());
        jsonObject.put("transactionType", transactionType);
        jsonObject.put("cmd", "debug");
        jsonObject.put("isEnd", isEnd);
        jsonObject.put("transactionCount", XTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务");
        return XTransaction;
    }


    public static XTransaction getXTransaction(String groupId) {
        return LB_TRANSACION_MAP.get(groupId);
    }

    public static XTransaction getCurrent() {
        return current.get();
    }
    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }

    public static void setTransactionCount(int i) {
        transactionCount.set(i);
    }

    public static Integer addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
        return i;
    }
}
