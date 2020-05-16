package org.sample.juc.t001;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch是一个同步的辅助类，允许一个或多个线程，等待其他一组线程完成操作，再继续执行。  *
 *
 * ** 减法计数器
 * countDownLatch.countDown() //计数器-1 *
 * 等待计数器归零,countDownLatch.await()会被唤醒，再向下执行
 *
 *
 *
 *
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(50);

        for (int i = 1; i <= 50; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "，接走了一个孩子");
                countDownLatch.countDown();

            },String.valueOf(i)).start();
        }
        try {
            countDownLatch.await(); //等待计数器归零，然后再继续往下执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("所有孩子接走了，关闭幼儿园大门");
    }














}