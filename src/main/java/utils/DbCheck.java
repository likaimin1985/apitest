package utils;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.checkpoint.CheckPointUtils;
import com.github.checkpoint.JsonCheckResult;

/**
 * 组合了JSON,dbutils,CheckPointUtils
 * 
 * @author pc
 *
 */
public class DbCheck {

	// select * from t_user_test where uid='123',size>1,mysql1
	public static DbCheckResult check(String dbcheck) {
		
		if(StringUtils.isBlank(dbcheck)) {
			return DbCheckResult.DbNotFoundCheck;
		}
		// 数据库
		String[] dbStrings = dbcheck.split(",");
		if (dbStrings.length < 3) {
			return DbCheckResult.DbUnDefined;
		}
		String dbtype = dbStrings[2];
		String sql = dbStrings[0];
		String check = dbStrings[1];
		QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource(dbtype));
		try {
			List list = queryRunner.query(sql, new MapListHandler());
			if(!list.isEmpty()) {
				CheckPointUtils.openLog=true;
				// json
				JsonCheckResult jsonCheckResult= CheckPointUtils.check(JSON.toJSONString(list), check);
				if(jsonCheckResult.isResult()) {
					return DbCheckResult.SUCCESS;
				}
				return DbCheckResult.False;
			}else {
				return DbCheckResult.DbEmpty;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return DbCheckResult.SqlError;
		}
	}
	
	public static void main(String[] args) {
		DbCheckResult dbString= DbCheck.check("select * from t_user_test where uid='6556c413-c657-4a6d-a47b-01e0caa55ced',size=1,mysql1");
		System.out.println(dbString);
	}

}
