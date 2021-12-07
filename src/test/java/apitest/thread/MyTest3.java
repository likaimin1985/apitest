package apitest.thread;

public class MyTest3 {
	
	public static void main(String[] args) {
	        
	        Thread thread=new Thread() {

				@Override
				public void run() {
					  pong();
				}
	        	
	        };
	        thread.run();
	        //主程序不等了
	        
	        System.out.println(Thread.currentThread().getName()+"  ping");
	}
	
	static void pong() {
        System.out.println(Thread.currentThread().getName() + "  pong");
}
	

}
