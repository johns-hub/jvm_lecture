package com.shengsiyuan.jvm.classloader;

/**
 * 当一个接口在初始化时，并不要求其父接口都完成了初始化
 * 只有在真正使用到父接口的时候（如引用接口中所定义的常量时），才会初始化
 */
public class MyTest5 {
    public static void main(String[] args) {
        // System.out.println(MyChild5.b);
        System.out.println(MyParent5_1.t2);
    }
}

class MyGrandpa{
    public static Thread t = new Thread(){
        {
            System.out.println("MyGrandpa invoked");
        }
    };
}

class MyParent5 extends MyGrandpa {
    //public static final int a = 1;

    //public static int a = new Random().nextInt(3);

    public static Thread t = new Thread(){
        {
            System.out.println("MyParent5 invoked");
        }
    };
}


class MyChild5 extends MyParent5 {
    //public static int b = 5;

    //public static final int b = new Random().nextInt(3);

    public static int b = 5;
}


interface MyGrandpa5_1 {
    public static Thread t1 = new Thread(){
        {
            System.out.println("MyGrandpa5_1 invoked");
        }
    };
}

interface MyParent5_1 extends MyGrandpa5_1 {
    public static Thread t2 = new Thread(){
        {
            System.out.println("MyParent5_1 invoked");
        }
    };
}


