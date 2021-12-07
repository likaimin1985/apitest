package utils;

import static org.testng.Assert.assertEquals;

import java.util.Vector;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;

public class HttpClientUtilsTest {
	
	@BeforeClass
	public void before() {
		HttpClientUtils.openProxy=false;
	}
	
	@Test(dataProvider = "testparameter")
	public void doGetTest(String loginname,String loginpass,String check) {
		String testurl = "http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname="+loginname+"&loginpass="+loginpass+"";
		String result = HttpClientUtils.doGet(testurl);
		System.out.println(result);
		JsonCheckResult checkResult = CheckPointUtils.check(result, check);
		assertEquals(checkResult.isResult(), true);
	}
	
	 @DataProvider(name = "testparameter")
	 public Object[][] parameterIntTestProvider() {
	        return new Object[][]{
	                   {"abc", "abc","code=1"},
	                   {"test1", "test1","code=1"},
	                   {"test111", "test311","code=2"},
	                   {"test2", "test2","code=2"}
	                  };
	    }

}
