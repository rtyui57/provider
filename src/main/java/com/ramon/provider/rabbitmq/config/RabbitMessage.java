package com.ramon.provider.rabbitmq.config;

import com.ramon.provider.utils.JSONUtils;
import org.springframework.util.CollectionUtils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author onamaya
 */
public class RabbitMessage {

    protected static final Random RANDOM_SEED = new SecureRandom();
    protected final String id = Long.toHexString(RANDOM_SEED.nextLong());

    protected String consumerTag;
    protected long deliveryTag;
    protected String exchange;
    protected String routingKey;
    protected boolean requeueOnFail = false;
    protected Map<String, Object> body = new HashMap<>();

    protected boolean redelivered;
    protected long failCount = 0L;
    protected String failReason;

    public String getId() {
        return id;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(body);
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public long getDeliveryTag() {
        return deliveryTag;
    }

    public void setDeliveryTag(long deliveryTag) {
        this.deliveryTag = deliveryTag;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public boolean isRequeueOnFail() {
        return requeueOnFail;
    }

    public void setRequeueOnFail(boolean requeueOnFail) {
        this.requeueOnFail = requeueOnFail;
    }

    public boolean isRedelivered() {
        return redelivered;
    }

    public void setRedelivered(boolean redelivered) {
        this.redelivered = redelivered;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public int getLength() {
        return getBodyAsByteArray().length;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public byte[] getBodyAsByteArray() {
        if (!isEmpty()) {
            return JSONUtils.toByteArray(body);
        }
        return null;
    }

    public String getBodyAsString() {
        return JSONUtils.toJSON(body);
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public void setBody(byte[] body) {
        this.body = JSONUtils.toMap(body);

    }

    public void setBody(RabbitEvent body) {
        this.body = JSONUtils.toMap(JSONUtils.toJSON(body));
    }
}
