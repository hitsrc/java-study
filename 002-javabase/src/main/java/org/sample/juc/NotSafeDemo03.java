package org.sample.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 集合类不安全
 *
 * 故障现象 java.util.ConcurrentModificationException
 *
 * 解决方案：
 *      1. Vector
 *      2. Collections.synchronizedList(new ArrayList<>())
 *
 * 优化建议
 *      3. CopyOnWriteArrayList
 *
 *      opyOnWrite容器即写时复制容器，往一个容器添加元素的时候，不直接往当前容器Object[]添加，
 *      而是先将当前容器Object[]进行copy,再将原容器的引用指向新的容器，setArray(new Alements);
 *      这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素，
 *      所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器
 *
 *
 *  ArrayList底层是Object[]
 *  HashSet 底层是HashMap   用了它的key,值是一个常量new Object()
 */
public class NotSafeDemo03 {

    public static void main(String[] args) {
        //listNoSafe();

        //setNoSafe();

        Map<String,String> map = new HashMap<>();
        for (int i = 1; i<=30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName() ,UUID.randomUUID().toString().substring(0,20));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }

    /**
     * CopyOnWriteArraySet
     */
    private static void setNoSafe() {
        Set set = new CopyOnWriteArraySet();
        for (int i = 1; i<=30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,20));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }

    /**
     * CopyOnWriteArrayList
     */
    private static void listNoSafe() {
        //List list = new ArrayList<>();// 线程不安全

        //List list = new Vector();

        //List<String> list = Collections.synchronizedList(new ArrayList<>());

        List list = new CopyOnWriteArrayList();

        /*
        list.add("a");
        list.add("a");
        list.add("a");
        list.forEach(System.out::println);
        */

        for (int i = 1; i<=30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,20));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

        //list.forEach(System.out::println);
    }
}
