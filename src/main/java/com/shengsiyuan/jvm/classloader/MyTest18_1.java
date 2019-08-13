package com.shengsiyuan.jvm.classloader;

public class MyTest18_1 {

    public static void main(String[] args) throws Exception {
        MyTest16 loader = new MyTest16("loader");
        loader.setPath("/Users/agan/Desktop/");

        Class<?> clazz = loader.loadClass("com.shengsiyuan.jvm.classloader.MyTest1");
        System.out.println("class: " + clazz.hashCode());
        System.out.println("class loader: " + clazz.getClassLoader());
    }
}
