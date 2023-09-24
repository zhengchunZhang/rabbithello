package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.PrintStream;

public class ReceiveLogs02 {
    //交换机的名称
    public  static  final String EXCHANGE_NAME="logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个临时队列，队列的名称是随机的；消费者与队列断开连接的时候，队列就会被销毁
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收到的消息打印到屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println("ReceiveLogs02控制台接收到的消息："+new String(message.getBody(),"UTF-8"));
        };
        //消费者取消消息的回调
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});

    }
}
