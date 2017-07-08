package com.szss;

/**
 * Created by zcg on 2017/6/30.
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExcutorTest {
    public static final void main(String args[]) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {

            final int index = i;

            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Thread:"+Thread.currentThread().getName()+" "+index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("============================");
        }
    }
}
