package com.hjzgg.oauth2.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hujunzheng on 2017/6/23.
 */

class ThreadCount {
    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static final ThreadLocal<Integer> threadCount =
            ThreadLocal.withInitial(() -> nextId.getAndIncrement());//返回的是重写了ThreadLocal initialValue()方法的ThreadLocal.SuppliedThreadLocal对象

    public static int get() {
        return threadCount.get();
    }
}

class ThreadSign {
    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static final ThreadLocal<Integer> threadCount = new InheritableThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return nextId.getAndIncrement();
        }
    };

    public static int get() {
        return threadCount.get();
    }
}


public class ThreadLocalTest {

    public static void testThreadLocal() {
        for (int i = 0; i < 5; ++i) {
            int pi = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " count->" + ThreadCount.get());
                for (int j = 0; j < 5; ++j) {
                    new Thread(() -> System.out.println("       " + Thread.currentThread().getName() + " count->" + ThreadCount.get()), "cthread" + j + " pthread" + pi).start();
                }
            }, "pthread" + i).start();
        }
    }

    public static void testInheritableThreadLocal() {
        for(int i=0; i<5; ++i) {
            int pi = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " sign->" + ThreadSign.get());
                for(int j=0; j<5; ++j) {
                    new Thread(() -> System.out.println("       " + Thread.currentThread().getName() + " sign->" + ThreadSign.get()), "cthread" + j + " pthread" + pi).start();
                }
            }, "pthread" + i).start();
        }
    }

    public static void main(String[] args) {
//        testThreadLocal();
        testInheritableThreadLocal();
    }
}
