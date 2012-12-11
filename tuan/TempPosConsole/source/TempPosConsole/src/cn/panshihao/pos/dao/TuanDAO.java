package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.Tuan;
import cn.panshihao.pos.tools.PosLogger;

//对tuan表的操作
public class TuanDAO extends SuperDAO {
	
	private String tablesName = "temp_tuan";
	
	//添加团购操作
	public boolean addKey(Tuan tuan){
		
		boolean isSuccess = false;
		
		if (tuan == null) {

			PosLogger.log.error("add tuan fail,tuan object is not exist");
			return isSuccess;

		}
		
		// 检查团购名称是否为空
		if (tuan.getTuan_name() == null) {

			PosLogger.log.error("add tuan fail,tuan name is not exist");
			return isSuccess;

		}

		// 检查团购描述是否为空
		if (tuan.getTuan_desc() == null) {

			PosLogger.log.error("add tuan fail,tuan description is not legal");
			return isSuccess;

		}
		
		// 检查团购开始时间是否为空
		if (tuan.getTuan_starttime() <= 0L) {

			PosLogger.log.error("add tuan fail,tuan starttime is not legal");
			return isSuccess;

		}
		
		// 检查团购结束时间是否为空
		if (tuan.getTuan_endtime() <= 0L) {

			PosLogger.log.error("add tuan fail,tuan endtime is not legal");
			return isSuccess;

		}

		PosLogger.log.debug("add tuan,tuan name " + tuan.getTuan_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("tuan_name",tuan.getTuan_name());
		colunmsMap.put("tuan_desc",tuan.getTuan_desc());
		colunmsMap.put("tuan_starttime",tuan.getTuan_starttime());
		colunmsMap.put("tuan_endtime",tuan.getTuan_endtime());
		
		if(tuan.getCategory_id() <= 0 ){
			
			PosLogger.log.info("not input category_id");
		}else{
			
			colunmsMap.put("category_id",tuan.getCategory_id());
		}
		
		if(tuan.getFirm_id() <= 0 ){
			
			PosLogger.log.info("not input firm_id");
		}else{
			
			colunmsMap.put("firm_id",tuan.getFirm_id());
		}

		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap);
		
		return isSuccess;
		
	}
	
	public static void main(String[] args) {
		
		TuanDAO dao = new TuanDAO();
		Tuan f = new Tuan();
//		f.setCategory_id(1);
//		f.setFirm_id(1);
		f.setTuan_desc("这是团的网吧上网的团购");
		f.setTuan_name("了上天网吧1.5元一小时团购票");
		f.setTuan_starttime(System.currentTimeMillis());
		f.setTuan_endtime(System.currentTimeMillis() - 2000);
		dao.addKey(f);
		
	}
	
}
