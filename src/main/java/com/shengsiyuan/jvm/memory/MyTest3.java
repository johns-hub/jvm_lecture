package com.shengsiyuan.jvm.memory;

/**
 * 死锁
 */
public class MyTest3 {
    public static void main(String[] args) {
        new Thread(()-> A.method(), "Thread-A").start();
        new Thread(()-> B.method(), "Thread-B").start();
    }
}

class A {
    public static synchronized void method() {
        System.out.println("method from A");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        B.method();
    }
}

class B {
    public static synchronized void method() {
        System.out.println("method from B");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        A.method();
    }
}