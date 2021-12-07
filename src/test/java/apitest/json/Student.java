package apitest.json;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;


public class Student {
	private String name;  
    private int age; 
   // @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date date;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", age=" + age + ", date=" + date + "]";
	}
}
