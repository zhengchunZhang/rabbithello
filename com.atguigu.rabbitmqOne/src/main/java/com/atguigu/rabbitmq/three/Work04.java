package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Work04 {
    public static  final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C4等待接收消息处理时间较长");
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            //模拟执行时间1s
            SleepUtils.sleep(30);
            System.out.println("接收到的消息："+new String(message.getBody(),"UTF-8"));
            //手动应答
            /**
             * 1.消息的标记 tag
             * 2.是否批量应答
             *
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //设置不公平分发
    //   int prefetchCount = 1;
        //预取值是5
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,(consumerTag->{
            System.out.println(consumerTag+"消息取消消费接口回调逻辑");
        }));


















    }
}
