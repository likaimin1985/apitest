package apitest.thread;

import java.util.ArrayList;
import java.util.List;

public class JavaHeapTest {
	
	public static void main(String[] args) {
		List list=new ArrayList();
		while (true) {
			System.out.println("我还活着");
			list.add(new JavaHeapTest());
		}
	}

}
