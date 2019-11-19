package com.roger.javainterview.rocketmqexample;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Comsumer2 {
    public static void main(String[] args) throws MQClientException {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cgroup1");
        // 设置NameServer的地址
        //        使用java 参数 -Drocketmq.namesrv.domain=localhost:3000 动态获取nameserver 地址，返回值为 192.156.19.2:9876;192.18.28.198:9876
//        完整的endpoint地址url为 http://localhost:3000/rocketmq/nsaddr
//        consumer.setNamesrvAddr("localhost:9876");
//        不消费已经存在的历史消息，只消费新发送过来的消息
        //        broker 的启动配置中，将 namesrvAddr = http://localhost:3000/rocketmq/nsaddr 即可动态获取 nsserver
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//       从第一条消息开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setConsumeThreadMax(10);
        consumer.setConsumeThreadMin(5);

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("TopicTest", "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者实例
        consumer.start();
//    consumer.fetchSubscribeMessageQueues("TopicTest");// 如果需要在启动时检查配置并及时报错，需要此语句。
        System.out.printf("Consumer Started.%n");
    }


}
