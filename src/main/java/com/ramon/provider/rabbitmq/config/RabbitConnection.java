package com.ramon.provider.rabbitmq.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.concurrent.ExecutorService;

/**
 *
 * @author onamaya
 */
public class RabbitConnection {

    protected RabbitConfig rabbitConfig;
    protected String owner;
    protected Connection conn;
    protected Channel channel;
    protected String consumerTag;
    protected long sendPacketCount = 0;
    protected long failedSendPacketCount = 0;
    protected long recvPacketCount = 0;
    protected ExecutorService executorService;

    public RabbitConfig getRabbitConfig() {
        return rabbitConfig;
    }

    public void setRabbitConfig(RabbitConfig rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public long getSendPacketCount() {
        return sendPacketCount;
    }

    public synchronized void incSendPacketCount() {
        sendPacketCount++;
    }

    public long getFailedSendPacketCount() {
        return failedSendPacketCount;
    }

    public synchronized void incFailedSendPacketCount() {
        failedSendPacketCount++;
    }

    public long getRecvPacketCount() {
        return recvPacketCount;
    }

    public synchronized void incRecvPacketCount() {
        recvPacketCount++;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
