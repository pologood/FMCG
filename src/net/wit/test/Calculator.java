package net.wit.test;

/**
 *
 * Created by WangChao on 2016-9-23.
 */
public class Calculator extends Thread{
    int total;

    @Override
    public void run() {
        synchronized (this){
            for (int i=1;i<=100;i++){
                total+=i;
            }
        }
        notifyAll();
    }


}

class ReadResult extends Thread{
    Calculator c;
    public ReadResult(Calculator c){
        this.c=c;
    }

    @Override
    public void run() {
        synchronized (c){
            System.out.println(Thread.currentThread().getName()+":  等待计算结果。。。");
            try {
                c.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":  计算结果为："+c.total);
        }
    }

    public static void main(String[] args){
        Calculator calculator=new Calculator();
        new ReadResult(calculator).start();
        new ReadResult(calculator).start();
        new ReadResult(calculator).start();
        calculator.start();
    }
}