package org.sample.juc.t001;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 加法计数器 cyclicBarrier.await() 则计数器+1
 *
 * CyclicBarrier是一个同步的辅助类，允许一组线程相互之间等待，达到一个共同点，再继续执行。
 *
 * 场景1： 长跑比赛，所有运动员到达终点，还有个统计排名的动作
 * 场景2： 集齐7颗龙珠，召唤神龙 *
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,() -> {
            System.out.println("召唤神龙成功");
        });

        for (int i = 1; i <= 7; i++) {
            final int temp = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "收集" + temp + "个龙珠");

                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


}
