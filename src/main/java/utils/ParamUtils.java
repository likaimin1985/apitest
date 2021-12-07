package utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.cj.jdbc.exceptions.MySQLQueryInterruptedException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

public class ParamUtils {
	//全局 并行
	//private static Map<String, Object> correlation =new LinkedHashMap<String, Object>();

	private static ThreadLocal<Map<String, Object>> correlation =new ThreadLocal<Map<String,Object>>(){

		@Override
		protected Map<String, Object> initialValue() {
			return new LinkedHashMap<String, Object>();
		}

	};

	public static Map<String, Object> getCorrelation() {
		return correlation.get();
	}

	public static void clear() {
		getCorrelation().clear();
	}


	public static void addKeyValue(String key,Object value) {
		getCorrelation().put(key, value);



	public static void addFromMap(Map<String, Object> map) {
		if(MapUtils.isNotEmpty(map)) {
			map.forEach((k,v)->{
				getCorrelation().put(k, v);
			});
		}

	}

  public static void main(String[] args) {
    addPatternResult("title=<title>=(.+?)</title>","title=<title>=123321</title>");
    System.out.println("correlation = " + correlation.get());
  }

	public static void addPatternResult(String checkhtmlResult, String result) {
		  if(StringUtils.isNotBlank(checkhtmlResult)) {
				Map<String, Object> htmlmap = StringToMap.covert2(checkhtmlResult);
				htmlmap.forEach((k,v)->{
					System.out.println("htmlmap------"+k+"  "+v);
					   String pattern = v.toString(); //非贪婪
				      Pattern r = Pattern.compile(pattern);
				      Matcher m = r.matcher(result);
				       int count=0;
				      while (m.find()) {
				    	  count++;
				    	  System.out.println("正则提取的值group0 "+m.group());
				    	  System.out.println("正则提取的值group1 "+m.group(1));
				    	  //匹配多个
				    	  if(count>1) {
				    		  getCorrelation().put(k+"_g"+count, m.group(1));
				    	  }else {
				    		 //只有一个
				    		  getCorrelation().put(k, m.group(1));
				    	  }
				      }
				      // 补齐第一个
				      //_g1
				      if(count>1) {
				    	  getCorrelation().put(k+"_g1", getCorrelation().get(k));
				      }
				});

		  }

	}

	public static void addjsonResult(String addjson, String result) {
		if(JSON.isValid(result)) {
			Map<String, Object> jsonmap = StringToMap.covert2(addjson);
			jsonmap.forEach((k,v)->{

			  Object value=	JSONPath.read(result, String.valueOf(v));
			  if(value==null) {
				  //如果当前没有，全局找
				  value=JSONPath.read(result, ".."+String.valueOf(v));
			  }
			   if(value!=null) {
				   if(value instanceof List) {
					  List<Object> list=(List<Object>)value;
					  for (int i = 0; i < list.size(); i++) {
						  getCorrelation().put(k+"_g"+(i+1), list.get(i));
					  }
					  //默认给第一个
					  if(!list.isEmpty()) {
						  getCorrelation().put(k, list.get(0));
					  }
				   }else {
					   getCorrelation().put(k, value);
				   }
			   }
			});
		}
	}



	private static final String pattern = "\\$\\{(.*?)\\}"; //非贪婪

	private static final Pattern r = Pattern.compile(pattern);

	public static String matcher(String str) {
		if(StringUtils.isNoneBlank(str)) {
			Matcher m = r.matcher(str);
		    while (m.find()) {
		    	  System.out.println("Found value: " + m.group(0) );
		    	  System.out.println("Found value: " + m.group(1) );
		    	  str=str.replace(m.group(0),  MapUtils.getString(correlation.get(),String.valueOf(m.group(1)),""));
		      }
		}
	    return FunctionUtils.functionHandler(str);
	}

	public static String matcher2(String str) {
		if(StringUtils.isNoneBlank(str)) {
			Matcher m = r.matcher(str);
		    while (m.find()) {
		    	  System.out.println("Found value: " + m.group(0) );
		    	  System.out.println("Found value: " + m.group(1) );
		    	  str=str.replace(m.group(0),  MapUtils.getString(correlation.get(),String.valueOf(m.group(1)),""));
		      }
		}
	    return str;
	}




}
