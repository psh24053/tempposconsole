package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

/**
 * 
 * @author 彭琅
 *
 */
//对数据库的基本操作
public class SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	//添加操作
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
			
			//构造添加语句
			for(String colunmsName : colunmsMap.keySet()){
				
				sqlHead += colunmsName + ",";
				
				if(colunmsMap.get(colunmsName) instanceof String){
					
					sqltail += "'" + colunmsMap.get(colunmsName) + "',";
					
				}else{
					
					sqltail += colunmsMap.get(colunmsName) + ",";
					
				}
				
			}
			
			sqlHead = sqlHead.substring(0, sqlHead.length() - 1);
			
			sqlHead += ")";
			
			sqltail = sqltail.substring(0, sqltail.length() - 1);
			
			sqltail += ");";
			
			String sql = sqlHead + sqltail;
			
			PosLogger.log.debug("insert sql is :" + sql);
			
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
