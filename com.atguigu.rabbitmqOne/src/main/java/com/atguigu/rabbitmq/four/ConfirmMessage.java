package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    //批量发消息的个数
    public static final int message_count = 1000;
    public static void main(String[] args) throws Exception {
        ConfirmMessage.publicMessageAsync();
    }
    public  static  void publishMessageOnly() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < message_count; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息马上就发布确认
            boolean flag = channel.waitForConfirms();
            if(flag) {
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时"+(end-begin)+"ms");


    }
    public static void publicMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;
        for (int i = 0; i < message_count; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息马上就发布确认
            if(i%batchSize == 0) {
                //发布确认
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时"+(end-begin)+"ms");
    }

    public static void publicMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表 适用高并发的情况
         * 1.轻松的将序号和消息进行关联
         * 2.轻松批量删除条目 只需要给到序号
         * 3.支持多线程（高并发）
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //消息成功回调函数
        /**
         * 1.消息的确认
         * 2.是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag,multiple) ->{
            if(multiple) {
                //2.删除已经确认的消息 剩下就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息"+deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag,multiple) ->{
            //3.打印一下未确认的消息都有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息"+message+"未确认的标记：："+deliveryTag);
        };
        //准备消息的监听器 监听消息发送的成功和失败
        /**
         * 1.监听那些消息成功了
         * 2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback,nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < message_count; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.记录要发送的所有消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);

        }

        long end = System.currentTimeMillis();
        System.out.println("耗时"+(end-begin)+"ms");
    }
}
