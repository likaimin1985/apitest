package apitest.work;

import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;

import utils.JDBCUtils;

/**
 * dbutils 增删修改例子
 * @author pc
 *
 */
public class DbUtilsCRUDTest {

	public static void main(String[] args) throws SQLException {
		//修改，删除，添加测试
//		update();
		add();
		//可变变量测试
       // test();
       // test("1","2");
//        String[] pStrings=new String[] {"1","2"};
//        test(pStrings);
	}

	
	/**
	 * 修改测试
	 * @throws SQLException
	 */
	private static void update() throws SQLException {
		//ComboPooledDataSource ds = new ComboPooledDataSource();
		// dbutis使用数据源
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
		// 可变变量  无限 也可以没有 也可以数组
		Object[] objects= new Object[] {"123_test","333_test", "14ba4bd0-a0da-4a2c-b136-de036b54e98a"};
		//runner.update("update t_user_test set loginname=?,loginpass=? where uid=?", "123_dbutils","123_dbutils","14ba4bd0-a0da-4a2c-b136-de036b54e98a");
		runner.update("update t_user_test set loginname=?,loginpass=? where uid=?", objects);
	}
	
	/**
	 * 删除和添加测试
	 * @throws SQLException
	 */
	private static void add() throws SQLException {
		QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());
		//删除
		runner.update("delete from t_user_test");
		//添加
		for (int i = 0; i < 1000; i++) {
			Object[] objects= new Object[] {UUID.randomUUID().toString(),"test"+i, "pass"+i};
			runner.update("insert INTO t_user_test(uid,loginname,loginpass) values(?,?,?)", objects);
		}
	}
	
	
	private static void test(String...p) {
		for (int i = 0; i < p.length; i++) {
			String string = p[i];
			System.out.println(string);
		}
		
	}

}
