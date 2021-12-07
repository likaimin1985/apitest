package testng;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class ApiTestTestNg2 {


	@BeforeClass
	public void before() {
	}

	@Test()
  public void testCase() {
    System.out.println("测试测试" );
	}

	@AfterClass
  public void after() {
	}

}
