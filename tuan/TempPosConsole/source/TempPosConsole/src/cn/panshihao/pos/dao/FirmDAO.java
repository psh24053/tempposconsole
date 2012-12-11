package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.tools.PosLogger;

//对firm表的操作
public class FirmDAO extends SuperDAO {
	
	private String tablesName = "temp_firm";
	
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
	
	public static void main(String[] args) {
		
		FirmDAO dao = new FirmDAO();
		Firm f = new Firm();
		f.setFirm_name("乐上天网吧");
		f.setFirm_address("仁寿太富");
		f.setFirm_desc("这个是很好的网吧");
		f.setFirm_person("老王");
		f.setFirm_phone("15008224403");
		dao.addFirm(f);
		
	}
	
}
