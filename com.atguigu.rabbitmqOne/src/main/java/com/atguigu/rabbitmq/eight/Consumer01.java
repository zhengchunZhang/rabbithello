package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列名称
    private static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        //普通队列
        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //过期时间 10s
//        params.put("x-message-ttl",100000);
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "lisi");
//        params.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,params);
        //死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);


        //绑定普通队列和普通交换机
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信队列和死信交换机
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收死信队列消息.....");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            if(message.indexOf("5")!=-1) {
                System.out.println("消息被拒绝"+message);
                //第二个参数表示不会将该消息塞回去至原队列
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else {

                System.out.println("Consumer01 接收到消息"+message);
                //false表示不会批量应答
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            }
        };
        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });

    }
}
