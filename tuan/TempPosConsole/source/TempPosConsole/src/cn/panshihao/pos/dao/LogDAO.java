package cn.panshihao.pos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import cn.panshihao.pos.model.Log;
import cn.panshihao.pos.tools.PosLogger;

//对Log表的操作
public class LogDAO extends SuperDAO {
	
	private final String tablesName = "temp_log";
	
	private final String primaryKeyName = "log_id";
	
	//添加兑换码操作
	public boolean addKey(Log log){
		
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

		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap);
		
		return isSuccess;
		
	}
	
	//修改日志操作
	public boolean updateLog(Log log){
		
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
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,log.getLog_id(),colunmsMap);
		
		return isSuccess;
		
	}
	
	//删除员工日志
	public boolean deleteLog(int primaryKeyVaule){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("delete log fail,log id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
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
			
			PosLogger.log.error("result is null");
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
	
	public static void main(String[] args) {
		
		LogDAO dao = new LogDAO();
		Log f = new Log();
		f.setLog_id(1);
		f.setLog_content("这是第一个log");
		f.setLog_time(System.currentTimeMillis());
		f.setUser_id(1);
		dao.updateLog(f);
		
	}
	
}
