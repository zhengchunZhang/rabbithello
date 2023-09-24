package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Task2 {
    public static  final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
        //从控制台输入模拟发送消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            //设置为生产者发送消息为持久化消息（要求保存在磁盘上）不设置则默认保存在内存中
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送的消息："+message);
        }
    }

}
