package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
	
	private ResultSet rs = null;
	
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

			this.closeConnection();

		}

		return isSuccess;

	}
	
	//更新操作
	public boolean updateToDatabase(String tableName,String primaryKeyName,int primaryKeyValue,HashMap<String,Object> colunmsMap){
		
		boolean isSuccess = false;
		
		if (tableName == null) {

			PosLogger.log.error("update hander,tablename is not exist");
			return isSuccess;

		}
		
		if (colunmsMap == null) {

			PosLogger.log.error("update hander,colunmsList is not exist");
			return isSuccess;

		}
		
		if (primaryKeyValue <= 0) {

			PosLogger.log.error("update hander,primaryKeyValue is not exist");
			return isSuccess;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			String sql = "update " + tableName + " set ";
			
			//构造添加语句
			for(String colunmsName : colunmsMap.keySet()){
				
				sql += colunmsName + "=";
				
				if(colunmsMap.get(colunmsName) instanceof String){
					
					sql += "'" + colunmsMap.get(colunmsName) + "',";
					
				}else{
					
					sql += colunmsMap.get(colunmsName) + ",";
					
				}
				
			}
			
			sql = sql.substring(0, sql.length() - 1);
			
			sql += " where " + primaryKeyName + "=" + primaryKeyValue + ";";
			
			ps = conn.prepareStatement(sql);

			int result = ps.executeUpdate();

			if (result > 0) {

				PosLogger.log.info("update into " + tableName + " success");

				isSuccess = true;

			} else {

				PosLogger.log.error("update into database error");

			}

		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());

		} finally {

			// 关闭资源

			this.closeConnection();

		}
		
		return isSuccess;
		
	}
	
	//删除操作
	public boolean deleteToDatabase(String tableName,String primaryKeyName,int primaryKeyValue){
		
		boolean isSuccess = false;
		
		if (tableName == null) {

			PosLogger.log.error("delete hander,tablename is not exist");
			return isSuccess;

		}
		
		if (primaryKeyValue <= 0) {

			PosLogger.log.error("delete hander,primaryKeyValue is not exist");
			return isSuccess;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			String sql = "delete from " + tableName + " where " + primaryKeyName + "=" + primaryKeyValue;
			
			ps = conn.prepareStatement(sql);

			int result = ps.executeUpdate();

			if (result > 0) {

				PosLogger.log.info("delete " + tableName + " success");

				isSuccess = true;

			} else {

				PosLogger.log.error("delete database error");

			}

		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());

		} finally {

			// 关闭资源
			this.closeConnection();

		}
		
		return isSuccess;
		
	}
	
	//根据主键获取对象
	public HashMap<String,Object> selectFromDatabase(String tableName,String primaryKeyName,int primaryKeyValue){
		
		HashMap<String,Object> dataMap = null;
		
		if (tableName == null) {

			PosLogger.log.error("select hander,tablename is not exist");
			return null;

		}
		
		if (primaryKeyValue <= 0) {

			PosLogger.log.error("select hander,primaryKeyValue is not exist");
			return null;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			String sql = "select *  from " + tableName + " where " + primaryKeyName + "=" + primaryKeyValue;
			
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();

			if(rs == null){
				
				PosLogger.log.error("have no data tableName=" + tableName + " primaryKeyValue=" + primaryKeyValue);
				
			}

			//将rs转换为list
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();

			dataMap = new HashMap<String, Object>();
			
			if(rs.next()){
				
				for(int i = 1 ; i <= columnCount ; i++){

					dataMap.put(metaData.getColumnName(i), rs.getObject(i));
					
				}
				
			}
			
		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());

		} finally {

			// 关闭资源
			this.closeConnection();

		}
		
		return dataMap;
		
	}
	
	private void closeConnection(){
		
		if(this.rs != null){
			
			try{
				rs.close();
			}catch(SQLException e){
				PosLogger.log.error(e.getMessage());
			}
			
		}
		
		if (this.ps != null) {

			try {
				ps.close();
			} catch (SQLException e) {
				PosLogger.log.error(e.getMessage());
			}

		}

		if (this.conn != null) {

			try {
				conn.close();
			} catch (SQLException e) {
				PosLogger.log.error(e.getMessage());
			}
		}
		
	}
	
}
