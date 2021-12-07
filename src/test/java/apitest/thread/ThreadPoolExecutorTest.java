package apitest.thread;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutorTest {
	
	public static void main(String[] args) {
		
//		for (int i = 0; i < 1000; i++) {
//			new Thread() {
//				@Override
//				public void run() {
//					   System.out.println(Thread.currentThread().getName());		
//				}
//			}.start();
//		}
		
//		 ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//		 
//		 for (int i = 0; i < 1000; i++) {
//			 cachedThreadPool.execute(new Thread() {
//
//				@Override
//				public void run() {
//					System.out.println(Thread.currentThread().getName());	
//				}
//				 
//			 });
//		}
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
			}
		}).start();
		
		
		new Thread(()->{
			System.out.println("11111");
		}).start(); 
		 
		 ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		 for (int i = 0; i < 1000; i++) {
//			 fixedThreadPool.execute(new Thread() {
//
//				@Override
//				public void run() {
//					System.out.println(Thread.currentThread().getName());	
//				}
//				 
//			 });
			 
			 fixedThreadPool.execute(()->{
				 System.out.println(Thread.currentThread().getName());
			 });
		}
		 
		 
		 
		 fixedThreadPool.shutdown();
		 
//		 StringBuffer buffer=new StringBuffer();
//		 
//		 buffer.append(b)
		 
	}

}
