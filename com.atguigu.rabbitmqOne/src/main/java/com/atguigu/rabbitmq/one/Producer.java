package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Producer {
    public static  final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.154.155");
        factory.setUsername("admin");

        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化，默认消息存储在内存中
         * 3。该队列时候只供一个消费者进行消费 是否进行消息共享 true表示可以多个消费者消费 false表示单个消费者
         * 4.是否自动删除，最后一个消费者断开连接以后 该队列是否自动删除
         */


        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello world";
        /**
         * 发送一个消费
         * 1.发送到那个交换机
         * 2.路由的Key值是哪个 本次是队列的名称
         * 3.其他参数
         * 4.消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");





















    }
}
