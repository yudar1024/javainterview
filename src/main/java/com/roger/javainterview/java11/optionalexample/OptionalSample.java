package com.roger.javainterview.java11.optionalexample;

import java.util.Optional;

public class OptionalSample {

  private static void showOptionalUse() {
    Optional<Integer> price1 = Optional.of(0);
    Optional<Integer> price2 = Optional.empty();
    Optional<Integer> price3 = Optional.ofNullable(null);

    // 当null时 返回42
    int p2 = price2.orElseGet(() -> 42);
    int p3 = price3.orElse(33);
    System.out.println("p2=" + p2);
    System.out.println("p3=" + p3);
    price3 = Optional.of(10);

  }

  private static void processValue(int value) {
    System.out.println(value);
  }

}