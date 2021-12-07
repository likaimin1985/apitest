package apitest.json;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class FastJsonTest {
	
	public static void main(String[] args) {
		
		    String json="{\"msg\":\"登录成功\",\"uid\":\"9CC972DFA2D4481F89841A46FD1B3E7B\",\"code\":\"1\"}";
		
		    
//		   JSONObject jsonObject= (JSONObject) JSON.parse(json);  //map
//		   System.out.println(jsonObject.get("msg"));
//		   System.out.println(jsonObject.get("uid"));
//		   System.out.println(jsonObject.get("code"));
		   
		   String json2="{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"}]}";
		   JSONObject jsonObject2= (JSONObject) JSON.parse(json2);  //map
		   System.out.println(jsonObject2.get("code"));
		   System.out.println(jsonObject2.get("data"));
		   
		   JSONArray jsonArray=  (JSONArray) jsonObject2.get("data");
		   for (int i = 0; i < jsonArray.size(); i++) {
			   JSONObject jsonobj= (JSONObject) jsonArray.get(i);
			   System.out.println(jsonobj.get("name"));
			   
		     }	   
		   
		   System.out.println(JSONPath.read(json, "uid"));
		   //		
//		    String jsonString1 = "{\"param5\":\"value5\",\"param3\":\"value3\",\"param4\":\"value4\",\"param1\":\"value1\",\"param2\":\"value2\"}"; 
//		    
//	        JSONObject object = (JSONObject) JSON.parse(jsonString1);
//	        System.out.println(object);
//	        
//	        System.out.println(object.get("param5"));
	        
//	        String jsonString2 = "[{\"param5\":\"value5\",\"param3\":\"value3\",\"param4\":\"value4\",\"param1\":\"value1\",\"param2\":\"value2\"},"
//	        		+ "{\"p1\":\"v1\",\"p2\":\"v2\",\"p3\":\"v3\",\"p4\":\"v4\",\"p5\":\"v5\"}]";  
//	        JSONArray arry=  JSON.parseArray(jsonString2);
//	        for (int i = 0; i < arry.size(); i++) {
//	        	JSONObject obj = (JSONObject) arry.get(i);
//	        	System.out.println(obj);
//			}
         
	        //解析有规律的
	        String jsonString3 = "[{\"age\":12,\"date\":1465475917155,\"name\":\"s1\"},"
	        		+ "{\"age\":12,\"date\":1465475917175,\"name\":\"s2\"},"
	        		+ "{\"age\":12,\"date\":1465475917175,\"name\":\"s3\"},"
	        		+ "{\"age\":12,\"date\":1465475917175,\"name\":\"s4\"},"
	        		+ "{\"age\":12,\"date\":1465475917175,\"name\":\"s5\"},"
	        		+ "{\"age\":12,\"date\":1465475917175,\"name\":\"s6\"}]";
	        
	        List<Student> studentList = JSON.parseArray(jsonString3,Student.class); 
	        for (Student student : studentList) {
				System.out.println(student);
			//	System.out.println(JSON.toJSONString(student));
			}
	        
	        System.out.println(JSON.toJSONString(studentList));
	        
            //对象转json
//	        String jsonString = JSON.toJSONString(studentList);
//	        System.out.println("javabean to json"+jsonString);
//	       String dateFormat =  JSON.toJSONStringWithDateFormat(studentList, "yyyy-MM-dd HH:mm:ss");
//	       System.out.println("dataformat "+ dateFormat);
	        
	       //如果没有对象
//	        JSONObject jsonObject = new JSONObject();
//	        jsonObject.put("zhangsan", "test");
//	        jsonObject.put("age", "100");
//	        System.out.println(jsonObject.toJSONString());
	        
	}

}
