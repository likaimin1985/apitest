package apitest.function;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import utils.ParamUtils;

public class FunctionTest {

	public static void main(String[] args) {

//		String str="http://www.baidu.com?id=#{__UUID}&time=#{__time}&test=#{__tt}";
//		
//		//数据替换  //#{__UUID}    #{__time}  提取出来
//	    String pattern = "#\\{(.+?)\\}"; //非贪婪
//	      Pattern r = Pattern.compile(pattern);
//	      
//	      Matcher m = r.matcher(str);
//	      while (m.find()) {
//	    	  System.out.println("Found value: " + m.group(0) );
//	    	  System.out.println("Found value: " + m.group(1) );
//	    	 String functionValue= getFunctionValue(m.group(1));
//	    	 str=str.replace(m.group(0), functionValue);
////	    	  System.out.println("分组");
//	      }	
//	      System.out.println(str);
		// 第一步参数化支持
		ParamUtils.addKeyValue("loginname", "abc");
		ParamUtils.addKeyValue("loginpass", "abc");
		ParamUtils.addKeyValue("username", "test");
		String url = "http://www.baidu.com?id=#{__UUID}&time=#{__time}&userId=#{__md5(${loginname},${loginpass},${username})}";
		url = ParamUtils.matcher(url);
		System.out.println(url);

		String pattern = "#\\{(.+?)\\}"; // 非贪婪
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(url);
		while (m.find()) {
			System.out.println("Found value: " + m.group(0));
			System.out.println("Found value: " + m.group(1));
			String functionValue = getFunctionValue(m.group(1));
			url = url.replace(m.group(0), functionValue);
//	    	  System.out.println("分组");
		}

		System.out.println(url);

	}

	private static String getFunctionValue(String functionKey) {
		if ("__UUID".equalsIgnoreCase(functionKey)) {
			return UUID.randomUUID().toString();
		} else if ("__time".equalsIgnoreCase(functionKey)) {
			return "" + System.currentTimeMillis();
		} else if (functionKey.startsWith("__md5")) {
			// System.out.println("md5");
			// 提取参数
			// 例如__md5(test,abc,test)
			// __md5(test,abc)
			// __md5(test)
			//test,abc,test->""testabctest
			String args = getFunctionArgs(functionKey);
			if (StringUtils.isNotBlank(args)) {
				try {
					return DigestUtils.md5Hex(args.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	private static String pattern = "\\((.+?)\\)"; // 非贪婪
	private static Pattern r = Pattern.compile(pattern);

	private static String getFunctionArgs(String functionKey) {
		//String[] args = null;
		Matcher m = r.matcher(functionKey);
		if (m.find()) {
			//args = m.group(1).split(",");
			return m.group(1).replace(",", "");
		}
//		if (args != null && args.length > 1) {
//			String str = "";
//			for (int i = 0; i < args.length; i++) {
//				String string = args[i];
//				str += string;
//			}
//			return str;
//		}
		return "";
	}

}
