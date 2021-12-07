package apitest.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlTest {

	public static void main(String[] args) {
		 XmlMapper xmlMapper = new XmlMapper();
		 try {
//			 User user=new User(1,"lucas","nan","2019-10-01","上海");
//		     String xml_user=xmlMapper.writeValueAsString(user);
//		     System.out.println(xml_user);
			 
			 String xml_user="<User><id>1</id><username>lucas</username><sex>nan</sex><birthday>2019-10-01</birthday><address>上海</address></User>";
		     
		     User userObject=xmlMapper.readValue(xml_user,User.class);
		     System.out.println(userObject);
		     
		     //xml 格式数据
			 String xml="<returnsms>" + 
			 		" 	<returnstatus>Success</returnstatus>" + 
			 		" 	<message>ok</message>" + 
			 		" 	<remainpoint>11032</remainpoint>" + 
			 		" 	<taskID>4354699</taskID>" + 
			 		" 	<test>123</test>" + 
			 		" 	<successCounts>1</successCounts>"+
			 		"</returnsms>";
			 //转json
			 Map map =xmlMapper.readValue(xml, HashMap.class);
			 System.out.println(map);
			 System.out.println(JSON.toJSONString(map));
			 //xml如何转json?
			  
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
