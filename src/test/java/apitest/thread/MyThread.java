package apitest.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


class Mytest5 implements Runnable {

	@Override
	public void run() {
		
	}
	
}
public class MyThread extends Thread{
	
	private String threadName;
	
	CountDownLatch latch;
	

	public MyThread(String threadName, CountDownLatch latch) {
		super();
		this.threadName = threadName;
		this.latch = latch;
	}

	public MyThread(String threadName) {
		super();
		this.threadName = threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println(threadName+"  _____"+i);
		}
		
		latch.countDown(); //--1
//		while(true) {
//			
//		}
	}
	
	public static void main(String[] args) {
		System.out.println("开始");
		//int count=10;
		CountDownLatch latch = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			String name="thread"+i;
			//10跑道，cpu并行执行 run
			MyThread myThread=new MyThread(name,latch); 
			myThread.setName(name);
			myThread.start();
		}
		//堵塞
		try {
			//latch==0
			latch.await(1,TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//主程序等待线程吗？
		System.out.println("结束");
		
	}

}
