package com.ymatou.mq.rabbit.support;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import com.rabbitmq.client.impl.recovery.AutorecoveringChannel;
import com.ymatou.mq.rabbit.RabbitChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * channel wrapper，主要用于处理ack事件
 * Created by zhangzhihua on 2017/3/30.
 */
public class ChannelWrapper {

    private static final Logger logger = LoggerFactory.getLogger(ChannelWrapper.class);

    /**
     * channel
     */
    private Channel channel;

    /**
     * 未确认集合
     */
    private SortedMap<Long, Object> unconfirmedSet;

    /**
     * 当前channel所在线程
     */
    private Thread thread;

    /**
     * 当前channel所属connection wrapper
     */
    private ConnectionWrapper connectionWrapper;

    public ChannelWrapper(Channel channel){
        this.channel = channel;
    }

    public void addRecoveryListener(){
        if(this.getChannel() instanceof AutorecoveringChannel){
            AutorecoveringChannel recoverChannel = (AutorecoveringChannel)this.getChannel();
            recoverChannel.addRecoveryListener(new RecoveryListener() {
                @Override
                public void handleRecovery(Recoverable recoverable) {
                    logger.warn("channel handleRecovery,recoverable:{}",recoverable);
                    if(unconfirmedSet != null){
                        unconfirmedSet.clear();
                    }
                }

                @Override
                public void handleRecoveryStarted(Recoverable recoverable) {
                    logger.warn("channel handleRecoveryStarted,recoverable:{}",recoverable);
                }
            });
        }
    }

    public ChannelWrapper(Channel channel,SortedMap<Long, Object> unconfirmedSet){
        this.channel = channel;
        this.unconfirmedSet = unconfirmedSet;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public SortedMap<Long, Object> getUnconfirmedSet() {
        return unconfirmedSet;
    }

    public void setUnconfirmedSet(SortedMap<Long, Object> unconfirmedSet) {
        this.unconfirmedSet = unconfirmedSet;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public ConnectionWrapper getConnectionWrapper() {
        return connectionWrapper;
    }

    public void setConnectionWrapper(ConnectionWrapper connectionWrapper) {
        this.connectionWrapper = connectionWrapper;
    }
}
