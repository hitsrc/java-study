package org.sample.juc;

import java.util.concurrent.TimeUnit;

/**
 * 8 lock
 * 请问以下情况， 先打印邮件还是短信
 * 对象锁 全局锁
 *
 * 1 标准访问                                                先打印邮件 因为中间有sleep(100) 邮件线程先抢了phone对象锁 先执行
 * 2 邮件方法暂停4秒                                         先打印邮件  锁住了对象 谁先抢到锁先执行
 * 3 新增普通方法sayHello,    先打印邮件还是hello             先打印hello hello没有锁 不形成竞争
 * 4 两部手机                                                先打印短信  锁是分别锁的两个对象
 * 5 两个静态同步方法，同一个手机                              先打印邮件  锁的是Class(全局锁)
 * 6 两个静态同步方法，两个个手机                              同上
 * 7 1个静态同步方法，1个普通同步方法，同一个手机               短信  锁的不是同一个东西 不互相竞争
 * 8 1个静态同步方法，1个普通同步方法，两个手机                 短信  锁的不是同一个东西 不互相竞争
 *
 * 一个对象里如果有多个synchronized方法，同一个时刻，只要一个线程调用其中一个synchronized方法了，其他线程都只能等待，
 * 也就是说，某一个时刻内，只能有唯一一个线程去访问这些synchronized方法
 * 锁的是当前对象this
 */
public class LockDemo05 {
    public static void main(String[] args) throws InterruptedException {

        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                //phone1.sendSyncEmail();
                phone1.sendStaticSyncEmail();//同 Phone.sendStaticSyncEmail()
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();
        Thread.sleep(100);
        new Thread(() -> {
            try {
                //phone1.sendSyncSMS();
                //phone1.sayHello();
                //phone2.sendStaticSyncSMS();//同 Phone.sendStaticSyncEmail()

                phone2.sendSyncSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();
    }
}


class Phone {
    public synchronized void sendSyncEmail() throws Exception {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("---------sendSyncEmail");
    }

    public static synchronized void sendStaticSyncEmail() throws Exception{
        TimeUnit.SECONDS.sleep(4);
        System.out.println("---------sendStaticSyncEmail");
    }

    public synchronized void sendSyncSMS() throws Exception {
        System.out.println("---------sendSyncSMS");
    }

    public static synchronized void sendStaticSyncSMS() throws Exception {
        System.out.println("---------sendStaticSyncSMS");
    }
    public void sayHello() {
        System.out.println("---------sayHello");
    }
}
