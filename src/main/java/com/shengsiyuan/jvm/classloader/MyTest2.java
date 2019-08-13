package com.shengsiyuan.jvm.classloader;

/**
 * 常量在编译阶段会存入到调用这个常量的方法所在的类的常量池中，
 * 本质上，调用类并没有直接引用到定义常量的类，因此并不会触发
 * 定义常量的类的初始化
 *
 * 注意：这里指的是将常量存放到了MyTest2的常量池中，之后MyTest2与MyParent2就没有任何关系了
 * 甚至，我们可以将MyParent2的class文件删除
 *
 *
 * 助记符：
 * ldc 表示将int，float或是String类型的常量值从常量池中推送至栈顶
 * bipush 表示将单字节（-128 ~ 127）的常量值推送至栈顶
 * sipush 表示将一个短整型常量值（-32768 ~ 32767）推送至栈顶
 * iconst_1 表示将int类型1推送至栈顶（iconst_1 ~ iconst_5）
 */
public class MyTest2 {
    public static void main(String[] args) {
        System.out.println(MyParent2.m);
    }
}

class MyParent2 {
    public static final String str = "hello world";

    public static final short s = 127;

    public static final int i = 128;

    public static final int m = 6;

    static {
        System.out.println("MyParent2 static block");
    }
}
