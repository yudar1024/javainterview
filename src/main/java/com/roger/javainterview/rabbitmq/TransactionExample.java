package com.roger.javainterview.rabbitmq;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

// 本例包含消息的持久化，ack确认（防丢失），消息的事务与顺序问题。

public class TransactionExample {

  // 事务方式1 使用txSelect
  public void useTrancation() throws IOException, TimeoutException {
    publish();
    consumer();
  }

  private void publish() throws IOException, TimeoutException {
    Connection conn = ConnectionUtil.GetRabbitConnection();

    // 创建信道
    Channel channel = conn.createChannel();
    // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
    channel.queueDeclare("txSelect", true, false, false, null);
    String message = String.format("时间 => %s", new Date().getTime());
    try {
      channel.txSelect(); // 声明事务 默认的事务方式性能极差，不推荐使用
      // 发送消息
      channel.basicPublish("", "txSelect", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
      channel.txCommit(); // 提交事务
    } catch (Exception e) {
      channel.txRollback();
    } finally {
      channel.close();
      conn.close();
    }
  }


  /**
   * 消费消息
   */
  private void consumer() {
    // 创建一个连接
    Connection conn = ConnectionUtil.GetRabbitConnection();
    if (conn != null) {
      try {
        // 创建通道
        Channel channel = conn.createChannel();
        // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
        channel.queueDeclare("txSelect", true, false, false, null);

        // 创建订阅器，并接受消息
        channel.basicConsume("txSelect", false, "", new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
              byte[] body) throws IOException {
            String routingKey = envelope.getRoutingKey(); // 队列名称
            String contentType = properties.getContentType(); // 内容类型
            String content = new String(body, "utf-8"); // 消息正文
            System.out.println("事务消息正文：" + content);
            channel.basicAck(envelope.getDeliveryTag(), false); // 手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答，true批量确认小于index的消息】
          }
        });

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}