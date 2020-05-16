package org.sample.juc;

import lombok.Data;

@Data
class Aircondition {

    private int num = 0;

    public synchronized void increment() throws Exception {
        //1.判断
        while(num != 0){
            this.wait();
        }
        //2.操作
        num++;
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        //3.通知
        this.notifyAll();
    }
    public synchronized void decrement() throws Exception {
        //1.判断
        while (num == 0) {
            this.wait();
        }
        //2.操作
        num--;
        System.out.println(Thread.currentThread().getName() + "\t" + num);

        //3.通知
        this.notifyAll();


    }
}

/**
 * 两个线程 操作一个初始值为0的变量，
 * 一个对该变量加，一个减
 * 实现交替，10轮后，变量初始值为0
 */
public class ProdConsumerDemo04 {
    public static void main(String[] args) {
        Aircondition aircondition = new Aircondition();

        new Thread(() -> {
            for (int i=1;i<=10; i++) {
                try {
                    Thread.sleep(200);
                    aircondition.increment();
                    System.out.println("加操作后num:" + aircondition.getNum());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        },"A").start();

        new Thread(() -> {
            for (int i=1;i<=10; i++) {
                try {
                    Thread.sleep(200);
                    aircondition.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("减操作后num:" + aircondition.getNum());
            }

        },"B").start();
    }
}
