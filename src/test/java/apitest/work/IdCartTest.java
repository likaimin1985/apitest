package apitest.work;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import utils.JDBCUtils;

public class IdCartTest {

	final static String path=System.getProperty("user.dir")+File.separator+"data"+File.separator+"test.txt";


	private static void countByJson() {
		int count=0;
		try {
			List<String> list= FileUtils.readLines(new File(path),"utf-8");
			for (String d : list) {
				JSONObject jsonObject= (JSONObject) JSON.parse(d);
				String idcard = jsonObject.getString("idcard");
				if(StringUtils.isNotBlank(idcard)) {
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("countByJson 统计身份证非空结果  "+count);

	}

	private static void countByJsonPath() {
		int count=0;
		try {
			List<String> list= FileUtils.readLines(new File(path),"utf-8");
			for (String d : list) {
			    String idcard = (String) JSONPath.read(d, "idcard");
			    if(StringUtils.isNotBlank(idcard)) {
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("countByJsonPath 统计身份证非空结果  "+count);
	}

	private static void countPattern() {
		int count=0;
		try {
			String alllines = FileUtils.readFileToString(new File(path), "utf-8");
			  String pattern = "\"idcard\":\"(.*)\",";
			  Pattern r = Pattern.compile(pattern);

		      Matcher m = r.matcher(alllines);
		      while (m.find()) {
		    	  if(StringUtils.isNotBlank(m.group(1))) {
		    		  count++;
		    	  }
		      }
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("countPattern 统计身份证非空结果  "+count);

	}
	public static void main(String[] args){
//		countByJson();
//		countByJsonPath();
//		countPattern();
//		countByDb();
//
//		countByDbUtils();

	}


    private static void countByDbUtils() {
		QueryRunner queryRunner=new QueryRunner();
		try {
		//Map map=queryRunner.query("SELECT count(1) as mycount FROM userInfo where idCard is not null and idCard!=''", new MapHandler());
		//System.out.println(map.get("mycount"));
		long count=queryRunner.query(JDBCUtils.getConnection(),"SELECT count(1) as mycount FROM userInfo where idCard is not null and idCard!=''", new ScalarHandler<Long>());
		System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public static final String URL = "jdbc:mysql://118.24.13.38:3307/testfan_jiguang";
    public static final String USER = "root";
    public static final String PASSWORD = "123456";

	private static void countByDb() {
		try {
			  //1.加载驱动程序
	       // Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
	        //2. 获得数据库连接
	        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	        //3.操作数据库，实现增删改查
	        Statement stmt = conn.createStatement();
	        //https://www.runoob.com/sql/sql-tutorial.html 公开课 sql
	        ResultSet rs = stmt.executeQuery("SELECT count(1) as mycount FROM userInfo where idCard is not null and idCard!=''");
	        //如果有数据，rs.next()返回true
	        while(rs.next()){
	            System.out.println(rs.getInt("mycount"));

	        }
	        //dbutils

		} catch (Exception e) {
			e.printStackTrace();
		}



	}





}
