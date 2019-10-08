package com.roger.javainterview;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.roger.javainterview.rabbitmq.ConfirmExample;
import com.roger.javainterview.rabbitmq.SimpleUsage;
import com.roger.javainterview.rabbitmq.TransactionExample;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
        // SimpleUsage simpleUsage = new SimpleUsage();
        // simpleUsage.excute();

        TransactionExample transactionExample = new TransactionExample();
        try {
            transactionExample.useTrancation();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ConfirmExample confirmExample = new ConfirmExample();
        try {
            confirmExample.excute();
        } catch (IOException | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    

  
}
