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
	public boolean insertIntoDatabase(String tableName,HashMap<String,Object> colunmsMap,int UserID) {

		boolean isSuccess = true;
		
		if (tableName == null) {

			PosLogger.log.error("Add hander,tablename is not exist");
			return false;

		}
		
		if (colunmsMap == null) {

			PosLogger.log.error("Add hander,colunmsList is not exist");
			return false;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			//设置事务
			conn.setAutoCommit(false);
			
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

				PosLogger.log.info("Insert into " + tableName + " success");


			} else {
				PosLogger.log.error("Insert into database error");
				
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			String logContent = "";
			
			if(tableName.equals("temp_category")){
				logContent = "类别,类别名字:" + colunmsMap.get("category_name");
			}else if(tableName.equals("temp_firm")){
				logContent = "商家,商家名字" + colunmsMap.get("firm_name");
			}else if(tableName.equals("temp_key")){
				logContent = "兑换码,该兑换码所属团购ID:" + colunmsMap.get("tuan_id");
			}else if(tableName.equals("temp_tuan")) {
				logContent = "团购,团购名称:" + colunmsMap.get("tuan_name");
			}else if(tableName.equals("temp_users")){
				logContent = "用户,用户名:" + colunmsMap.get("user_name") + ",用户级别:" + colunmsMap.get("user_grade");
			}

			//添加log表
			PreparedStatement ps_2 = conn.prepareStatement("insert into temp_log(user_id,log_time,log_content) values(?,?,?)");
			
			ps_2.setInt(1, UserID);
			ps_2.setLong(2, System.currentTimeMillis());
			ps_2.setString(3, "增加" + logContent);
			
			int result_2 = ps_2.executeUpdate();
			
			if (result_2 > 0) {

				PosLogger.log.info("Insert into log success");


			} else {
				
				PosLogger.log.error("Insert into database error");
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			conn.commit();

		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				
				PosLogger.log.error(e1.getMessage());
				
			}

		} finally {

			// 关闭资源

			this.closeConnection();

		}

		return isSuccess;

	}
	
	//更新操作
	public boolean updateToDatabase(String tableName,String primaryKeyName,int primaryKeyValue,HashMap<String,Object> colunmsMap,int userID){
		
		boolean isSuccess = false;
		
		if (tableName == null) {

			PosLogger.log.error("Update hander,tablename is not exist");
			return isSuccess;

		}
		
		if (colunmsMap == null) {

			PosLogger.log.error("Update hander,colunmsList is not exist");
			return isSuccess;

		}
		
		if (primaryKeyValue <= 0) {

			PosLogger.log.error("Update hander,primaryKeyValue is not exist");
			return isSuccess;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			//设置事务
			conn.setAutoCommit(false);
			
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

				PosLogger.log.info("Update into " + tableName + " success");

				isSuccess = true;

			} else {

				PosLogger.log.error("Update into database error");
				
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}

			String logContent = "";
			
			if(tableName.equals("temp_category")){
				logContent = "类别,类别名字:" + colunmsMap.get("category_name");
			}else if(tableName.equals("temp_firm")){
				logContent = "商家,商家名字" + colunmsMap.get("firm_name");
			}else if(tableName.equals("temp_key")){
				logContent = "兑换码,该兑换码所属团购ID:" + colunmsMap.get("tuan_id");
			}else if(tableName.equals("temp_tuan")) {
				logContent = "团购,团购名称:" + colunmsMap.get("tuan_name");
			}else if(tableName.equals("temp_users")){
				logContent = "用户,用户名:" + colunmsMap.get("user_name") + ",用户级别:" + colunmsMap.get("user_grade");
			}
			
			//添加log表
			PreparedStatement ps_2 = conn.prepareStatement("insert into temp_log(user_id,log_time,log_content) values(?,?,?)");
			
			ps_2.setInt(1, userID);
			ps_2.setLong(2, System.currentTimeMillis());
			ps_2.setString(3, "更改" + logContent);
			
			int result_2 = ps_2.executeUpdate();
			
			if (result_2 > 0) {

				PosLogger.log.info("Insert into log success");


			} else {
				
				PosLogger.log.error("Insert into database error");
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			conn.commit();

		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				
				PosLogger.log.error(e1.getMessage());
				
			}

		} finally {

			// 关闭资源

			this.closeConnection();

		}
		
		return isSuccess;
		
	}
	
	//删除操作
	public boolean deleteToDatabase(String tableName,String primaryKeyName,int primaryKeyValue,int userID,String objectName){
		
		boolean isSuccess = false;
		
		if (tableName == null) {

			PosLogger.log.error("Delete hander,tablename is not exist");
			return isSuccess;

		}
		
		if (primaryKeyValue <= 0) {

			PosLogger.log.error("Delete hander,primaryKeyValue is not exist");
			return isSuccess;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			//设置事务
			conn.setAutoCommit(false);
			
			String sql = "delete from " + tableName + " where " + primaryKeyName + "=" + primaryKeyValue;
			
			ps = conn.prepareStatement(sql);

			int result = ps.executeUpdate();

			if (result > 0) {

				PosLogger.log.info("Delete " + tableName + " success");

				isSuccess = true;

			} else {

				PosLogger.log.error("Delete database error");
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			String logContent = "";
			
			if(tableName.equals("temp_category")){
				logContent = "类别,类别名字:" + objectName;
			}else if(tableName.equals("temp_firm")){
				logContent = "商家,商家名字" + objectName;
			}else if(tableName.equals("temp_key")){
				logContent = "兑换码,该兑换码ID:" + objectName;
			}else if(tableName.equals("temp_tuan")) {
				logContent = "团购,团购名称:" + objectName;
			}else if(tableName.equals("temp_users")){
				logContent = "用户,用户名:" + objectName;
			}
			
			//添加log表
			PreparedStatement ps_2 = conn.prepareStatement("insert into temp_log(user_id,log_time,log_content) values(?,?,?)");
			
			ps_2.setInt(1, userID);
			ps_2.setLong(2, System.currentTimeMillis());
			ps_2.setString(3, "删除" + logContent);
			
			int result_2 = ps_2.executeUpdate();
			
			if (result_2 > 0) {

				PosLogger.log.info("Insert into log success");


			} else {
				
				PosLogger.log.error("Insert into database error");
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			conn.commit();

		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				
				PosLogger.log.error(e1.getMessage());
				
			}

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

			PosLogger.log.error("Select hander,tablename is not exist");
			return null;

		}
		
		if (primaryKeyValue <= 0) {

			PosLogger.log.error("Select hander,primaryKeyValue is not exist");
			return null;

		}
		
		try {

			conn = SQLConn.getConnection();
			
			String sql = "select *  from " + tableName + " where " + primaryKeyName + "=" + primaryKeyValue;
			
			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();

			if(rs == null){
				
				PosLogger.log.error("Have no data tableName=" + tableName + " primaryKeyValue=" + primaryKeyValue);
				
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
