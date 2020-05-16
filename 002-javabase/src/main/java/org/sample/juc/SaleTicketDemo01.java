package org.sample.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个售票员 卖出30张票
 *    口诀：  线程 操作 资源类
 * 如何编写企业级的多线程代码
 */
public class SaleTicketDemo01 {
    public static void main(String[] args) {

        final Ticket ticket = new Ticket();

        new Thread(() -> {for(int i = 1; i <= 40; i++) ticket.sale(); },"A").start();
        new Thread(() -> {for(int i = 1; i <= 40; i++) ticket.sale(); },"B").start();
        new Thread(() -> {for(int i = 1; i <= 40; i++) ticket.sale(); },"C").start();


        //Enum : Thread.State
        /*new Thread(new Runnable() {
            public void run() {
                for(int i = 1; i <= 40; i++) {
                    ticket.sale();
                }
            }
        }, "A").start();
        new Thread(new Runnable() {
            public void run() {
                for(int i = 1; i <= 40; i++) {
                    ticket.sale();
                }
            }
        }, "B").start();
        new Thread(new Runnable() {
            public void run() {
                for(int i = 1; i <= 40; i++) {
                    ticket.sale();
                }
            }
        }, "C").start();*/



    }
}

class Ticket {
    private int number = 30;

    Lock lock = new ReentrantLock();

    public void sale(){
        lock.lock();
        try {
            if(number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t 卖出第" + (number--) + "票");
            }

        } catch (Exception e) {

        } finally {
            lock.unlock();
        }

    }
}