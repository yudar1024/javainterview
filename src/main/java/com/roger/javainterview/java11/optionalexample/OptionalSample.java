package com.roger.javainterview.java11.optionalexample;

import java.util.Optional;

public class OptionalSample {

  private static void showOptionalUse() {
    Optional<Integer> price1 = Optional.of(0);
    Optional<Integer> price2 = Optional.empty();
    Optional<Integer> price3 = Optional.ofNullable(null);
    price3.ifPresentOrElse( p-> System.out.println(p),new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("null value");
      }
    }));

    // 当null时 返回42
    int p2 = price2.orElseGet(() -> 42);
    int p3 = price3.orElse(33);
    System.out.println("p2=" + p2);
    System.out.println("p3=" + p3);
    price3 = Optional.of(10);
    price3.ifPresentOrElse(p -> {
      System.out.println(p);
    }, () -> System.out.println("null value"));
    price3.ifPresentOrElse(p -> {
      processValue(p);
    }, () -> System.out.println("null value"));

  }

  private static void processValue(int value) {
    System.out.println(value);
  }

}