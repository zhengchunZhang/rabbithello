package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Task01 {
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台输入模拟发送消息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送的消息："+message);
        }
    }
}
