package com.shengsiyuan.jvm.memory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


/**
 * 方法区产生内存溢出错误
 */
public class MyTest4 {
    public static void main(String[] args) {
        for (;;) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MyTest4.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (obj, method, arg, proxy) ->
                    proxy.invokeSuper(obj, arg));

            enhancer.create();

            System.out.println("gen clazz success");
        }
    }
}
