package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.Key;
import cn.panshihao.pos.tools.PosLogger;

//对Key表的操作
public class KeyDAO extends SuperDAO {
	
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
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Delete key fail,key id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule,userID);
		
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
	
	public static void main(String[] args) {
		
		KeyDAO dao = new KeyDAO();
		Key f = new Key();
		f.setKey_id(1);
		f.setKey_status(1);
		f.setKey_code("123456FJSDLKFSDG");
//		f.setTuan_id(1);
		dao.updateKey(f,1);
		
	}
	
}
