package com.ramon.provider.rabbitmq.manager;

import com.rabbitmq.client.*;
import com.ramon.provider.rabbitmq.config.RabbitConfig;
import com.ramon.provider.rabbitmq.config.RabbitConnection;
import com.ramon.provider.rabbitmq.config.RabbitMessage;
import com.ramon.provider.rabbitmq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RabbitManager {

    protected static final Logger logger = LoggerFactory.getLogger(RabbitManager.class);
    private static final String DEFAULT_EXCHANGE = "";
    private static final String DEFAULT_DELIVERY_TAG = "defaultDeliveryTag";
    private static final String DLX_EXCHANGE = "x-dead-letter-exchange";
    private static final String DLX_ROUTING_KEY = "x-dead-letter-routing-key";
    private static final String SINGLE_ACTIVE_CONSUMER = "x-single-active-consumer";
    private static final String BINDED_QUEUE_MESSAGE = "Binded queue: ''{}'', exchange: ''{}'', routingkey: ''{}''";
    private final Map<String, Channel> mConsumerTagChannel = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> queueThreadCount = new HashMap<>();

    public void createResources(RabbitConfig config) {
        RabbitConnection rConn = newRabbitConnection(config);
        try {
            if (!Objects.isNull(config.getExchange())) {
                createExchange(rConn, config);
            }

            if (!Objects.isNull(config.getExchangeDlx())) {
                createExchangeDlx(rConn, config);
            }

            if (!Objects.isNull(config.getQueue())) {
                queueDeclare(rConn, config);
                rConn.getChannel().queueBind(config.getQueue(), config.getExchange(), config.getRoutingKey());
            }

            if (!Objects.isNull(config.getQueueDlx())) {
                queueDeclareDlx(rConn, config);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error creating Rabbit Resources", ex);
        } finally {
            freeRabbitConnection(rConn);
        }
    }

    public RabbitConnection newRabbitConnection(RabbitConfig config) {
        RabbitConnection rConn = new RabbitConnection();
        rConn.setRabbitConfig(config);

        if (ObjectUtils.isEmpty(config.getConsumerTag())) {
            config.setConsumerTag(DEFAULT_DELIVERY_TAG);
        }
        rConn.setConsumerTag(config.getConsumerTag());

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(config.getUser());
        factory.setPassword(config.getPass());
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());

        CustomizableThreadFactory threadFactory = new CustomizableThreadFactory();
        threadFactory.setThreadGroupName("RabbitMQ");
        threadFactory.setDaemon(true);
        String prefix = getQueueThreadPrefix(config);
        threadFactory.setThreadNamePrefix(prefix);
        factory.setThreadFactory(threadFactory);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, config.getThreadIdleTimeout(), TimeUnit.SECONDS, new LinkedBlockingQueue<>(), threadFactory);
        executorService.allowCoreThreadTimeOut(true);
        factory.setSharedExecutor(executorService);
        rConn.setExecutorService(executorService);

        Connection conn = null;
        Channel channel = null;

        try {
            conn = factory.newConnection();
            logger.trace("Rabbit Connection created at {}:{}. Id : {}", config.getHost(), config.getPort(), conn.getId());
            rConn.setConn(conn);

            channel = conn.createChannel();
            logger.trace("Rabbit Channel created. Number : {}", channel.getChannelNumber());
            rConn.setChannel(channel);

        } catch (Exception ex) {
            String errMsg = "Error creating Rabbit Connection";
            logger.error(errMsg, ex);
            closeChannel(channel);
            closeConnection(conn);
            if (rConn.getExecutorService() != null) {
                rConn.getExecutorService().shutdown();
            }
            throw new RuntimeException(errMsg, ex);
        }

        return rConn;
    }

    private synchronized String getQueueThreadPrefix(RabbitConfig config) {
        String queueName = config.getQueue();
        if (ObjectUtils.isEmpty(queueName)) {
            queueName = config.getExchange();
        }
        if (queueThreadCount.containsKey(queueName)) {
            queueThreadCount.get(queueName).incrementAndGet();
        } else {
            queueThreadCount.put(queueName, new AtomicInteger(0));
        }
        return "Rabbit-" + queueName + "-" + queueThreadCount.get(queueName) + "-";
    }

    public void createExchange(RabbitConfig config) throws IOException {
        final RabbitConnection rConn = newRabbitConnection(config);

        try {
            createExchange(rConn, config);
        } finally {
            freeRabbitConnection(rConn);
        }
    }

    public void createExchange(RabbitConnection rConn, RabbitConfig config) throws IOException {
        rConn.getChannel().exchangeDeclare(config.getExchange(), BuiltinExchangeType.TOPIC, true);
        logger.debug("Rabbit Exchange : ''{}'' of type : ''{}'' created at {}:{}", config.getExchange(), BuiltinExchangeType.TOPIC.name(), config.getHost(), config.getPort());

    }

    public void createExchangeDlx(RabbitConnection rConn, RabbitConfig config) throws IOException {
        if (!ObjectUtils.isEmpty(config.getExchangeDlx())) {
            BuiltinExchangeType exchangeType = (Objects.isNull(config.getRoutingKeyDlx())) ? BuiltinExchangeType.FANOUT : BuiltinExchangeType.DIRECT;
            rConn.getChannel().exchangeDeclare(config.getExchangeDlx(), exchangeType);
            logger.debug("Rabbit Exchange DLX : ''{}'' of type : ''{}'' created at {}:{}", config.getExchangeDlx(), BuiltinExchangeType.FANOUT.name(), config.getHost(), config.getPort());
        }
    }

    private AMQP.Queue.DeclareOk queueDeclare(RabbitConnection rConn, RabbitConfig config) throws IOException {
        Map<String, Object> map = new HashMap<>();
        if (!ObjectUtils.isEmpty(config.getExchangeDlx())) {
            map.put(DLX_EXCHANGE, config.getExchangeDlx());
        }
        if (!ObjectUtils.isEmpty(config.getRoutingKeyDlx())) {
            map.put(DLX_ROUTING_KEY, config.getRoutingKeyDlx());
        }
        if (config.isSingleActiveConsumer()) {
            map.put(SINGLE_ACTIVE_CONSUMER, true);
        }

        AMQP.Queue.DeclareOk queue = rConn.getChannel().queueDeclare(config.getQueue(), config.isDurable(), config.isExclusive(), config.isAutoDelete(), map);
        logger.trace("Declared Rabbit Queue: ''{}'', consumers: {}, messages: {}", queue.getQueue(), queue.getConsumerCount(), queue.getMessageCount());
        return queue;
    }

    public AMQP.Queue.DeclareOk queueDeclareDlx(RabbitConnection rConn, RabbitConfig config) throws IOException {
        AMQP.Queue.DeclareOk queueDlx = null;
        if (!ObjectUtils.isEmpty(config.getQueueDlx())) {
            queueDlx = rConn.getChannel().queueDeclare(config.getQueueDlx(), config.isDurable(), config.isExclusive(), config.isAutoDelete(), null);
            logger.trace("Declared Rabbit DLX Queue: ''{}'', consumers: {}, messages: {}", queueDlx.getQueue(), queueDlx.getConsumerCount(), queueDlx.getMessageCount());
        }
        return queueDlx;
    }

    public void queueDelete(RabbitConnection rConn, RabbitConfig config) {
        try {
            logger.debug("Deleting Queue : {}", config.getQueue());
            rConn.getChannel().queueDelete(config.getQueue());
        } catch (IOException ex1) {
            throw new RuntimeException(MessageFormat.format("Error Deleting Queue : {0}", config.getQueue()), ex1);
        }
    }

    public void queueDeleteDlx(RabbitConnection rConn, RabbitConfig config)  {
        if (!Objects.isNull(config.getQueueDlx())) {
            try {
                logger.debug("Deleting DLX Queue : {}", config.getQueue());
                rConn.getChannel().queueDelete(config.getQueueDlx());
            } catch (IOException ex1) {
                throw new RuntimeException(MessageFormat.format("Error Deleting DLX Queue : {0}", config.getQueue()), ex1);
            }
        }
    }


    public RabbitConnection newRecvConnection(RabbitConfig config, MessageConsumer consumer) {
        return newRecvConnectionCommonBody(config, consumer);
    }

    private RabbitConnection newRecvConnectionCommonBody(RabbitConfig config, MessageConsumer consumer) {
        final RabbitConnection rConn = newRabbitConnection(config);
        rConn.setOwner(consumer.getOwner());

        try {
            createExchange(rConn, config);
            createExchangeDlx(rConn, config);

            queueDeclare(rConn, config);
            rConn.getChannel().queueBind(config.getQueue(), config.getExchange(), config.getRoutingKey());
            logger.trace(BINDED_QUEUE_MESSAGE, config.getQueue(), config.getExchange(), config.getRoutingKey());

            if (!Objects.isNull(config.getQueueDlx())) {
                queueDeclareDlx(rConn, config);
                rConn.getChannel().queueBind(config.getQueueDlx(), config.getExchangeDlx(), config.getRoutingKeyDlx());
                logger.trace(BINDED_QUEUE_MESSAGE, config.getQueueDlx(), config.getExchangeDlx(), config.getRoutingKeyDlx());
            }

            if (!ObjectUtils.isEmpty(config.getAdditionalRoutingKeys())) {
                for (String additionalRoutingKey : config.getAdditionalRoutingKeys()) {
                    rConn.getChannel().queueBind(config.getQueue(), config.getExchange(), additionalRoutingKey);
                    logger.trace(BINDED_QUEUE_MESSAGE, config.getQueue(), config.getExchange(), config.getRoutingKey());
                }
            }

            if (!config.isAutoAck()) {
                synchronized (mConsumerTagChannel) {
                    if (mConsumerTagChannel.containsKey(rConn.getConsumerTag())) {
                        throw new RuntimeException(MessageFormat.format("Another Manual-Ack Connection currently uses same ConsumerTag : {0}", rConn.getConsumerTag()));
                    }
                    mConsumerTagChannel.put(rConn.getConsumerTag(), rConn.getChannel());
                    logger.trace("Added Manual-Ack ConsumerTag : {} to Channel Number : {}", rConn.getConsumerTag(), rConn.getChannel().getChannelNumber());
                }
            }

            rConn.getChannel().basicQos(config.getChannelPrefetch());
            rConn.getChannel().basicConsume(config.getQueue(), false, rConn.getConsumerTag(), new DefaultConsumer(rConn.getChannel()) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    RabbitMessage msg = new RabbitMessage();
                    try {
                        msg.setExchange(envelope.getExchange());
                        msg.setRoutingKey(envelope.getRoutingKey());
                        msg.setConsumerTag(consumerTag);
                        msg.setDeliveryTag(envelope.getDeliveryTag());
                        msg.setBody(body);
                        processReDelivery(msg, envelope, properties);

                        receiveMessage(rConn, msg, consumer);
                    } catch (Exception e) {
                        logger.error("Error procesing message from queue: ''{}''", config.getQueue(), e);
                    }
                }
            });
        } catch (Exception e) {
            freeRabbitConnection(rConn);
            throw new RuntimeException(e);
        }
        return rConn;
    }

    private void processReDelivery(RabbitMessage msg, Envelope envelope, AMQP.BasicProperties properties) {
        if (!Objects.isNull(properties.getHeaders())) {
            List<Map> lXDeaths = (List) properties.getHeaders().get("x-death");
            if (!ObjectUtils.isEmpty(lXDeaths)) {
                msg.setRedelivered(true);

                Map<String, Object> mXDeath = lXDeaths.get(0);

                String exchange = (String) mXDeath.get("exchange").toString();
                if (!Objects.isNull(exchange)) {
                    msg.setExchange(exchange);
                }

                List lRoutingKeys = (List) mXDeath.get("routing-keys");
                if (!ObjectUtils.isEmpty(lRoutingKeys)) {
                    msg.setRoutingKey(lRoutingKeys.get(0).toString());
                }

                Long failCount = (Long) mXDeath.get("count");
                if (!Objects.isNull(failCount)) {
                    msg.setFailCount(failCount);
                }
                String reason = (String) mXDeath.get("reason").toString();
                if (!Objects.isNull(reason)) {
                    msg.setFailReason(reason);
                }
                logger.info("Received re-delivered message. x-death parameters => exchange : {}, routing-key : {}, failCount : {}, reason : {}", msg.getExchange(), msg.getRoutingKey(), msg.getFailCount(), msg.getFailReason());
            }
        }
    }

    public RabbitConnection newSendConnection(RabbitConfig config, String owner) {
        RabbitConnection rConn = newRabbitConnection(config);
        rConn.setOwner(owner);
        return rConn;
    }

    public void freeRabbitConnection(RabbitConnection rConn) {
        freeRabbitConnection(rConn, true);
    }

    protected void freeRabbitConnection(RabbitConnection rConn, boolean freeAutoDelete) {
        if (!rConn.getRabbitConfig().isAutoAck()) {
            synchronized (mConsumerTagChannel) {
                Channel ch = mConsumerTagChannel.get(rConn.getConsumerTag());
                if (rConn.getChannel() == ch) {
                    mConsumerTagChannel.remove(rConn.getConsumerTag());
                    logger.trace("Removed Manual-Ack ConsumerTag : {} to Channel Number : {}", rConn.getConsumerTag(), rConn.getChannel().getChannelNumber());
                }
            }
        }

        if (freeAutoDelete) {
            freeAutoDeleteQueue(rConn);
        }

        closeChannel(rConn.getChannel());
        closeConnection(rConn.getConn());
        if (rConn.getExecutorService() != null) {
            rConn.getExecutorService().shutdown();
        }
        logger.debug("Released Rabbit Connection");
    }

    protected void closeChannel(Channel channel) {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException | TimeoutException t) {
            logger.error("Error closing Rabbit channel", t);
        }

    }

    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException t) {
                logger.error("Error closing Rabbit Connection", t);
            }
        }
    }

    protected void freeAutoDeleteQueue(RabbitConnection rConn) {
        RabbitConfig rConfig = rConn.getRabbitConfig();
        if (rConfig.isAutoDelete()) {
            try {
                if (!Objects.isNull(rConfig.getQueue())
                        && existsQueue(rConfig, rConfig.getQueue())
                        && rConn.getChannel().consumerCount(rConfig.getQueue()) == 0) {

                    logger.warn("Freeing AUTO-DELETE Queue : {} without consumers. Manually Delete", rConfig.getQueue());
                    try {
                        queueDelete(rConn, rConfig);
                    } catch (Exception ex) {
                        logger.error("Error deleting Queue : {}", rConfig.getQueue(), ex);
                    }

                    if (!Objects.isNull(rConfig.getQueueDlx())
                            && existsQueue(rConfig, rConfig.getQueueDlx())) {
                        try {
                            logger.warn("Freeing AUTO-DELETE DLX Queue : {} without consumers. Manually Delete", rConfig.getQueue());
                            queueDeleteDlx(rConn, rConfig);
                        } catch (Exception ex) {
                            logger.error("Error deleting Queue DLX : {}", rConfig.getQueueDlx(), ex);
                        }
                    }
                }
            } catch (Exception ex) {
                logger.error("Error freeing AutoDelete Queues", ex);
            }
        }
    }

    public void receiveMessage(RabbitConnection rConn, RabbitMessage msg, MessageConsumer consumer) {
        rConn.incRecvPacketCount();
        logger.debug("Received Message. Id: {}, Owner : {}, Exchange : {}, routingKey : {}, Body Len : {},", msg.getId(), rConn.getOwner(), msg.getExchange(), msg.getRoutingKey(), msg.getLength());

        if (rConn.getRabbitConfig().getMaxFailRetries() > 0 && msg.getFailCount() >= rConn.getRabbitConfig().getMaxFailRetries()) {
            logger.info("Discarded Message Id : {}. Max Failure Retries reached. Current : {}, Max : {}", msg.getId(), msg.getFailCount(), rConn.getRabbitConfig().getMaxFailRetries());
            try {
                rConn.getChannel().basicAck(msg.getDeliveryTag(), false);
            } catch (IOException ex) {
                logger.error("Error sending ACK for Message. Id : {}, Owner : {}, DeliveryTag : {}", msg.getId(), rConn.getOwner(), msg.getDeliveryTag(), ex);
            }
            return;
        }

        try {
            consumer.consumeMessage(msg);

            if (rConn.getRabbitConfig().isAutoAck()) {
                try {
                    rConn.getChannel().basicAck(msg.getDeliveryTag(), false);
                } catch (IOException ex) {
                    logger.error("Error sending ACK for Message. Id : {}, Owner : {}, DeliveryTag : {}", msg.getId(), rConn.getOwner(), msg.getDeliveryTag(), ex);
                }
            }
        } catch (Throwable t) {
            logger.error("Error consuming Message. Id : {} Owner : {}, Exchange : {}, RoutingKey : {}, Body Len : {}", msg.getId(), rConn.getOwner(), msg.getExchange(), msg.getRoutingKey(), msg.getLength(), t);

            if (rConn.getRabbitConfig().isAutoAck()) {
                try {
                    rConn.getChannel().basicReject(msg.getDeliveryTag(), msg.isRequeueOnFail());
                } catch (IOException ex2) {
                    logger.error("Error sending REJECT for Message. Id : {}, Owner : {}, DeliveryTag : {}", msg.getId(), rConn.getOwner(), msg.getDeliveryTag(), ex2);
                }
            }
            throw t;
        }
    }

    public void manualAck(String consumerTag, List<Long> deliveryTags) {
        try {
            Channel channel = mConsumerTagChannel.get(consumerTag);
            if (channel != null) {
                for (Long deliveryTag : deliveryTags) {
                    try {
                        channel.basicAck(deliveryTag, false);
                    } catch (IOException ex) {
                        logger.warn("Error Manual-Ack ConsumerTag : {}, DeliveryTag : {}", consumerTag, deliveryTag);
                        //TODO: Parametrizar acciones. Requeue?
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Unexpected Error during Manual-ACK. ConsumerTag : {}", consumerTag, t);
        }
    }

    public void manualNack(String consumerTag, List<Long> deliveryTags, boolean requeue) {
        try {
            Channel channel = mConsumerTagChannel.get(consumerTag);
            if (channel != null) {
                for (Long deliveryTag : deliveryTags) {
                    try {
                        channel.basicNack(deliveryTag, false, requeue);
                    } catch (IOException ex) {
                        logger.warn("Error Manual-NACK ConsumerTag : {}, DeliveryTag : {}", consumerTag, deliveryTag);
                        //TODO: Parametrizar acciones. Requeue?
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Unexpected Error during Manual-NACK. ConsumerTag : {}", consumerTag, t);
        }
    }

    public void manualReject(String consumerTag, List<Long> deliveryTags, boolean requeue) {
        try {
            Channel channel = mConsumerTagChannel.get(consumerTag);
            if (channel != null) {
                for (Long deliveryTag : deliveryTags) {
                    try {
                        channel.basicReject(deliveryTag, requeue);
                    } catch (IOException ex) {
                        logger.warn("Error Manual-REJECT ConsumerTag : {}, DeliveryTag : {}", consumerTag, deliveryTag);
                        //TODO: Parametrizar acciones. Requeue?
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Unexpected Error during Manual-REJECT. ConsumerTag : {}", consumerTag, t);
        }
    }

    public void purgeQueueConnection(String owner, RabbitConfig config) {
        final RabbitConnection rConn = newRabbitConnection(config);
        rConn.setOwner(owner);

        try {
            AMQP.Queue.DeclareOk queue = queueDeclare(rConn, config);
            if (queue != null) {
                logger.trace("Declared Rabbit Dlx queue: ''{}'', consumers: {}, messages: {}", queue.getQueue(), queue.getConsumerCount(), queue.getMessageCount());
                rConn.getChannel().queuePurge(config.getQueue());
                logger.trace("Purged Dlx queue: ''{}''", config.getQueue());
            }

            if (!ObjectUtils.isEmpty(config.getQueueDlx())) {
                AMQP.Queue.DeclareOk queueDlx = queueDeclareDlx(rConn, config);
                if (queueDlx != null) {
                    logger.trace("Declared Dlx Rabbit queue: ''{}'', consumers: {}, messages: {}", queueDlx.getQueue(), queueDlx.getConsumerCount(), queueDlx.getMessageCount());
                    rConn.getChannel().queuePurge(config.getQueueDlx());
                    logger.trace("Purged Dlx queue: ''{}''", config.getQueueDlx());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            freeRabbitConnection(rConn, false);
        }
    }

    public synchronized void sendMessage(RabbitConnection rConn, RabbitConfig config, RabbitMessage msg) {
        if (rConn == null) {
            logger.error("The message with Id : {} will not be sent due to a Rabbit Connection problem. Cause: RabbitConnection is null.", msg.getId());
            return;
        }

        if (msg.isEmpty()) {
            logger.info("Ignoring empty message. Id : {}, Owner : {}", msg.getId(), rConn.getOwner());
            return;
        }

        int msgLen = msg.getLength();
        try {
            String exchange = (!ObjectUtils.isEmpty(msg.getExchange())) ? msg.getExchange() : config.getExchange();
            String routingKey = (!ObjectUtils.isEmpty(msg.getRoutingKey())) ? msg.getRoutingKey() : config.getRoutingKey();
            rConn.getChannel().basicPublish(exchange, routingKey, null, msg.getBodyAsByteArray());
            long totalTime = new Date().getTime();
            rConn.incSendPacketCount();
            logger.debug("[TIME] Rabbit Sended Message. Id : {}, Owner : {}, Msg Len : {}, Time : {} millis", msg.getId(), rConn.getOwner(), msgLen, totalTime);
        } catch (IOException ex) {
            logger.error("Error sending message to Rabbit Connection. Id : {}, Owner : {}, Body Len : {}", msg.getId(), rConn.getOwner(), msgLen, ex);
            rConn.incFailedSendPacketCount();
            logger.debug("Total Packages Send Failed : {}, Owner : {}", rConn.getFailedSendPacketCount(), rConn.getOwner());
        }
        logger.trace("Total Packages Sended : {}, Owner : {}", rConn.getSendPacketCount(), rConn.getOwner());
    }

    public int getQueueCount(RabbitConnection rConn, String queueName) {
        try {
            AMQP.Queue.DeclareOk response = rConn.getChannel().queueDeclarePassive(queueName);
            return response.getMessageCount();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public boolean existsQueue(RabbitConfig config) {
        return existsQueue(config, config.getQueue());
    }

    protected boolean existsQueue(RabbitConfig config, String queueName)  {
        RabbitConnection rConn = null;
        try {
            rConn = newRabbitConnection(config);
            try {
                rConn.getChannel().queueDeclarePassive(queueName);
                return true;
            } catch (IOException ex) {
                return false;
            }
        } finally {
            if (rConn != null) {
                freeRabbitConnection(rConn, false);
            }
        }
    }
}
