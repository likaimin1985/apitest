package apitest.work;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import utils.JDBCUtils;

//https://blog.csdn.net/a911711054/article/details/77719685
//参考例子
/**
 * dbutils 查询例子
 * @author pc
 *
 */
public class DbutilsQueryTest {
	public static void main(String[] args) {
//		findOne();
//		findList();
//		findMap();
//		findListMap();
		findColumnListHandler();
	}
	
	
	private static void findColumnListHandler() {
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user_t";
		try {
			List list =  (List) runner.query(sql, new ColumnListHandler(2));
			System.out.println(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	private static void findListMap() {
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user_t";
		try {
			List list =  runner.query(sql, new MapListHandler());
			System.out.println(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	private static void findMap() {
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user_t where id=? and user_name=?";
		try {
			Map map =  runner.query(sql, new MapHandler(), "1","test");
			System.out.println(map);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 查找一个对象 BeanHandler
	 * @throws SQLException
	 */
	public static void findOne() {
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from userInfo where id=?";
		UserInfo user;
		try {
			user = (UserInfo) runner.query(sql, new BeanHandler(UserInfo.class), "7");
			System.out.println(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查找多个 关键对象BeanListHandler
	 */
	public static void findList() {
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from userInfo";
		try {
			List<UserInfo> list = (List) runner.query(sql, new BeanListHandler(UserInfo.class));
			System.out.println(list);
			for (Object object : list) {
				System.out.println(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
//	
//	/**
//	 * 关键对象 MapHandler
//	 */
//	public static void findMap() {
//		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
//        String sql = "select * from t_user";
//		try {
//			Object map = runner.query(sql, new MapHandler());
//			System.out.println(map);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	/**
//	 * 关键对象 MapListHandler
//	 */
//	public static void findListMap() {
//		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
//        String sql = "select * from t_user";
//		try {
//			Object map = runner.query(sql, new MapListHandler());
//			System.out.println(map);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	



}
