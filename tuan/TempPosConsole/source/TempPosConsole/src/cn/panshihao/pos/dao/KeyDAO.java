package cn.panshihao.pos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import cn.panshihao.pos.model.Key;
import cn.panshihao.pos.tools.PosLogger;

//对Key表的操作
public class KeyDAO extends SuperDAO {
	
	private final String tablesName = "temp_key";
	
	private final String primaryKeyName = "key_id";
	
	//添加兑换码操作
	public boolean addKey(Key key){
		
		boolean isSuccess = false;
		
		if (key == null) {

			PosLogger.log.error("add key fail,key object is not exist");
			return isSuccess;

		}
		
		// 检查兑换码code是否为空
		if (key.getKey_code() == null) {

			PosLogger.log.error("add key fail,key code is not exist");
			return isSuccess;

		}

		// 检查兑换码状态是否合法
		if (key.getKey_status() < 0 || key.getKey_status() > 1) {

			PosLogger.log.error("add key fail,key status is not legal");
			return isSuccess;

		}

		PosLogger.log.debug("add key");
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("key_code",key.getKey_code());
		colunmsMap.put("key_status",key.getKey_status());
		
		if(key.getTuan_id() <= 0 ){
			
			PosLogger.log.info("not input tuan_id");
		}else{
			
			colunmsMap.put("tuan_id",key.getTuan_id());
		}

		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap);
		
		return isSuccess;
		
	}
	
	//修改兑换码操作
	public boolean updateKey(Key key){
		
		boolean isSuccess = false;
		
		if (key == null) {

			PosLogger.log.error("update key fail,key object is not exist");
			return isSuccess;

		}
		
		// 检查keyid是否为空
		if (key.getKey_id() <= 0) {

			PosLogger.log.error("update key fail,key id is not exist");
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
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,key.getTuan_id(),colunmsMap);
		
		return isSuccess;
		
	}
	
	//删除企业
	public boolean deleteKey(int primaryKeyVaule){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("delete key fail,key id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
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

		ResultSet rs = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(rs == null){
			
			PosLogger.log.error("result is null");
			return null;
			
		}
		
		try {
			
			if(rs.next()){
				
				key.setKey_code(rs.getString("key_code"));
				key.setKey_id(rs.getInt("key_id"));
				key.setKey_status(rs.getInt("key_status"));
				key.setTuan_id(rs.getInt("tuan_id"));
				
			}
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} finally{
			
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					PosLogger.log.error(e.getMessage());
				}
			}
			
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
		dao.updateKey(f);
		
	}
	
}
