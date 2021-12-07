package apitest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
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
import utils.FunctionUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;
import utils.StringToMap;
import org.apache.commons.collections4.MapUtils;


class MyTask4 extends Thread{
	
	private Map<String, Object> map;
	private CountDownLatch latch;
	private static final Logger logger = LoggerFactory.getLogger(MyTask4.class);
	
	
	

	public MyTask4(Map<String, Object> map, CountDownLatch latch) {
		super();
		this.map = map;
		this.latch = latch;
	}



	public MyTask4(Map<String, Object> map) {
		super();
		this.map = map;
	}



	@Override
	public void run() {
		ParamUtils.addFromMap(map);
		logger.info("Thread.currentThread() {} {}", Thread.currentThread().getName(),ParamUtils.getCorrelation());
		System.out.println(Thread.currentThread().getName());
		List<TestCaseResult> allCaseResults=new ArrayList<TestCaseResult>();
		try {
			List<TestCase> list = ExcelUtils.getInstance().readExcel2Objects(ApiTestThread4.path, TestCase.class);
			Collections.sort(list, (o1,o2)->o1.getOrder()-o2.getOrder());
			List<TestCase> list2 = list.stream().filter(d->d.getIsOpen().equalsIgnoreCase("是")).collect(Collectors.toList());
			list2.forEach(System.out::println);
			//map
			for (TestCase testcase : list2) {
				//1000 testcase
				TestCaseResult testCaseResult =new TestCaseResult();
				String result="";
				ApiTestThread4.before_replace(testcase);
				if("get".equalsIgnoreCase(testcase.getType())) {
					result= HttpClientUtils.doGet(testcase.getUrl(), testcase.getHeader());
	            }else if ("post".equalsIgnoreCase(testcase.getType())) {
	            	result= HttpClientUtils.doPost(testcase.getUrl(), testcase.getParams(),testcase.getHeader());
				}else if ("postjson".equalsIgnoreCase(testcase.getType())) {
					result= HttpClientUtils.doPostJson(testcase.getUrl(), testcase.getParams(),testcase.getHeader());
				}else if ("getXml".equalsIgnoreCase(testcase.getType())) {
					result= HttpClientUtils.doGet(testcase.getUrl(),testcase.getHeader());
				}
				
				ParamUtils.addjsonResult(testcase.getJsonResult(), result);
				ParamUtils.addPatternResult(testcase.getHtmlResult(), result);
//				ApiTestThread.addjsonResult(testcase.getJsonResult(),result,map);
//				ApiTestThread3.addHtmlResult(testcase.getHtmlResult(),result,map);
				
				System.out.println("map--------- "+  map);
				ApiTestThread4.after_replace(testcase);
				
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
				//ApiTestThread3.allresult.add(testCaseResult);
				
				allCaseResults.add(testCaseResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("系统出错了{ }", e);
		}finally {
			ApiTestThread4.allresult.addAll(allCaseResults);
			latch.countDown();
			ParamUtils.clear();
		}
		
	}
	
}

public class ApiTestThread4 {
	
	
	private static XmlMapper xmlMapper = new XmlMapper();
	
	public static String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"apitest3.xlsx";
	private static final Logger logger = LoggerFactory.getLogger(ApiTestThread4.class);
	
	public static List<TestCaseResult> allresult=new ArrayList<TestCaseResult>();
	
	
	public static void main(String[] args) {
	      
		HttpClientUtils.openProxy=true;
		logger.info("开始测试");
		try {
			List<Map<String, Object>> paraMaps= ExcelToMapUtils.importExcel(path, 1);
			CountDownLatch latch = new CountDownLatch(paraMaps.size());
			//采用线程池对线程优化
		    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
			for (Map<String, Object> map : paraMaps) {
				//main
				//ParamUtils.addFromMap(map);
				//new MyTask3(map,latch).start();
				fixedThreadPool.execute(new MyTask4(map,latch));
			}
			latch.await(3,TimeUnit.MINUTES);
			//主程序等待吗
			String result_path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"result_"+System.nanoTime()+".xlsx";
			ExcelUtils.getInstance().exportObjects2Excel(allresult, TestCaseResult.class, result_path);
			
			EmailUtils.sendEmailsWithAttachments("测试结果", "请查收附件", result_path);
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			allresult.clear();
		}
		
	}
	

	//title=<title>(.+?)</title>
	


//	public static void addjsonResult(String addjson, String result,Map<String, Object> map) {
//		if(JSON.isValid(result)) {
//			Map<String, Object> jsonmap = StringToMap.covert2(addjson);
//			jsonmap.forEach((k,v)->{
//				
//			  Object value=	JSONPath.read(result, String.valueOf(v));
//			  if(value==null) {
//				  //如果当前没有，全局找
//				  value=JSONPath.read(result, ".."+String.valueOf(v));
//			  }
//			   if(value!=null) {
//				   if(value instanceof List) {
//					  List<Object> list=(List<Object>)value;
//					  for (int i = 0; i < list.size(); i++) {
//						  map.put(k+"_g"+(i+1), list.get(i));  
//					  }
//					  //默认给第一个
//					  if(!list.isEmpty()) {
//						  map.put(k, list.get(0));  
//					  }
//				   }else {
//					   map.put(k, value);  
//				   }
//			   }
//			});
//		}
//	}


	public static void before_replace(TestCase testcase) {
	    testcase.setUrl(ParamUtils.matcher(testcase.getUrl()));
	    testcase.setParams(ParamUtils.matcher(testcase.getParams()));
	    testcase.setHeader(ParamUtils.matcher(testcase.getHeader()));
	}
	
//	public static void before_replace2(TestCase testcase) {
//	    testcase.setUrl(FunctionUtils.functionHandler(testcase.getUrl()));
//	    testcase.setParams(FunctionUtils.functionHandler(testcase.getParams()));
//	    testcase.setHeader(FunctionUtils.functionHandler(testcase.getHeader()));
//	}
	
	public static void after_replace(TestCase testcase) {
		  testcase.setDbCheck(ParamUtils.matcher(testcase.getDbCheck()));
	}
	

}
