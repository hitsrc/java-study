package com.sample.tools.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RedisHttpSession代理类 用于刷新过期时间
 * 每次操作后重置session失效时间
 * @author swq
 *
 */
public class RedisHttpSessionProxy implements InvocationHandler {
    private Object originalObj;

    public Object bind(Object originalObj) {
        this.originalObj = originalObj;
        return Proxy.newProxyInstance(originalObj.getClass().getClassLoader(), originalObj.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RedisHttpSession session = (RedisHttpSession) originalObj;
        //check redis connection
        
        //For every methods of interface, check it valid or not
        if (session.isInvalidated()){
        	//可以是登陆超时 或是无效的token
            throw new IllegalStateException("The HttpSession has already be invalidated.");
        } else {
            Object result =  method.invoke(originalObj, args);
            //if not invalidate method, refresh expireTime and lastAccessedTime;
            if (!method.getName().equals("invalidate")) {
            	session.setMaxInactiveInterval(30*60);//刷新过期时间 session30分钟后失效
                session.refresh();
                session.setLastAccessedTime(System.currentTimeMillis());
            }
            return result;
        }
    }
}