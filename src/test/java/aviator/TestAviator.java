package aviator;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;

public class TestAviator {
	
	public static void main(String[] args) {
		System.out.println("1+2+3+4");
		
		Long result = (Long) AviatorEvaluator.execute("1+2+3+4");
		System.out.println(result);
		
		
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("data1", 1);
		//env.put("data2", 1);
		Boolean result1 = (Boolean) AviatorEvaluator.execute("$.data1==1", env);
		System.out.println(result1);
		
	}

}
