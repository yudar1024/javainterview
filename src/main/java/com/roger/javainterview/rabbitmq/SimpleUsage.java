package com.roger.javainterview.rabbitmq;

import java.io.IOException;
import java.util.Date;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class SimpleUsage {

  public void excute (){
    publisher();
    consumer();
  }

  /**
   * 推送消息
   */
  public void publisher() {
    // 创建一个连接
    Connection conn = ConnectionUtil.GetRabbitConnection();
    if (conn != null) {
      try {
        // 创建通道
        Channel channel = conn.createChannel();
        // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
        channel.queueDeclare("queue1", false, false, false, null);
        String content = String.format("当前时间：%s", new Date().getTime());
        // 发送内容【参数说明：参数一：交换机名称；参数二：队列名称，参数三：消息的其他属性-routing
        // headers，此属性为MessageProperties.PERSISTENT_TEXT_PLAIN用于设置纯文本消息存储到硬盘；参数四：消息主体】
        channel.basicPublish("", "queue1", null, content.getBytes("UTF-8"));
        System.out.println("已发送消息：" + content);
        // 关闭连接
        channel.close();
        conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 消费消息
   */
  public void consumer() {
    // 创建一个连接
    Connection conn = ConnectionUtil.GetRabbitConnection();
    if (conn != null) {
      try {
        // 创建通道
        Channel channel = conn.createChannel();
        // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
        channel.queueDeclare("queue1", false, false, false, null);

        // 创建订阅器，并接受消息
        channel.basicConsume("queue1", false, "", new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
              byte[] body) throws IOException {
            String routingKey = envelope.getRoutingKey(); // 队列名称
            String contentType = properties.getContentType(); // 内容类型
            String content = new String(body, "utf-8"); // 消息正文
            System.out.println("消息正文：" + content);
            channel.basicAck(envelope.getDeliveryTag(), false); // 手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答，true批量确认小于index的消息】
          }
        });

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}