package testng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;
import com.github.crab2died.ExcelUtils;

import apitest.ApiTestThread4;
import apitest.TestCase;
import apitest.TestCaseResult;
import utils.DbCheck;
import utils.DbCheckResult;
import utils.EmailUtils;
import utils.ExcelToMapUtils;
import utils.HttpClientUtils;
import utils.ParamUtils;

public class ApiTestTestNg {

	public static String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"apitest3.xlsx";

	private static final Logger logger = LoggerFactory.getLogger(ApiTestTestNg.class);

	public static List<TestCaseResult> allresult=new ArrayList<TestCaseResult>();

	@BeforeClass
	public void before() {
		HttpClientUtils.openProxy=true;
	}

	@Test(dataProvider = "excel", threadPoolSize = 10)
    public void testCase(Map<String, Object> map) {
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
			allresult.addAll(allCaseResults);
			ParamUtils.clear();
		}
    }


	     //parallel = true 并行测试
	    @DataProvider(name = "excel")
	    public Iterator<Object[]> parameterIntTestProvider() {
	    	List<Object[]> dataProvider = new ArrayList<Object[]>();

			List<Map<String, Object>> paraMaps= ExcelToMapUtils.importExcel(path, 1);
			paraMaps.forEach(d->{
				dataProvider.add(new Object[] {d});  //List<Object[1]>
			});

	       return dataProvider.iterator();
	    }


	    @AfterClass
	    public void after() {
	    	try {
	    		String result_path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"result_"+System.nanoTime()+".xlsx";
				ExcelUtils.getInstance().exportObjects2Excel(allresult, TestCaseResult.class, result_path);
				EmailUtils.sendEmailsWithAttachments("测试结果", "请查收附件", result_path);
			} catch (Exception e) {
			}finally {
				allresult.clear();
			}
		}

}
