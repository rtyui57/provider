package com.ramon.provider.rabbitmq.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 *
 * @author onamaya
 */
public class RabbitConfig {

    protected String host = "localhost";
    protected Integer port = 5672;
    protected int restPort = 15672;
    protected String user = "user";
    protected String pass = "pass";
    protected String exchange;
    protected String exchangeDlx;
    protected String queue;
    protected String queueDlx;
    protected String routingKey;
    protected String routingKeyDlx;
    protected int channelPrefetch = 0;
    protected boolean autoAck = true;
    protected String consumerTag;
    protected List<String> additionalRoutingKeys;
    protected boolean enabled = true;
    protected boolean durable = true;
    protected boolean exclusive = false;
    protected boolean singleActiveConsumer = false;
    protected boolean autoDelete = false;
    protected Long maxFailRetries = -1L;
    protected long threadIdleTimeout = 60;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public int getRestPort() {
        return restPort;
    }

    public void setRestPort(int restPort) {
        this.restPort = restPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getQueueDlx() {
        return queueDlx;
    }

    public void setQueueDlx(String queueDlx) {
        this.queueDlx = queueDlx;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getExchangeDlx() {
        return exchangeDlx;
    }

    public void setExchangeDlx(String exchangeDlx) {
        this.exchangeDlx = exchangeDlx;
    }

    public String getRoutingKeyDlx() {
        return routingKeyDlx;
    }

    public void setRoutingKeyDlx(String routingKeyDlx) {
        this.routingKeyDlx = routingKeyDlx;
    }

    public Integer getChannelPrefetch() {
        return channelPrefetch;
    }

    public void setChannelPrefetch(Integer channelPrefetch) {
        this.channelPrefetch = channelPrefetch;
    }

    public boolean isAutoAck() {
        return autoAck;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public List<String> getAdditionalRoutingKeys() {
        return additionalRoutingKeys;
    }

    public void setAdditionalRoutingKeys(List<String> additionalRoutingKeys) {
        this.additionalRoutingKeys = additionalRoutingKeys;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    @JsonIgnore
    public String getEndpoint() {
        return host + ":" + port;
    }

    @JsonIgnore
    public String getRestEndpoint() {
        return host + ":" + restPort;
    }

    public Long getMaxFailRetries() {
        return maxFailRetries;
    }

    public void setMaxFailRetries(Long maxFailRetries) {
        this.maxFailRetries = maxFailRetries;
    }

    public boolean isSingleActiveConsumer() {
        return singleActiveConsumer;
    }

    public void setSingleActiveConsumer(boolean singleActiveConsumer) {
        this.singleActiveConsumer = singleActiveConsumer;
    }

    public long getThreadIdleTimeout() {
        return threadIdleTimeout;
    }

    public void setThreadIdleTimeout(long threadIdleTimeout) {
        this.threadIdleTimeout = threadIdleTimeout;
    }
}
