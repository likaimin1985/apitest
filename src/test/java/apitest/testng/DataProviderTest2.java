package apitest.testng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest2 {
	
	
	@Test(dataProvider = "mytest")
    public void parameterIntTest(TestBean  bean, String index) {
       System.out.println("Parameterized Number is : " + bean.getName());
       System.out.println("Parameterized Number is : " + bean.getMsg());
       System.out.println("index"+index);
    }

    @DataProvider(name = "mytest")
    public Iterator<Object[]> parameterIntTestProvider() {
    	List<Object[]> dataProvider = new ArrayList<Object[]>();
    	for (int i = 0; i < 10; i++) {
    		TestBean bean = new TestBean();
    		bean.setName("testname");
    		bean.setMsg("--"+i);
    		dataProvider.add(new Object[] { bean,"i"+i});  //List<Object[2]>
		}
       return dataProvider.iterator();
    }

}
