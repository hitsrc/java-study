package org.sample.juc.t001;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 3个车位 6台车       都停进去并随后离开
 * semaphore.acquire();//获取  假如满了，等待直到释放位置
 * semaphore.release();//释放  当前信号量+1，然后唤醒等待的线程
 * 作用：多个共享资源互斥的使用   并发限流，控制最大的线程数
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        //线程数量  停车位，限流
        Semaphore  semaphore = new Semaphore(3);

        for (int i = 1; i <= 6; i++) {
            new Thread( () -> {
                try {
                    semaphore.acquire();//获取
                    System.out.println(Thread.currentThread().getName() + "+++抢到车位");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + "---离开车位");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();//释放
                }
            }).start();
        }

    }
}
