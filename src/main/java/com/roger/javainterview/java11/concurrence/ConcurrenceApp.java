package com.roger.javainterview.java11.concurrence;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ConcurrenceApp {

  public static void main(String[] args) {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
    String cc = "a,b,c,d";
    List<String> result = List.of(cc.split(",")).stream().map(x -> x.toUpperCase()).collect(Collectors.toList());

    ThreadPoolExecutor tpe=new ThreadPoolExecutor(2,10,2000, TimeUnit.MILLISECONDS,queue);
    System.out.println(result);
    tpe.execute(new Runnable() {
      @Override
      public void run() {

        System.out.println(result.toString()+ ZonedDateTime.now());
      }
    });
    tpe.shutdownNow();
  }
    
}