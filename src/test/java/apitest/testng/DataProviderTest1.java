package apitest.testng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest1 {
	
	
	//行数
	@Test(dataProvider = "testdb")
    public void parameterIntTest(Class clzz, String str) {
       System.out.println("Parameterized Number is : " + clzz);
       System.out.println("Parameterized Number is : " + str);
    }

	//定义3行2列数据
    //This function will provide the patameter data
    @DataProvider(name = "testdb")
    public Object[][] parameterIntTestProvider() {
        return new Object[][]{
                   {Vector.class, "test1"},
                   {String.class, "test2"},
                   {Integer.class, "test3"}
                  };
    }
    
    public static void main(String[] args) {
		List<Object[]> list =new ArrayList<Object[]>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			
		}
	}

}
