package apitest.json;

import java.util.List;

import com.alibaba.fastjson.JSONPath;

public class JsonPath {

	public static void main(String[] args) {
	    String jsonStr = "{\n" +
                "    \"store\": {\n" +
                "        \"bicycle\": {\n" +
                "            \"color\": \"red\",\n" +
                "            \"price\": 19.95\n" +
                "        },\n" +
                "        \"book\": [\n" +
                "            {\n" +
                "                \"author\": \"刘慈欣\",\n" +
                "                \"price\": 8.95,\n" +
                "                \"category\": \"科幻\",\n" +
                "                \"title\": \"三体\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"itguang\",\n" +
                "                \"price\": 12.99,\n" +
                "                \"category\": \"编程语言\",\n" +
                "                \"title\": \"go语言实战\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
//	    System.out.println(jsonStr);
//	    
//	    System.out.println(JSON.isValid(jsonStr));
//	    
//	
//	    //第一本书title
//        String title = (String) JSONPath.read(jsonStr, "store.book[0].title");
//       System.out.println(title);
////        
//       List<String> titles =  (List<String>) JSONPath.read(jsonStr, "store.book.title");
//        System.out.println(titles);
////        绝对路径
//       List<Double> prices =  (List<Double>) JSONPath.read(jsonStr, "$.store.book.price");
//        System.out.println(prices);
//       
////        //多层结构 相对路径
//        List<String> prices2 =  (List<String>) JSONPath.read(jsonStr, "..price");
//        System.out.println(prices2);
        
        String jsonString="{\"code\":\"1\",\"data\":[{\"name\":\"testfan0\",\"pwd\":\"pwd0\"},{\"name\":\"testfan1\",\"pwd\":\"pwd1\"},{\"name\":\"testfan2\",\"pwd\":\"pwd2\"}]}";
        
        Object value =   JSONPath.read(jsonString, "name");
        if(value==null) {
        	//全局找
        	value = JSONPath.read(jsonString, "..name");
        }
        
        if(value!=null) {
        	if(value instanceof List) {
        		  List<Object> list=(List)value;
					 for (int i = 0; i < list.size(); i++) {
						 System.out.println(list.get(i));
					  }
        	}else {
        		
        	}
        }
        System.out.println(value);
       

	}
}
