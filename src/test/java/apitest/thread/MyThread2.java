package apitest.thread;


class MyTask2 extends Thread{

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println(Thread.currentThread().getName()+"   "+i);
		}
		
	}
	
}


public class MyThread2 {
	
	//开启进程
	public static void main(String[] args) {
		//main 主线程
		System.out.println(Thread.currentThread().getName());
		System.out.println("开始");
		//单独执行，线程属于进程，跑道 cpu
		for (int i = 0; i < 10; i++) {
			new MyTask2().start();
		}
		MyTask2 myTask2= new MyTask2();
		myTask2.setPriority(10);
		myTask2.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()+"任务2 ");
		System.out.println("结束");
		
		
		
		
		
	}

}
