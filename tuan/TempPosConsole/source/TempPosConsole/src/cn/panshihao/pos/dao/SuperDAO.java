package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.model.User;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

/**
 * 
 * @author 彭琅
 *
 */
//对数据库的基本操作
public class SuperDAO {
	
	public Connection conn = null;
	
	public PreparedStatement ps = null;
	
	//增加User
	public boolean insertIntoDatabase(String tableName,HashMap<String,Object> colunmsMap) {

		boolean isSuccess = false;
		
		if (tableName == null) {

			PosLogger.log.error("add hander,tablename is not exist");
			return isSuccess;

		}
		
		if (colunmsMap == null) {

			PosLogger.log.error("add hander,colunmsList is not exist");
			return isSuccess;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			String sqlHead  = "insert into " + tableName + "(";
			
			String sqltail = " values(";
			
			for(String colunmsName : colunmsMap.keySet()){
				
				sqlHead += colunmsName + ",";
				
				sqltail += colunmsMap.get(colunmsName) + ",";
				
			}
			
			sqlHead = sqlHead.substring(0, sqlHead.length() - 1);
			
			sqlHead += ")";
			
			sqltail = sqltail.substring(0, sqltail.length() - 1);
			
			sqltail += ");";
			
			String sql = sqlHead + sqltail;
			
			PosLogger.log.debug("insert sql is " + sql);
			
			ps = conn.prepareStatement(sql);

			int result = ps.executeUpdate();

			if (result > 0) {

				PosLogger.log.info("insert into " + tableName + " success");

				isSuccess = true;

			} else {

				PosLogger.log.error("insert into database error");

			}

		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());

		} finally {

			// 关闭资源

			if (ps != null) {

				try {
					ps.close();
				} catch (SQLException e) {
					PosLogger.log.error(e.getMessage());
				}

			}

			if (conn != null) {

				try {
					conn.close();
				} catch (SQLException e) {
					PosLogger.log.error(e.getMessage());
				}
			}

		}

		return isSuccess;

	}
	
	
}
