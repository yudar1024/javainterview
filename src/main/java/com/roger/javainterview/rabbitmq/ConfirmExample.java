package com.roger.javainterview.rabbitmq;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

public class ConfirmExample {

  private final static String EXCHANGE_NAME = "durable-exchange";
  private final static String QUEUE_NAME = "durable-queue";
  private final static int TIME_OUT = 1000;

  public void excute() throws IOException, TimeoutException {
    publishWithBatch();
    consumer();
  }

  private void publish() throws IOException, TimeoutException {
    Connection conn = ConnectionUtil.GetRabbitConnection();

    // 创建信道
    Channel channel = conn.createChannel();
    // 生命交换器 参数1 交换器名称， 参数2 交换器类型（direct|headers|fanout|topic） 参数3 是否持久化，
    // 参数4，是否自动删除队列 参数5 额外参数
    channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
    // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "myroutingKey");
    String message = String.format("时间 => %s", new Date().getTime());
    try {
      channel.confirmSelect(); // 声明发布确认，
      AMQP.BasicProperties props = new AMQP.BasicProperties().builder().deliveryMode(2).build();
      // 发送消息 PERSISTENT_TEXT_PLAIN 中的发送模式为2
      channel.basicPublish(EXCHANGE_NAME, "myroutingKey", MessageProperties.PERSISTENT_TEXT_PLAIN,
          message.getBytes("UTF-8"));
      // channel.basicPublish(EXCHANGE_NAME, "myroutingKey", props,
      // message.getBytes("UTF-8"));
      if (channel.waitForConfirms(TIME_OUT)) {
        System.out.println("message deliveried");
      }
    } catch (Exception e) {
      channel.txRollback();
    } finally {
      channel.close();
      conn.close();
    }
  }

  private void publishWithBatch() throws IOException, TimeoutException {
    Connection conn = ConnectionUtil.GetRabbitConnection();

    // 创建信道
    Channel channel = conn.createChannel();
    // 生命交换器 参数1 交换器名称， 参数2 交换器类型（direct|headers|fanout|topic） 参数3 是否持久化，
    // 参数4，是否自动删除队列 参数5 额外参数
    channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
    // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "myroutingKey");
    channel.confirmSelect(); // 声明发布确认，
    AMQP.BasicProperties props = new AMQP.BasicProperties().builder().deliveryMode(2).build();
    try {
      for (int i = 0; i < 10; i++) {
        String message = String.format("时间 => %s", i);

        // 发送消息 PERSISTENT_TEXT_PLAIN 中的发送模式为2
        channel.basicPublish(EXCHANGE_NAME, "myroutingKey", MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.getBytes("UTF-8"));
      }

      // 将需要保障顺序的一组操作，发送到一个queque中，一个queue只对应一个消费者。即可保障消息的顺序性，同类型的消费者，使用多个queue一一对应
      channel.waitForConfirmsOrDie(TIME_OUT);
      System.out.println("batch message deliveried");

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      channel.close();
      conn.close();
    }
  }

  private void publishWithAsyncListener() throws IOException, TimeoutException {
    Connection conn = ConnectionUtil.GetRabbitConnection();

    // 创建信道
    Channel channel = conn.createChannel();
    // 生命交换器 参数1 交换器名称， 参数2 交换器类型（direct|headers|fanout|topic） 参数3 是否持久化，
    // 参数4，是否自动删除队列 参数5 额外参数
    channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
    // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "myroutingKey");
    channel.confirmSelect(); // 声明发布确认，
    AMQP.BasicProperties props = new AMQP.BasicProperties().builder().deliveryMode(2).build();
    try {
      for (int i = 10; i < 20; i++) {
        String message = String.format("时间 => %s", i);

        // 发送消息 PERSISTENT_TEXT_PLAIN 中的发送模式为2
        channel.basicPublish(EXCHANGE_NAME, "myroutingKey", MessageProperties.PERSISTENT_TEXT_PLAIN,
            message.getBytes("UTF-8"));
      }
      // 异步监听确认和未确认的消息
      channel.addConfirmListener(new ConfirmListener() {

        @Override
        public void handleNack(long deliveryTag, boolean multiple) throws IOException {
          System.out.println("未确认消息，标识：" + deliveryTag);

        }

        @Override
        public void handleAck(long deliveryTag, boolean multiple) throws IOException {
          System.out.println(String.format("已确认消息，标识：%d，多个消息：%b", deliveryTag, multiple));

        }
      });

      System.out.println("batch message deliveried");

    } catch (Exception e) {
      channel.txRollback();
    } finally {
      channel.close();
      conn.close();
    }
  }

  private void consumer() {
    // 创建一个连接
    Connection conn = ConnectionUtil.GetRabbitConnection();
    if (conn != null) {
      try {
        // 创建通道
        Channel channel = conn.createChannel();
        // 参数4，是否自动删除队列 参数5 额外参数
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        // 声明队列【参数说明：参数一：队列名称，参数二：是否持久化；参数三：是否独占模式；参数四：消费者断开连接时是否删除队列；参数五：消息其他参数】
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "myroutingKey");

        // 创建订阅器，并接受消息
        channel.basicConsume(QUEUE_NAME, false, "consumer1", new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
              byte[] body) throws IOException {
            String routingKey = envelope.getRoutingKey(); // 队列名称
            String contentType = properties.getContentType(); // 内容类型
            String content = new String(body, "utf-8"); // 消息正文
            System.out.println("confirm消息正文：" + content);
            channel.basicAck(envelope.getDeliveryTag(), false); // 手动确认消息【参数说明：参数一：该消息的index；参数二：是否批量应答，true批量确认小于index的消息】
          }
        });

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}