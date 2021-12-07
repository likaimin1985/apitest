package apitest.thread;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThreadLocalMapTest {
	
	//private static Map<String, Object> correlation =new LinkedHashMap<String, Object>();
	
	static ThreadLocal<Map<String, Object>> threadLocal =new ThreadLocal<Map<String,Object>>(){

		@Override
		protected Map<String, Object> initialValue() {
			return new LinkedHashMap<String, Object>();
		}
		
	};
	
	static ThreadLocal<Integer> threadLocal2 =new ThreadLocal<Integer>(){

//		@Override
//		protected Integer initialValue() {
//			return new 
//		}

		
	};
	
	
	
//	static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>(){
//
//		@Override
//		protected Map<String, Object> initialValue() {
//			return new HashMap<String, Object>();
//		}
//	};
	
	public static void main(String[] args) {
		//主方法是主线程
		//线程本地变量  （Thread  T ->）
		//System.out.println("thread---"+Thread.currentThread().getName());
		threadLocal.get().put("1","11");
		
		//correlation.put("1", "11");
		dosomethings();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
//				System.out.println("thread---"+Thread.currentThread().getName());
//				System.out.println(threadLocal.get().get("1"));
				//线程内部
				threadLocal.get().put("1","22");
				
				//correlation.put("1", "22");
				dosomethings();
			}
		}).start();
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				threadLocal.get().put("2","33");
				threadLocal.get().put("1","44");
				
				//correlation.put("1", "33");
				dosomethings();
			}
		}).start();
		
    // System.out.println(threadLocal.get().get("1"));
	}
	
	private static void dosomethings() {
		System.out.println("dosomethings  "+Thread.currentThread().getName() + "  " +threadLocal.get());
	}

}
