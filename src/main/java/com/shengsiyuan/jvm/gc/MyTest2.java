package com.shengsiyuan.jvm.gc;

import java.io.IOException;

/**
 * PretenureSizeThreshold：设置对象超过多大时直接在老年代进行分配
 */
public class MyTest2 {
    public static void main(String[] args) throws IOException {
        int size = 1024 * 1024;
        byte[] bytes = new byte[5 * size];

        System.in.read();
    }
}
