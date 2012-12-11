package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.Log;
import cn.panshihao.pos.tools.PosLogger;

//对Log表的操作
public class LogDAO extends SuperDAO {
	
	private String tablesName = "temp_log";
	
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
	
	public static void main(String[] args) {
		
		LogDAO dao = new LogDAO();
		Log f = new Log();
		f.setLog_content("这是第一个log");
		f.setLog_time(System.currentTimeMillis());
		f.setUser_id(1);
		dao.addKey(f);
		
	}
	
}
