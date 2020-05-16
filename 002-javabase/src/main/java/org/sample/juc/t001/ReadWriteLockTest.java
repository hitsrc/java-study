package org.sample.juc.t001;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteLockTest {


    public static void main(String[] args) {

        MyCache myCache = new MyCache();

        for (int i = 1; i <= 8 ; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.put(temp + "" , temp);
            }, String.valueOf(i)).start();

        }


        for (int i = 1; i <= 8 ; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.get(temp + "" );
            }, String.valueOf(i)).start();

        }
    }

}

class MyCache {

    private volatile Map<String,Object> map = new HashMap<>();

    public void put(String key, Object val) {
        System.out.println(Thread.currentThread().getName() + "写入" + key);
        map.put(key,val);
        System.out.println(Thread.currentThread().getName() + "写OK");
    }

    public void get(String key) {
        System.out.println(Thread.currentThread().getName() + "读取" + key);
        Object o = map.get(key);
        System.out.println(Thread.currentThread().getName() + "读OK");
    }

}
