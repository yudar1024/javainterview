package com.roger.javainterview.concurrence;

public class ThreadLocalTest {

    private static VariableEntity entity1= new VariableEntity();
    private static ThreadLocal<String> threadLocal= new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                entity1.setAge(1);
                entity1.setName("n1");

             threadLocal.set("t1");
             System.out.println("thread 1 val1="+entity1.toString()+" local1="+threadLocal.get().toString());
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                entity1.setAge(2);
                entity1.setName("n2");
                threadLocal.set("t2");
                System.out.println("thread 2 val1="+entity1.toString()+" local1="+threadLocal.get().toString());

            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
