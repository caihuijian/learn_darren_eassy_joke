package com.example.testproxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * {@code InvocationHandler} is the interface implemented by
 * the <i>invocation handler</i> of a proxy instance.
 * <p>
 * InvocationHandler是一个接口 该接口被invocation handler的代理实例实现
 *
 * <p>Each proxy instance has an associated invocation handler.
 * When a method is invoked on a proxy instance, the method
 * invocation is encoded and dispatched to the {@code invoke}
 * method of its invocation handler.
 * <p>
 * 每个代理实例有一个相关联的调用handler
 * 当代理实例的一个方法被调用时 该方法的调用会被编码并分发给他的invocation handler的invoke方法
 *
 * @author Peter Jones
 * @see Proxy
 * @since 1.3
 */
public class DynamicProxyCallBack implements InvocationHandler {

    private final IBank mConsumer;

    public DynamicProxyCallBack(IBank consumer) {
        this.mConsumer = consumer;
    }


    /**
     * Processes a method invocation on a proxy instance and returns
     * the result.  This method will be invoked on an invocation handler
     * when a method is invoked on a proxy instance that it is
     * associated with.
     *
     * @param proxy  调用对应方法的代理实例
     * @param method 代理实例的对应的方法
     * @param args   对应方法的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e("hjcai", "做一些挂号等基础流程");
        Object object = method.invoke(mConsumer, args);
        Log.e("hjcai", "流程结束");
        return object;
    }
}
