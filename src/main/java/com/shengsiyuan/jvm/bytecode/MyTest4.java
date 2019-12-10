package com.shengsiyuan.jvm.bytecode;

/**
 * 栈帧（stack frame）
 *
 * 栈帧是一种用于帮助虚拟机执行方法调用与方法执行的数据结构
 *
 * 栈帧本身是一种数据结构，封装了方法的局部变量表、动态链接信息、方法的返回地址以及操作数栈等信息
 *
 * 符号引用，直接引用
 *
 * 有些符号引用在类加载阶段或是第一次使用时就会转化为直接引用，这种转化叫做静态解析；另外一些符号引用则是在每次运行期
 * 转换为直接引用，这种转换叫做动态链接，这体现为Java的多态性
 */

/**
 * 1. invokeinterface：调用接口的方法，实际上是运行期间决定的，决定到底调用实现该接口的那个对象的特定方法
 * 2. invokestatic：调用静态的方法
 * 3. invokespecial：调用自己的私有方法，构造方法（<init>）以及父类的方法
 * 4. invokevirtual：调用虚方法，运行期动态查找的过程
 * 5. invokedynamic：动态调用方法
 */

/**
 * 静态解析的4中清晰
 *
 * 1. 静态方法
 * 2. 父类方法
 * 3. 构造方法
 * 4. 私有方法（无法被重写）
 * 以上4类方法称作非虚方法，它们是在类加载阶段就可以将符合引用转换为直接引用的。
 */
public class MyTest4 {
    public static void test() {
        System.out.println("test invoked");
    }

    public static void main(String[] args) {
        test();
    }
}
