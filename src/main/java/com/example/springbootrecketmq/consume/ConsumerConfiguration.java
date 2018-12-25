package com.example.springbootrecketmq.consume;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author zhengyateng
 * @date 2018-12-25
 */
@Configuration
public class ConsumerConfiguration {
    public static final Logger logger = LoggerFactory.getLogger(ConsumerConfiguration.class);

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Resource
    private MessageListenerConcurrently messageListenerConcurrently;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() throws Exception {
        if (StringUtils.isEmpty(namesrvAddr)) {
            throw new Exception("namesrvAddr is null !!!");
        }
        if (StringUtils.isEmpty(topics)) {
            throw new Exception("topics is null !!!");
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(messageListenerConcurrently);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        //consumer.setMessageModel(MessageModel.CLUSTERING);

        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);

        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，
             * 则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            /*String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0],topicTag[1]);
            }*/
            consumer.subscribe("DemoTopic", "*");

            consumer.start();

        } catch (Exception e) {
            // logger.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,
            // namesrvAddr,e);
            throw new Exception(e);
        }

        return consumer;
    }

}
