package utils;

import lombok.Getter;


@Getter
public enum DbCheckResult {
	DbNotFoundCheck(false,"没有设置数据库检查点"),
	SqlError(false,"sql有问题"),
	DbEmpty(false,"检查点失败，数据库空"),
	SUCCESS(true,"数据库检查点成功"),
	False(false,"数据库检查点失败"),
	DbUnDefined(false,"没有数据库信息");
	
	
	private Boolean flag;
	
	private String  msg;
	

	private DbCheckResult(Boolean flag, String msg) {
		this.flag = flag;
		this.msg = msg;
	}
	
	

}
