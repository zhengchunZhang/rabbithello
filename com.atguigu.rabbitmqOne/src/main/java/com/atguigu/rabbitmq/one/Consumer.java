package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String queue_name="hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.154.155");
        factory.setUsername("admin");

        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明 接收消息  拉普莱斯表达式
        DeliverCallback deliverCallback = (consumerTag,messagae)->{
            System.out.println(new String(messagae.getBody()));
        };
        //声明 取消消息 拉普莱斯表达式
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };
        /**
         * 消费者消费消息
         * 1.消费的队列名称
         * 2.消费成功是否要自动应答 true代表的自动应答 false代表的手动应答
         * 3.消费者成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(queue_name,true,deliverCallback,cancelCallback);













    }
}
