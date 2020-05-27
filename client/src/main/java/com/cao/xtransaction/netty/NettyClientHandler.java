package com.cao.xtransaction.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cao.xtransaction.core.TransactionType;
import com.cao.xtransaction.core.XTransaction;
import com.cao.xtransaction.core.XTransactionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接受数据:" + msg.toString());
        JSONObject jsonObject = JSON.parseObject((String) msg);

        String groupId = jsonObject.getString("groupId");
        String cmd = jsonObject.getString("cmd");

        System.out.println("接收cmd:" + cmd);
        // 对事务进行操作


        XTransaction XTransaction = XTransactionManager.getXTransaction(groupId);
        if (cmd.equals("rollback")) {
            XTransaction.setTransactionType(TransactionType.rollback);
        } else if (cmd.equals("commit")) {
            XTransaction.setTransactionType(TransactionType.commit);
        }
        XTransaction.getTask().signalTask();
    }

    public synchronized Object call(JSONObject data) throws Exception {
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
