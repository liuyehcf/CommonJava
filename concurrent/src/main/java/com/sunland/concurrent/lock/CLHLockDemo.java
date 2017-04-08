package com.sunland.concurrent.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by liuye on 2017/4/8 0008.
 */
class QNode {
    volatile boolean locked;
}


public class CLHLockDemo {
    AtomicReference<QNode> tail = new AtomicReference<QNode>(new QNode());
    ThreadLocal<QNode> currNode;

    public CLHLockDemo() {
        tail = new AtomicReference<QNode>(new QNode());
        currNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    public void lock() {
        QNode curr = this.currNode.get();
        curr.locked = true;
        QNode prev = tail.getAndSet(curr);

        print();

        while (prev.locked) {
        }
    }

    public void unlock() {
        QNode qnode = currNode.get();
        qnode.locked = false;
    }

    private void print() {
        System.out.println(Thread.currentThread() + " trying to acquire the lock");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

        }
    }
}


class CLHLockTask implements Runnable {
    private final CLHLockDemo clhLockDemo = new CLHLockDemo();

    public void run() {
        try {
            clhLockDemo.lock();
            System.out.println("\n" + Thread.currentThread() + " acquired the lock");
        } finally {
            System.out.println(Thread.currentThread() + " release the lock");
            clhLockDemo.unlock();
        }
    }
}

class TestCLHLock {

    public static void main(String[] args) {
        CLHLockTask task = new CLHLockTask();
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            exec.execute(task);
        }

        exec.shutdown();
    }
}
