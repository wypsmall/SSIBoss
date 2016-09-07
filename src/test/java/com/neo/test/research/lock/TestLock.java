package com.neo.test.research.lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by neowyp on 2016/9/6.
 * Author   : wangyunpeng
 * Date     : 2016/9/6
 * Time     : 13:33
 * Version  : V1.0
 * Desc     : 测试独占锁
 */
@Slf4j
public class TestLock {

    /**
     * 程序主入口
     * 当前程序运行会出现死锁的情况，当生产者、消费者一方运行结束，
     * 而另一方触发条件await()，那么线程将无法退出
     * 例如：
     * productor执行25次，线程退出
     * customer指定到19次时，当前库存为19，需要消费25，如果扣减则库存为负值那么就会触及down.await();
     * 由于productor实例已经退出，其中的down.signal();永远不会被调用，所以customer处于死锁状态
     *
     * 当前模型存在另外一个漏洞，如果随机增加库存是大于库存上限的，那么还会有一种情况进入死锁
     * 当前库存为50，增加102的库存，同时扣减103的库存，两个条件都被触发，但是都无法释放
     * 想复现只要将随机取值调整为150，多运行几次就一定可以复现
     *
     * 特别声明，condition.await()是否释放锁，并且中断当前线程，理解为隐式unlock
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        //初始化独占锁
        ReentrantLock reentrantLock = new ReentrantLock();
        //初始化库存上限，比如库存不能大于100
        Condition upCondition = reentrantLock.newCondition();
        //初始化库存下限，比如库存不能小于0
        Condition downCondition = reentrantLock.newCondition();
        //实例化Store对象
        Store store = new Store();
        //设定线程内循环调用的次数
        int count = 25;

        //初始化生产者，并启动线程
        Productor productor = new Productor(reentrantLock, upCondition, downCondition, store, count);
        productor.start();

        //初始化消费者，并启动线程
        Customer customer = new Customer(reentrantLock, upCondition, downCondition, store, count);
        customer.start();
    }
}

/**
 * 库存对象，封装库存数量
 */
@Data
class Store {
    //当前库存值，初始化0
    private int curData = 0;

    public void add(int value) {
        curData += value;
    }

    public void sub(int value) {
        curData -= value;
    }
}

/**
 * 生产者对象
 */
@Slf4j
class Productor extends Thread {
    private ReentrantLock lock;
    private Condition up;
    private Condition down;
    private Store store;
    private int count = 10;

    public Productor() {
    }

    public Productor(ReentrantLock lock, Condition up, Condition down, Store store, int count) {
        this.setName("Productor");
        this.lock = lock;
        this.store = store;
        this.up = up;
        this.down = down;
        this.count = count;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < count) {

            try {
//                Thread.sleep(10);
                lock.lock();
                int cur = this.store.getCurData();
                int value = new Random(System.currentTimeMillis()).nextInt(50);
                //当预加值+当前值大于库存上限是，触发up.await
                if ((cur + value) > 100) {
                    log.info("Product-waiting-{} customer {}-{}", i, cur, value);
                    up.await();
                    log.info("Product-waiting-{} unReach {}-{}", i, cur, value);
                }
                this.store.add(value);
                log.info("Product-{} {}-{}-{}", i, cur, value, this.store);
                i++;
                //每次库存增加后，都会触发down激活
                down.signal();
            } catch (Exception e) {
                log.error("", e);
            } finally {
                lock.unlock();
                log.info("Productor release lock");
            }
        }
    }
}

/**
 * 消费者对象
 */
@Slf4j
class Customer extends Thread {
    private ReentrantLock lock;
    private Condition up;
    private Condition down;
    private Store store;
    private int count = 10;

    public Customer() {
    }

    public Customer(ReentrantLock lock, Condition up, Condition down, Store store, int count) {
        this.setName("Customer");
        this.lock = lock;
        this.store = store;
        this.up = up;
        this.down = down;
        this.count = count;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < count) {

            try {
//                Thread.sleep(10);
                lock.lock();
                int cur = this.store.getCurData();
                int value = new Random(System.currentTimeMillis()).nextInt(50);
                //当库存不够扣减时触发条件down.await
                if (cur < value) {
                    log.info("Customer-waiting-{} productor {}-{}", i, cur, value);
                    down.await();
                    log.info("Customer-waiting-{} unReach {}-{}", i, cur, value);
                }
                this.store.sub(value);
                log.info("Customer-{} {}-{}-{}", i, cur, value, this.store);
                i++;
                //当库存能被扣减后都去触发上限条件激活
                up.signal();
            } catch (Exception e) {
                log.error("", e);
            } finally {
                lock.unlock();
                log.info("Customer release lock");
            }
        }
    }
}
