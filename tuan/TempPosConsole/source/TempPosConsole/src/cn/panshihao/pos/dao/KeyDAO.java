package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.Key;
import cn.panshihao.pos.tools.PosLogger;

//对Key表的操作
public class KeyDAO extends SuperDAO {
	
	private String tablesName = "temp_key";
	
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
	
	public static void main(String[] args) {
		
		KeyDAO dao = new KeyDAO();
		Key f = new Key();
		f.setKey_status(1);
		f.setKey_code("123456FJSDLKFSDG");
//		f.setTuan_id(1);
		dao.addKey(f);
		
	}
	
}
