package apitest.work;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import utils.JDBCUtils;

public class DbTest {
//	 public static final String URL = "jdbc:mysql://118.24.13.38:3307/testfan_jiguang";
//	    public static final String USER = "root";
//	    public static final String PASSWORD = "123456";
//	    
	
	
	 
	  
	public static void main(String[] args) throws SQLException {
		getUserByJdbc();
		//getUserByDbUtils();
	}
	
	private static void getUserByJdbc() {
		
		Connection conn = null;
		 Statement stmt = null;
		try {
			
			  //1.加载驱动程序
	       // Class.forName("com.mysql.jdbc.Driver");
//			Class.forName("com.mysql.cj.jdbc.Driver");
//	        //2. 获得数据库连接
//	        conn = DriverManager.getConnection(URL, USER, PASSWORD);
		   // ComboPooledDataSource ds = new ComboPooledDataSource(); //读取配置文件
			conn=JDBCUtils.getConnection();
	        //3.操作数据库，实现增删改查
	         stmt = conn.createStatement();
	        //https://www.runoob.com/sql/sql-tutorial.html 公开课 sql
	        ResultSet rs = stmt.executeQuery("SELECT * FROM userInfo");
	        List<UserInfo> userInfos=new ArrayList<UserInfo>();
	        //如果有数据，rs.next()返回true
	        while(rs.next()){
	            String id=rs.getString("id");
	            String phone=rs.getString("phone");
	            String idCard=rs.getString("idCard");
	            String color=rs.getString("color");
	            UserInfo userInfo=new UserInfo();
	            userInfo.setId(id);
	            userInfo.setIdCard(idCard);
	            userInfo.setPhone(phone);
	            userInfo.setColor(color);
	            userInfos.add(userInfo);
	        }
	        userInfos.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void getUserByDbUtils() throws SQLException {
		 // ComboPooledDataSource ds = new ComboPooledDataSource(); //读取配置文件
		  QueryRunner queryRunner =new QueryRunner(JDBCUtils.getDataSource());
	      List<UserInfo> list = (List) queryRunner.query("SELECT * FROM userInfo", 
	    		  new BeanListHandler(UserInfo.class));
	      list.forEach(System.out::println);
	}

}
