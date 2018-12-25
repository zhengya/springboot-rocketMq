package com.example.springbootrecketmq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootrecketmqApplicationTests {
    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Test
    public void contextLoads() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message sendMsg = new Message("DemoTopic","DemoTag","1231231".getBytes());
        //默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        System.out.println(sendResult.toString());
    }

}

