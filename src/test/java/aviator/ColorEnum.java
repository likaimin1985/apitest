package aviator;

import lombok.Getter;

//定义类
@Getter
public enum ColorEnum {
	Red("红色","玫瑰红"),
	Green("红色","玫瑰红");
	
	
	private ColorEnum(String color, String desc) {
		this.color = color;
		this.desc = desc;
	}
	private String color;
	private String desc;

}
