package com.example.springstudy.thread;

public class ThreadLocalPractice {

    // 각 쓰레드별로 커넥션을 저장하는 공간
    static ThreadLocal<String> threadLocalConnection = new ThreadLocal<>();

    public static void main(String[] args) {
        // 쓰레드 A
        Runnable taskA = () -> {
            threadLocalConnection.set("connA");
            System.out.println("[Thread A] Set connection: " + threadLocalConnection.get());

            try {
                Thread.sleep(1000); // A가 오래 작업하는 척
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("[Thread A] Final connection: " + threadLocalConnection.get());
        };

        // 쓰레드 B
        Runnable taskB = () -> {
            System.out.println("[Thread B] Initial connection: " + threadLocalConnection.get()); // null
            threadLocalConnection.set("connB");
            System.out.println("[Thread B] Set connection: " + threadLocalConnection.get());
        };

        new Thread(taskA).start();
        new Thread(taskB).start();
    }
}

