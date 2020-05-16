package org.sample.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask futureTask = new FutureTask(new MyThread());

        new Thread(futureTask, "A").start();

        String str = (String)futureTask.get(); //可能会阻塞  放在最后或使用异步通信
        System.out.println("--------" + str);
    }
}

class MyThread implements Callable {

    @Override
    public String call() {

        return "aaaaaaaa";
    }
}