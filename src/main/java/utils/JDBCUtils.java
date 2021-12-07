package utils;

import java.sql.Connection;  //一次链接
import java.sql.SQLException;

import javax.sql.DataSource;  //连接池超级父类接口

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtils {
	
  private final static ComboPooledDataSource ds1 = new ComboPooledDataSource(); //读取配置文件
  private final static ComboPooledDataSource ds2 = new ComboPooledDataSource("mysql2"); //读取配置文件
  
  public static Connection getConnection() {
	  try {
		return ds1.getConnection();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  return null;
   }
	
  
  public static DataSource getDataSource(String dbType) {
	  if("mysql".equalsIgnoreCase(dbType)) {
		  return ds1;
		} else if ("mysql2".equalsIgnoreCase(dbType)) {
		  return ds1;
	   }
	  return ds1;
   }


public static DataSource getDataSource() {
	return getDataSource("mysql");
}

}
