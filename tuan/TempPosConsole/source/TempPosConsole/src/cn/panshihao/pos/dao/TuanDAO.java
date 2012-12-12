package cn.panshihao.pos.dao;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.Tuan;
import cn.panshihao.pos.tools.PosLogger;

//对tuan表的操作
public class TuanDAO extends SuperDAO {
	
	private final String tablesName = "temp_tuan";
	
	private final String primaryKeyName = "tuan_id";
	
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
	
	//修改团购操作
	public boolean updateTuan(Tuan tuan){
		
		boolean isSuccess = false;
		
		if (tuan == null) {

			PosLogger.log.error("update tuan fail,tuan object is not exist");
			return isSuccess;

		}
		
		// 检查tuanid是否为空
		if (tuan.getTuan_id() <= 0) {

			PosLogger.log.error("update tuan fail,tuan id is not exist");
			return isSuccess;

		}
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		
		if (tuan.getCategory_id() > 0) {

			colunmsMap.put("category_id",tuan.getCategory_id());
		}

		if (tuan.getFirm_id() > 0) {

			colunmsMap.put("firm_id",tuan.getFirm_id());

		}
		
		if (tuan.getTuan_name() != null) {

			colunmsMap.put("tuan_name",tuan.getTuan_name());

		}
		
		if (tuan.getTuan_desc() != null) {

			colunmsMap.put("tuan_desc",tuan.getTuan_desc());

		}
		
		if (tuan.getTuan_starttime() > 0) {

			colunmsMap.put("tuan_starttime",tuan.getTuan_starttime());

		}
		
		if (tuan.getTuan_endtime() > 0) {

			colunmsMap.put("tuan_endtime",tuan.getTuan_endtime());

		}
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,tuan.getTuan_id(),colunmsMap);
		
		return isSuccess;
		
	}
	
	//删除团购
	public boolean deleteTuan(int primaryKeyVaule){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("delete tuan fail,tuan id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		return isSuccess;
		
	}
	
	//根据主键获得Tuan
	public Tuan getTuanFromDatabase(int primaryKeyVaule){
		
		Tuan tuan = new Tuan();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("select tuan fail,tuan id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("result is null");
			return null;
			
		}
		
		if(dataMap.get("category_id") != null){
			tuan.setCategory_id((int)dataMap.get("category_id"));
		}
		if(dataMap.get("firm_id") != null){
			
			tuan.setFirm_id((int)dataMap.get("firm_id"));
			
		}
		if(dataMap.get("tuan_desc") != null){
			
			tuan.setTuan_desc((String)dataMap.get("tuan_desc"));
		}
		if(dataMap.get("tuan_endtime") != null){
			
			tuan.setTuan_endtime((long)dataMap.get("tuan_endtime"));
		}
		if(dataMap.get("tuan_id") != null){
			
			tuan.setTuan_id((int)dataMap.get("tuan_id"));
		}
		if(dataMap.get("tuan_name") != null){
			
			tuan.setTuan_name((String)dataMap.get("tuan_name"));
		}
		if(dataMap.get("tuan_starttime") != null){
			
			tuan.setTuan_starttime((long)dataMap.get("tuan_starttime"));
		}
		
		return tuan;
		
	}
	
	public static void main(String[] args) {
		
		TuanDAO dao = new TuanDAO();
//		Tuan f = new Tuan();
//		f.setTuan_id(1);
//		f.setCategory_id(1);
//		f.setFirm_id(1);
//		f.setTuan_desc("这是团的网吧上网的团购");
//		f.setTuan_name("了上天网吧1.5元一小时团购票");
//		f.setTuan_starttime(System.currentTimeMillis());
//		f.setTuan_endtime(System.currentTimeMillis() - 2000);
//		dao.updateTuan(f);
//		Tuan t = dao.getTuanFromDatabase(2);
//		System.out.println(t.getCategory_id());
//		System.out.println(t.getFirm_id());
//		System.out.println(t.getTuan_endtime());
		JSONObject json = new JSONObject();
		try {
			json.put("df", "df");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
	}
	
}
