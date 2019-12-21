package com.shengsiyuan.jvm.gc;

public class MyTest5 {
    public static void main(String[] args) {
        int size = 1024 * 1024;

        byte[] myAlloc1 = new byte[4 * size];

        System.out.println("111111");

        byte[] myAlloc2 = new byte[4 * size];

        System.out.println("222222");

        byte[] myAlloc3 = new byte[4 * size];

        System.out.println("333333");

        byte[] myAlloc4 = new byte[2 * size];

        System.out.println("444444");
    }
}
