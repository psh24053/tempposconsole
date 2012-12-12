package cn.panshihao.pos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.tools.PosLogger;

//对firm表的操作
public class FirmDAO extends SuperDAO {
	
	private final String tablesName = "temp_firm";
	
	private final String primaryKeyName = "firm_id";
	
	//添加企业操作
	public boolean addFirm(Firm firm){
		
		boolean isSuccess = false;
		
		if (firm == null) {

			PosLogger.log.error("add firm fail,firm object is not exist");
			return isSuccess;

		}
		
		// 检查企业名是否为空
		if (firm.getFirm_name() == null) {

			PosLogger.log.error("add firm fail,firm name is not exist");
			return isSuccess;

		}

		// 检查企业描述是否为空
		if (firm.getFirm_desc() == null) {

			PosLogger.log.error("add firm fail,firm description is not exist");
			return isSuccess;

		}
		
		// 检查企业地址是否为空
		if (firm.getFirm_address() == null) {

			PosLogger.log.error("add firm fail,firm address is not exist");
			return isSuccess;

		}
		
		// 检查企业电话是否为空
		if (firm.getFirm_phone() == null) {

			PosLogger.log.error("add firm fail,firm phone is not exist");
			return isSuccess;

		}
		
		// 检查企业联系人是否为空
		if (firm.getFirm_person() == null) {

			PosLogger.log.error("add firm fail,firm person is not exist");
			return isSuccess;

		}

		PosLogger.log.debug("add firm , firmname = "  + firm.getFirm_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("firm_name",firm.getFirm_name());
		colunmsMap.put("firm_desc",firm.getFirm_desc());
		colunmsMap.put("firm_address",firm.getFirm_address());
		colunmsMap.put("firm_person", firm.getFirm_person());
		colunmsMap.put("firm_phone", firm.getFirm_phone());
		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap);
		
		return isSuccess;
		
	}
	
	//修改企业操作
	public boolean updateFirm(Firm firm){
		
		boolean isSuccess = false;
		
		if (firm == null) {

			PosLogger.log.error("update firm fail,firm object is not exist");
			return isSuccess;

		}
		
		// 检查firmid是否为空
		if (firm.getFirm_id() <= 0) {

			PosLogger.log.error("update firm fail,firm id is not exist");
			return isSuccess;

		}
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		
		if (firm.getFirm_name() != null) {

			colunmsMap.put("firm_name",firm.getFirm_name());
		}

		if (firm.getFirm_desc() != null) {

			colunmsMap.put("firm_desc",firm.getFirm_desc());

		}
		
		if (firm.getFirm_address() != null) {

			colunmsMap.put("firm_address",firm.getFirm_address());

		}
		
		if (firm.getFirm_phone() != null) {

			colunmsMap.put("firm_phone",firm.getFirm_phone());

		}
		
		if (firm.getFirm_person() != null) {

			colunmsMap.put("firm_person",firm.getFirm_person());

		}
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,firm.getFirm_id(),colunmsMap);
		
		return isSuccess;
		
	}
	
	//删除企业
	public boolean deleteFirm(int primaryKeyVaule){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("delete firm fail,firm id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		return isSuccess;
		
	}
	
	//根据主键获得Firm
	public Firm getFirmFromDatabase(int primaryKeyVaule){
		
		Firm frim = new Firm();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("select frim fail,frim id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("result is null");
			return null;
			
		}
		
		if(dataMap.get("firm_address") != null){
			
			frim.setFirm_address((String)dataMap.get("firm_address"));
		}
		if(dataMap.get("firm_desc") != null){
			
			frim.setFirm_desc((String)dataMap.get("firm_desc"));
		}
		if(dataMap.get("firm_id") != null){
			
			frim.setFirm_id((int)dataMap.get("firm_id"));
		}
		if(dataMap.get("firm_name") != null){
			
			frim.setFirm_name((String)dataMap.get("firm_name"));
		}
		if(dataMap.get("firm_person") != null){
			frim.setFirm_person((String)dataMap.get("firm_person"));
			
		}
		if(dataMap.get("firm_phone") != null){
			
			frim.setFirm_phone((String)dataMap.get("firm_phone"));
		}
	
		return frim;
		
	}
	
	public static void main(String[] args) {
		
		FirmDAO dao = new FirmDAO();
		Firm f = new Firm();
		f.setFirm_id(1);
		f.setFirm_name("乐上天网吧");
		f.setFirm_address("仁寿太富");
		f.setFirm_desc("这个是很好的网吧");
		f.setFirm_person("老王");
		f.setFirm_phone("15008224403");
		dao.updateFirm(f);
		
	}
	
}
