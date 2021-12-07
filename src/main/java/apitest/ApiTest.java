package apitest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONValidator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;
import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.google.protobuf.Value;

import utils.DbCheck;
import utils.DbCheckResult;
import utils.EmailUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;
import utils.StringToMap;

public class ApiTest {
	
	private static final String pattern = "\\$\\{(.*?)\\}"; //非贪婪
	
	private static final Pattern r = Pattern.compile(pattern);
	
	private static XmlMapper xmlMapper = new XmlMapper();
	
	
	public static void main(String[] args) {
	      
		//HttpClientUtils.openProxy=true;
		String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"apitest3.xlsx";
		try {
			List<TestCaseResult> allresult=new ArrayList<TestCaseResult>();
			List<Map<String, Object>> paraMaps= ExcelToMapUtils.importExcel(path, 1);
			for (Map<String, Object> map : paraMaps) {
				//1000组数据
				List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(path, TestCase.class);
				Collections.sort(list, (o1,o2)->o1.getOrder()-o2.getOrder());
				List<TestCase> list2 = list.stream().filter(d->d.getIsOpen().equalsIgnoreCase("是")).collect(Collectors.toList());
				list2.forEach(System.out::println);
				//map
				for (TestCase testcase : list2) {
					//1000 testcase
					TestCaseResult testCaseResult =new TestCaseResult();
					String result="";
					before_replace(testcase,map);
					if("get".equalsIgnoreCase(testcase.getType())) {
						result= HttpClientUtils.doGet(testcase.getUrl(), testcase.getHeader());
	                }else if ("post".equalsIgnoreCase(testcase.getType())) {
	                	result= HttpClientUtils.doPost(testcase.getUrl(), testcase.getParams(),testcase.getHeader());
					}else if ("postjson".equalsIgnoreCase(testcase.getType())) {
						result= HttpClientUtils.doPostJson(testcase.getUrl(), testcase.getParams(),testcase.getHeader());
					}else if ("getXml".equalsIgnoreCase(testcase.getType())) {
						result= HttpClientUtils.doGet(testcase.getUrl(),testcase.getHeader());
//						if(StringUtils.isNotBlank(testcase.getJsonResult())){
//							 Map xmlmap =xmlMapper.readValue(result, HashMap.class);
//							 result=JSON.toJSONString(xmlmap);
//						}
					}
					
					addjsonResult(testcase.getJsonResult(),result,map);
					addHtmlResult(testcase.getHtmlResult(),result,map);
					
					System.out.println("map--------- "+  map);
					after_replace(testcase,map);
					
					//检查点处理
					JsonCheckResult checkReulst=CheckPointUtils.check(result, testcase.getResultCheck());
					
					testCaseResult.setApicheckResult(checkReulst.getMsg());
					
					System.out.println(testcase);
					System.out.println(testcase.getResultCheck()+"---测试结果----"+checkReulst.isResult()+"  原因  "+checkReulst.getMsg());
					//数据库检查
					DbCheckResult dbcheck= DbCheck.check(testcase.getDbCheck());
					System.out.println(testcase.getDbCheck()+" 数据库检查-----"+dbcheck);
					testCaseResult.setDbcheckResult(dbcheck.getMsg());
					
					if(dbcheck.getFlag()&&checkReulst.isResult()) {
						testCaseResult.setAllResult("检查全部通过");
					}else {
						testCaseResult.setAllResult("检查不通过");
					}
					
					if(dbcheck==DbCheckResult.DbNotFoundCheck&&checkReulst==JsonCheckResult.SKIP) {
						testCaseResult.setAllResult("不需要检查");
					}
					
					//testCaseResult.setOrder(testcase.getOrder());
					BeanUtils.copyProperties(testCaseResult, testcase);
					allresult.add(testCaseResult);
				}
				ParamUtils.clear();
			}
			String result_path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"result_"+System.nanoTime()+".xlsx";
			ExcelUtils.getInstance().exportObjects2Excel(allresult, TestCaseResult.class, result_path);
			
			EmailUtils.sendEmailsWithAttachments("测试结果", "请查收附件", result_path);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	//title=<title>(.+?)</title>
	private static void addHtmlResult(String checkhtmlResult, String result, Map<String, Object> map) {
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
				    		  map.put(k+"_g"+count, m.group(1));
				    	  }else {
				    		 //只有一个
				    		  map.put(k, m.group(1)); 
				    		 
				    	  }
				      }
				      // 补齐第一个
				      //_g1
				      if(count>1) {
				    	  map.put(k+"_g1", map.get(k));
				      }
				});
			  
		  }
		
	}


	private static void addjsonResult(String addjson, String result,Map<String, Object> map) {
//		if(JSONValidator.from(result).validate()) {
//			
//		}
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
						  map.put(k+"_g"+(i+1), list.get(i));  
					  }
					  //默认给第一个
					  if(!list.isEmpty()) {
						  map.put(k, list.get(0));  
					  }
				   }else {
					   map.put(k, value);  
				   }
			   }
			});
		}
	}


	private static void before_replace(TestCase testcase,Map<String, Object> map) {
	    testcase.setUrl(matcher(testcase.getUrl(),map));
	    testcase.setParams(matcher(testcase.getParams(),map));
	    testcase.setHeader(matcher(testcase.getHeader(),map));
	}
	
	private static void after_replace(TestCase testcase,Map<String, Object> map) {
		  testcase.setDbCheck(matcher(testcase.getDbCheck(),map));
	}
	
	private static String matcher(String str,Map<String, Object> map) {
		if(StringUtils.isNoneBlank(str)) {
			Matcher m = r.matcher(str);
		    while (m.find()) {
		    	  System.out.println("Found value: " + m.group(0) );
		    	  System.out.println("Found value: " + m.group(1) );
		    	  str=str.replace(m.group(0),  MapUtils.getString(map,String.valueOf(m.group(1)),""));
		      } 	
		}
	    return str;
	}

}
