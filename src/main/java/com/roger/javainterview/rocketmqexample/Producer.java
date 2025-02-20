package com.roger.javainterview.rocketmqexample;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByRandom;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class Producer {
    public static void main(String[] args) throws Exception {
        sendMsg();
    }

    private static void sendMsg() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("pgroup2");
        producer.setInstanceName("pinstance1");
        producer.setRetryTimesWhenSendFailed(5);
//        producer.setRetryTimesWhenSendAsyncFailed(5);
        // 设置NameServer的地址
//        使用java 参数 -Drocketmq.namesrv.domain=localhost:3000 动态获取nameserver 地址，返回值为 192.156.19.2:9876;192.18.28.198:9876
//        完整的endpoint地址url为 http://localhost:3000/rocketmq/nsaddr
//        broker 的启动配置中，将 namesrvAddr = http://localhost:3000/rocketmq/nsaddr 即可动态获取 nsserver
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            msg.setKeys("pgroup1"+"node1"+"index1");
//            msg.setDelayTimeLevel(1); 设置延迟发送
            // 发送消息到一个Broker
//            SendResult sendResult = producer.send(msg);
            SendResult sendResult = producer.send(msg,new SelectMessageQueueByRandom(),null); //选择发送路由
            // 通过sendResult返回消息是否成功送达
            System.out.printf("%s%n", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
