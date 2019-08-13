package com.shengsiyuan.jvm.classloader;

import java.io.*;

public class MyTest16 extends ClassLoader {
    private String classLoaderName;
    private String path;

    private static final String fileExtension = ".class";


    public MyTest16(String classLoaderName) {
        super(); // 将系统类加载器当作该类加载器的父加载器
        this.classLoaderName = classLoaderName;
    }

    public MyTest16(ClassLoader parent, String classLoaderName) {
        super(parent); // 显式指定该类加载器的父加载器
        this.classLoaderName = classLoaderName;
    }

    public MyTest16(ClassLoader parent) {
        super(parent);
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("findClass invoked：" + name);
        System.out.println("class loader name：" + this.classLoaderName);
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name){
        byte[] data = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            name = name.replace(".", "/");
            inputStream = new BufferedInputStream(new FileInputStream(path + name + fileExtension));
            outputStream = new ByteArrayOutputStream();

            byte[] bytes = new byte[1024];
            int len;
            while (-1 != (len = inputStream.read(bytes))) {
                outputStream.write(bytes, 0, len);
            }
            data = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream)
                    inputStream.close();
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    public static void main(String[] args) throws Exception {
        MyTest16 classLoader = new MyTest16("loader");

        //classLoader.setPath("/Users/agan/code/jvm_lecture/out/production/classes/");
        classLoader.setPath("/Users/agan/Desktop/classes/");

        Class<?> clazz = classLoader.loadClass("com.shengsiyuan.jvm.classloader.MyTest3");
        System.out.println("class: " + clazz.hashCode());
        Object object = clazz.newInstance();
        System.out.println(object);

        System.out.println();

        classLoader = null;
        clazz = null;
        object = null;
        Thread.sleep(20000);
        System.gc();


        classLoader = new MyTest16("loader");
        classLoader.setPath("/Users/agan/Desktop/classes/");
        clazz = classLoader.loadClass("com.shengsiyuan.jvm.classloader.MyTest3");
        System.out.println("class: " + clazz.hashCode());
        object = clazz.newInstance();
        System.out.println(object);

        /*MyTest16 classLoader2 = new MyTest16(classLoader, "loader2");
        classLoader2.setPath("/Users/agan/Desktop/classes/");

        Class<?> clazz2 = classLoader2.loadClass("com.shengsiyuan.jvm.classloader.MyTest4");
        System.out.println("class: " + clazz2.hashCode());
        Object object2 = clazz2.newInstance();
        System.out.println(object2);

        System.out.println();

        MyTest16 classLoader3 = new MyTest16(classLoader2, "loader2");
        classLoader3.setPath("/Users/agan/Desktop/classes/");

        Class<?> clazz3 = classLoader3.loadClass("com.shengsiyuan.jvm.classloader.MyTest4");
        System.out.println("class: " + clazz3.hashCode());
        Object object3 = clazz3.newInstance();
        System.out.println(object3);*/
    }
}
