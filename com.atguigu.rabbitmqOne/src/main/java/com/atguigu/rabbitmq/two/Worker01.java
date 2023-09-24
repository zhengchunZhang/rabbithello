package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Worker01 {
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println("接收到的消息："+new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) ->{
            System.out.println(consumerTag+"消息取消消费接口回调逻辑");
        };
        System.out.println("C2等待接收消息....");
        //接收消息
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
