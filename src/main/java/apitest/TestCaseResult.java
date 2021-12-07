package apitest;

import com.github.crab2died.annotation.ExcelField;

import lombok.Data;

@Data
public class TestCaseResult{
	
	@ExcelField(title = "地址",order = 2)
	private String url;
	
	@ExcelField(title = "用例名称",order = 1)
	private String caseName;
	
	@ExcelField(title = "类型")
	private String type;
	
	@ExcelField(title = "参数")
	private String params;
	
	@ExcelField(title = "头部")
	private String header;
	
	
	@ExcelField(title = "数据库校验结果")
	private String dbcheckResult;
	
	@ExcelField(title = "接口返回校验结果")
	private String apicheckResult;
	
	
	@ExcelField(title = "最终结果")
	private String allResult;
	
	


}
