package apitest;

import com.github.crab2died.annotation.ExcelField;

import lombok.Data;


@Data
public class TestCase {

	@ExcelField(title = "顺序")
	private int order;
	
	@ExcelField(title = "是否开启")
	private String isOpen;  //==是找出来
	
	@ExcelField(title = "地址")
	private String url;
	
	@ExcelField(title = "用例名称")
	private String caseName;
	
	@ExcelField(title = "类型")
	private String type;
	
	@ExcelField(title = "参数",readConverter = FileReadCovert.class)
	//@ExcelField(title = "参数")
	private String params;
	
	@ExcelField(title = "头部")
	private String header;
	
	@ExcelField(title = "返回结果从json提取")
	private String jsonResult;
	
	@ExcelField(title = "返回结果校验")
	private String resultCheck;
	
	@ExcelField(title = "数据库检查")
	private String dbCheck;
	
	@ExcelField(title = "返回结果从html提取")
	private String htmlResult;

	
}
