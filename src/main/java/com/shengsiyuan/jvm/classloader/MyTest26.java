package com.shengsiyuan.jvm.classloader;


/**
 * 线程上下文类加载的一般使用模式（获取 - 使用 - 还原）
 *  ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
 *  try {
 *      Thread.currentThread().setContextClassLoader(targetClassLoader);
 *      method();
 *  } finally {
 *      Thread.currentThread().setContextClassLoader(classLoader);
 *  }
 *
 *  method()里面则调用Thread.currentThread().getContextClassLoader()，获取当前线程的上下文类加载器做某些事情
 *
 *  如果一个类由类加载器A加载，那么这个类的依赖类也是由相同的类加载器加载的（如果该依赖类之前没有被加载过的话）
 *
 *  ContextClassLoader的作用就是为了破坏Java的类加载委托机制
 *
 *  当高层提供统一的接口让低层去实现，同时又要在高层加载（或实例化）低层的类时，就必须要通过线程上下文类加载器来帮助高层的ClassLoader
 *  找到并加载该类。
 *
*/
public class MyTest26 {

    public static void main(String[] args) {

    }
}
