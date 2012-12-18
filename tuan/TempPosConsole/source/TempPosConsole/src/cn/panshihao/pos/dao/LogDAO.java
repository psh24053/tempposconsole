package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.Log;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

//对Log表的操作
public class LogDAO extends SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	private ResultSet rs = null;
	
	private final String tablesName = "temp_log";
	
	private final String primaryKeyName = "log_id";
	
	//添加兑换码操作
	public boolean addKey(Log log,int userID){
		
		boolean isSuccess = false;
		
		if (log == null) {

			PosLogger.log.error("add log fail,log object is not exist");
			return isSuccess;

		}
		
		// 检查log内容是否为空
		if (log.getLog_content() == null) {

			PosLogger.log.error("add log fail,log content is not exist");
			return isSuccess;

		}

		// 检查log时间是否为空
		if (log.getLog_time() <= 0L) {

			PosLogger.log.error("add log fail,log time is not legal");
			return isSuccess;

		}

		PosLogger.log.debug("add log");
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("log_time",log.getLog_time());
		colunmsMap.put("log_content",log.getLog_content());
		
		if(log.getUser_id() <= 0 ){
			
			PosLogger.log.info("not input user_id");
		}else{
			
			colunmsMap.put("user_id",log.getUser_id());
		}

		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//修改日志操作
	public boolean updateLog(Log log,int userID){
		
		boolean isSuccess = false;
		
		if (log == null) {

			PosLogger.log.error("update log fail,log object is not exist");
			return isSuccess;

		}
		
		// 检查logid是否为空
		if (log.getLog_id() <= 0) {

			PosLogger.log.error("update log fail,log id is not exist");
			return isSuccess;

		}
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		
		if (log.getUser_id() > 0) {

			colunmsMap.put("user_id",log.getUser_id());
		}

		if (log.getLog_content() != null) {

			colunmsMap.put("log_content",log.getLog_content());

		}
		
		if (log.getLog_time() > 0) {

			colunmsMap.put("log_time",log.getLog_time());

		}
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,log.getLog_id(),colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//删除员工日志
	public boolean deleteLog(int primaryKeyVaule,int userID){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Delete log fail,log id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule,userID,null);
		
		return isSuccess;
		
	}
	
	//根据主键获得Log
	public Log getLogFromDatabase(int primaryKeyVaule){
		
		Log log = new Log();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("select log fail,log id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("Result is null");
			return null;
			
		}
		
		if(dataMap.get("log_content") != null){
			
			log.setLog_content((String)dataMap.get("log_content"));
		}
		if(dataMap.get("log_id") != null){
			
			log.setLog_id((int)dataMap.get("log_id"));
			
		}
		if(dataMap.get("log_time") != null){
			log.setLog_time((long)dataMap.get("log_time"));
			
		}
		if(dataMap.get("user_id") != null){
			
			log.setUser_id((int)dataMap.get("user_id"));
		}

		return log;
		
	}
	
	/**
	 * @author penglang
	 * @param userid:用户ID,start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取指定用户日志信息,并且按照时间戳从大到小排序.
	 * json说明:"list"-包含所有日志的数组名,"count"-实际得到的日志条数,"lid"-日志ID,
	 * "name"-用户名称,"tim"-存储日志的时间戳,"con"-日志的内容,"uid"-用户ID
	 * json例:{count:2,list:[{uid:1,name:"娱乐","desc":"K歌,上网,样样都有",form:"暂不知道什么形式"}
	 * ,{uid:2,name:"餐饮",desc:"吃饭喝酒什么都有",form:"暂不知道什么形式"}]}
	 */
	public JSONObject getLogByUserID(int userID,int start,int count){
		
		//检查用户ID是否合法
		if(userID <= 0){
			
			PosLogger.log.error("Userid is not exist");
			
		}
		
		JSONObject logArray = new JSONObject();
		
		PosLogger.log.debug("Get log by userID");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from temp_log l,temp_users u " +
					" where l.user_id=u.user_id l.user_id=" + userID + " order by l.log_time desc " +
							"limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("This user have no log ,userid = " + userID);
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject logJson = new  JSONObject();
				logJson.put("lid", rs.getInt("l.log_id"));
				logJson.put("tim", rs.getLong("l.log_time"));
				logJson.put("con", rs.getString("l.log_content"));
				logJson.put("name", rs.getString("u.user_name"));
				logJson.put("uid", rs.getInt("u.user_id"));
				
				array.put(logJson);
				
			}
			
			logArray.put("list", array);
			logArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return logArray;
	}
	
	/**
	 * @author penglang
	 * @param start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取所有用户日志信息
	 * json说明:"list"-包含所有日志的数组名,"count"-实际得到的日志条数,"lid"-日志ID,
	 * "name"-用户名称,"tim"-存储日志的时间戳,"con"-日志的内容,"uid"-用户ID
	 * json例:{count:2,list:[{uid:1,name:"娱乐","desc":"K歌,上网,样样都有",form:"暂不知道什么形式"}
	 * ,{uid:2,name:"餐饮",desc:"吃饭喝酒什么都有",form:"暂不知道什么形式"}]}
	 */
	public JSONObject getAllLog(int start,int count){
		
		JSONObject logArray = new JSONObject();
		
		PosLogger.log.debug("Get log by userID");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from temp_log l,temp_users u " +
					" where l.user_id=u.user_id  order by l.log_time desc " +
							"limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject logJson = new  JSONObject();
				logJson.put("lid", rs.getInt("l.log_id"));
				logJson.put("tim", rs.getLong("l.log_time"));
				logJson.put("con", rs.getString("l.log_content"));
				logJson.put("name", rs.getString("u.user_name"));
				logJson.put("uid", rs.getInt("u.user_id"));
				
				array.put(logJson);
				
			}
			
			logArray.put("list", array);
			logArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return logArray;
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
	
	public static void main(String[] args) {
		
		LogDAO dao = new LogDAO();
		Log f = new Log();
		f.setLog_id(1);
		f.setLog_content("这是第一个log");
		f.setLog_time(System.currentTimeMillis());
		f.setUser_id(1);
		dao.updateLog(f,1);
		
	}
	
}
