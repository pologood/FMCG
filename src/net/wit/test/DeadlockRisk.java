package net.wit.test;

/**
 * 线程死锁
 * Created by WangChao on 2016-9-22.
 */
public class DeadlockRisk extends Thread {
    private int flag = 1;
    private static Object obj1 = new Object(), obj2 = new Object();

    public void run() {
        System.out.println(Thread.currentThread().getName() + ":  flag=" + flag);
        if (flag == 1) {
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName() + ":  我已经锁定obj1，休息0.5秒后锁定obj2去！");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName() + ":  1");
                }
            }
        }
        if (flag == 2) {
            synchronized (obj2) {
                System.out.println(Thread.currentThread().getName() + ":  我已经锁定obj2，休息0.5秒后锁定obj1去！");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj1) {
                    System.out.println(Thread.currentThread().getName() + ":  0");
                }
            }
        }
    }

    public static void main(String[] args) {
        DeadlockRisk run01 = new DeadlockRisk();
        DeadlockRisk run02 = new DeadlockRisk();
        run01.flag = 1;
        run02.flag = 2;
        Thread thread01 = new Thread(run01, "Thread-1");
        Thread thread02 = new Thread(run02, "Thread-2");
        System.out.println("线程开始喽！");
        thread01.start();
        thread02.start();


//        DeadlockRisk run01 = new DeadlockRisk();
//        DeadlockRisk run02 = new DeadlockRisk();
//        run01.setName("Thread-1");
//        run02.setName("Thread-2");
//        run01.flag = 1;
//        run02.flag = 2;
//        System.out.println("线程开始喽！");
//        run01.start();
//        run02.start();
    }

}
