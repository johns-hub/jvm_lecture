package com.shengsiyuan.jvm.gc;

public class MyTest4 {
    public static void main(String[] args) throws InterruptedException {
        byte[] byte_1 = new byte[512 * 1024];
        byte[] byte_2 = new byte[512 * 1024];

        myGc();
        Thread.sleep(1000);

        System.out.println("111111");

        myGc();
        Thread.sleep(1000);

        System.out.println("222222");

        myGc();
        Thread.sleep(1000);

        System.out.println("333333");

        myGc();
        Thread.sleep(1000);

        System.out.println("444444");

        byte[] byte_3 = new byte[1024 * 1024];
        byte[] byte_4 = new byte[1024 * 1024];
        byte[] byte_5 = new byte[1024 * 1024];

        myGc();
        Thread.sleep(1000);

        System.out.println("555555");

        myGc();
        Thread.sleep(1000);

        System.out.println("666666");

        System.out.println("exit");
    }

    public static void myGc() {
        for (int i = 0; i < 40; i++) {
            byte[] bytes = new byte[1024 * 1024];
        }
    }
}
