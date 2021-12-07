package utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StringToMap {

	private static Map<String, Object> convertToMap(String str,String regx) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(StringUtils.isNotBlank(str)) {
			String[] str_array = str.split(regx);
			for (int i = 0; i < str_array.length; i++) {
				String key=str_array[i];
				System.out.println("key "+key);
				String[] key_array=key.split("=");
				if(key_array.length>1) {
					String allvalue="";
				    for (int j = 1; j < key_array.length; j++) {
				    	if(j<key_array.length-1) {
				    		allvalue+=key_array[j]+"=";
				    	}else {
				    		allvalue+=key_array[j];
				    	}
					}
					map.put(key_array[0], allvalue);
				}
			}
		}
		return map;
	}

	//method=loginMobile&loginname=test1&loginpass=test1
	public static Map<String, Object>  covert1(String str) {
		return convertToMap(str,"&");
	}

	//token=61b3590090982a0185dda9d3bd793b46;userId=123
	public static Map<String, Object>  covert2(String str) {
		return convertToMap(str,";");
	}

	public static void main(String[] args) {
		//System.out.println(covert2("src=<img hidefocus=true src=(.+?) height=129>"));

		System.out.println(covert2("title=<title>=(.+?)</title>"));

//		System.out.println(covert2("id2=uid;code=code"));
	}



}
