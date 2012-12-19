package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.Key;
import cn.panshihao.pos.tools.GenerateKeyCode;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

//对Key表的操作
public class KeyDAO extends SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	private ResultSet rs = null;
	
	private final String tablesName = "temp_key";
	
	private final String primaryKeyName = "key_id";
	
	//添加兑换码操作
	public boolean addKey(Key key,int userID){
		
		boolean isSuccess = false;
		
		if (key == null) {

			PosLogger.log.error("Add key fail,key object is not exist");
			return isSuccess;

		}
		
		// 检查兑换码code是否为空
		if (key.getKey_code() == null) {

			PosLogger.log.error("Add key fail,key code is not exist");
			return isSuccess;

		}

		// 检查兑换码状态是否合法
		if (key.getKey_status() < 0 || key.getKey_status() > 1) {

			PosLogger.log.error("Add key fail,key status is not legal");
			return isSuccess;

		}

		PosLogger.log.debug("Add key");
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("key_code",key.getKey_code());
		colunmsMap.put("key_status",key.getKey_status());
		
		if(key.getTuan_id() <= 0 ){
			
			PosLogger.log.info("Not input tuan_id");
		}else{
			
			colunmsMap.put("tuan_id",key.getTuan_id());
		}

		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//修改兑换码操作
	public boolean updateKey(Key key,int userID){
		
		boolean isSuccess = false;
		
		if (key == null) {

			PosLogger.log.error("Update key fail,key object is not exist");
			return isSuccess;

		}
		
		// 检查keyid是否为空
		if (key.getKey_id() <= 0) {

			PosLogger.log.error("Update key fail,key id is not exist");
			return isSuccess;

		}
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		
		if (key.getTuan_id() > 0) {

			colunmsMap.put("tuan_id",key.getTuan_id());
		}

		if (key.getKey_code() != null) {

			colunmsMap.put("key_code",key.getKey_code());

		}
		
		if (key.getKey_status() > -1 ||key.getKey_status() < 2) {

			colunmsMap.put("key_status",key.getKey_status());

		}
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,key.getTuan_id(),colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//删除企业
	public boolean deleteKey(int primaryKeyVaule,int userID){
		
		Key key = getKeyFromDatabase(primaryKeyVaule);
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Delete key fail,key id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule,userID,key.getKey_id()+"");
		
		return isSuccess;
		
	}
	
	//根据主键获得Key
	public Key getKeyFromDatabase(int primaryKeyVaule){
		
		Key key = new Key();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("select key fail,key id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("Result is null");
			return null;
			
		}
		
		if(dataMap.get("key_code") != null){
			
			key.setKey_code((String)dataMap.get("key_code"));
		}
		if(dataMap.get("key_id") != null){
			
			key.setKey_id((int)dataMap.get("key_id"));
		}
		if(dataMap.get("key_status") != null){
			key.setKey_status((int)dataMap.get("key_status"));
			
		}
		if(dataMap.get("tuan_id") != null){
			
			key.setTuan_id((int)dataMap.get("tuan_id"));
		}

		return key;
		
	}
	
	/**
	 * @author penglang
	 * @param tuanID:团购ID,start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取指定团购兑换码.
	 * json说明:"list"-包含所有日志的数组名,"count"-实际得到的日志条数,"kid"-兑换码ID,
	 * "cod"-兑换码,"sta"-兑换码状态(0为未售出兑换码,1为已售出兑换码).
	 * json例:{count:2,list:[{kid:1,"cod":"12345ASLDKHGGJD",sta:1}
	 * ,{kid:2,"cod":"12345ASLDKHGGJD",sta:0}]}
	 */
	public JSONObject getKeyByTuanID(int tuanID,int start,int count){
		
		//检查团购ID是否合法
		if(tuanID <= 0){
			
			PosLogger.log.error("TuanID is not exist");
			
		}
		
		JSONObject keyArray = new JSONObject();
		
		PosLogger.log.debug("Get key by tuanID");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from temp_key where tuan_id=" + tuanID + 
							" limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("This tuan have no key ,tuanID = " + tuanID);
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject logJson = new  JSONObject();
				logJson.put("kid", rs.getInt("key_id"));
				logJson.put("cod", rs.getString("key_code"));
				logJson.put("sta", rs.getInt("key_status"));
				
				array.put(logJson);
				
			}
			
			keyArray.put("list", array);
			keyArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return keyArray;
	}
	
	/**
	 * @author penglang
	 * @param tuanID:团购ID,start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取指定团购已使用兑换码.
	 * json说明:"list"-包含所有日志的数组名,"count"-实际得到的日志条数,"kid"-兑换码ID,
	 * "cod"-兑换码).
	 * json例:{count:2,list:[{kid:1,"cod":"12345ASLDKHGGJD"}
	 * ,{kid:2,"cod":"12345ASLDKHGGJD"}]}
	 */
	public JSONObject getUsedKeyByTuanID(int tuanID,int start,int count){
		
		//检查团购ID是否合法
		if(tuanID <= 0){
			
			PosLogger.log.error("TuanID is not exist");
			
		}
		
		JSONObject keyArray = new JSONObject();
		
		PosLogger.log.debug("Get already used key by tuanID");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from temp_key where tuan_id=" + tuanID + 
							" and user_status=1 limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("This tuanID have no already key ,tuanID = " + tuanID);
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject logJson = new  JSONObject();
				logJson.put("kid", rs.getInt("key_id"));
				logJson.put("cod", rs.getString("key_code"));
				
				array.put(logJson);
				
			}
			
			keyArray.put("list", array);
			keyArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return keyArray;
	}
	
	/**
	 * @author penglang
	 * @param tuanID:团购ID,start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取指定团购未使用兑换码.
	 * json说明:"list"-包含所有日志的数组名,"count"-实际得到的日志条数,"kid"-兑换码ID,
	 * "cod"-兑换码).
	 * json例:{count:2,list:[{kid:1,"cod":"12345ASLDKHGGJD"}
	 * ,{kid:2,"cod":"12345ASLDKHGGJD"}]}
	 */
	public JSONObject getNotUsedKeyByTuanID(int tuanID,int start,int count){
		
		//检查团购ID是否合法
		if(tuanID <= 0){
			
			PosLogger.log.error("TuanID is not exist");
			
		}
		
		JSONObject keyArray = new JSONObject();
		
		PosLogger.log.debug("Get not used key by tuanID");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from temp_key where tuan_id=" + tuanID + 
							" and user_status=0 limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("This tuanID have no not userd key ,tuanID = " + tuanID);
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject logJson = new  JSONObject();
				logJson.put("kid", rs.getInt("key_id"));
				logJson.put("cod", rs.getString("key_code"));
				
				array.put(logJson);
				
			}
			
			keyArray.put("list", array);
			keyArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return keyArray;
	}
	
	/**
	 * @author penglang
	 * @return String
	 * 生成唯一的兑换码
	 */
	public String GenerateUniqueCode(){
		
		String keyCode = "";
		
		PosLogger.log.debug("Generate Unique Code");
		
		conn = SQLConn.getConnection();
		
		try {
			
			while(true){
				
				keyCode = GenerateKeyCode.generateKeyCodeHead() + GenerateKeyCode.generateKeyCodeTail();
				
				ps = conn.prepareStatement("select count(*) from temp_key where key_code='" + keyCode + "'");
				
				rs = ps.executeQuery();
				
				if(rs == null){
					PosLogger.log.error("Database error");
					return null;
				}
				
				if (rs.next()) {
					
					if(rs.getInt(1) >= 1){
						
						//兑换码存在
						PosLogger.log.debug("Generate Unique Code fail,code is already exsit");
						continue;
						
					}else if(rs.getInt(1) == 0){
						
						//兑换码不存在
						PosLogger.log.debug("Generate Unique Code success");
						break;
						
					}
					
				}
				
			}
			
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return keyCode;
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
		
		KeyDAO dao = new KeyDAO();
//		Key f = new Key();
//		f.setKey_id(1);
//		f.setKey_status(1);
//		f.setKey_code("123456FJSDLKFSDG");
//		f.setTuan_id(1);
//		dao.updateKey(f,1);
		System.out.println(dao.GenerateUniqueCode());
		
	}
	
}
