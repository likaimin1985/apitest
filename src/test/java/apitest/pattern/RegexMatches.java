package apitest.pattern;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.MapUtils;

public class RegexMatches {
	 public static void main( String args[] ){
		 
	      // 按指定模式在字符串查找
	     // String line = "This order was placed for QTIMY3000! OK?";
	      //D*d+.*
	      //String pattern = "(\\D*)(\\d+)(.*)";
	      //String pattern = "(\\d+)(.*)";
//	 
     //  String pattern = "Q(.*)3(\\d+)";
//	      // 创建 Pattern 对象
	   //   Pattern r = Pattern.compile(pattern);
//	 
//	      // 现在创建 matcher 对象
//	      Matcher m = r.matcher(line);
//	      if (m.find( )) {
//	    	  //所有表达式匹配的
//	         System.out.println("Found value: " + m.group(0) );
//	         System.out.println("Found value: " + m.group(1) );
//	         System.out.println("Found value: " + m.group(2) );
//	         //System.out.println("Found value: " + m.group(3) ); 
//	      } else {
//	         System.out.println("NO MATCH");
//	      }
	      
	    
	      
		// test();
	      //http://www.baidu.com?pid=${id}
//		 if(true) {
//			 System.out.println("1111111");
//		 }
		 
//		 while(true) {
//			 System.out.println("1111111");	 
//		 }
	      
	      test1();
	   }
	 
	 private static void test1() {
           //String urlString="http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=${loginname}&loginpass=${loginpass}";
	      
           String urlString="http://118.24.13.38:8080/goods/UserServlet?method=loginMobile&loginname=${loginname}&loginpass=${loginpass}";
           Map<String,Object> map=new HashMap<String, Object>();
           map.put("loginname", "test1");
        //   map.put("loginpass", "abc");
           
	      String pattern = "\\$\\{(.*?)\\}"; //非贪婪
	      Pattern r = Pattern.compile(pattern);
	      
	      Matcher m = r.matcher(urlString);
	      while (m.find()) {
	    	  System.out.println("Found value: " + m.group(0) );
	    	  System.out.println("Found value: " + m.group(1) );
	    	  urlString=urlString.replace(m.group(0), MapUtils.getString(map,String.valueOf(m.group(1)),""));
	    	  System.out.println("分组");
	      }
	      System.out.println(urlString);
	}
	 
//	private static void test() {
//		//add
//		 Map<String, Object> map = new HashMap<String, Object>();
//		 //map
//	     map.put("id", "uuid0000");
//	     map.put("uid", "testuid");
//	     
//	      //替换逻辑
//	     String url="http://www.baidu.com?pid=${id}&test=${uid}";
//	     String pattern_baidu = "\\$\\{(.+?)\\}";
//	     Pattern r2 = Pattern.compile(pattern_baidu);
//	     Matcher m2 = r2.matcher(url);
//	     while(m2.find()) {
//	    	     System.out.println("Found value: " + m2.group() );
//		         System.out.println("Found value: " + m2.group(1) );
//		         url=url.replace(m2.group(), map.get(m2.group(1)).toString());    
//	     }
//	      System.out.println(url);
//	}
}
