package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //设置消息的 TTL 时间
//        AMQP.BasicProperties properties = new
//                AMQP.BasicProperties().builder().expiration("10000").build();
        AMQP.BasicProperties properties = null;
        for (int i = 0; i < 11; i++) {
            String message="info"+i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties,
                    message.getBytes());
            System.out.println("生产者发送消息:"+message);
        }
    }
}
