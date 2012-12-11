package cn.panshihao.pos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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

		ResultSet rs = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(rs == null){
			
			PosLogger.log.error("result is null");
			return null;
			
		}
		
		try {
			
			if(rs.next()){
				
				tuan.setCategory_id(rs.getInt("category_id"));
				tuan.setFirm_id(rs.getInt("firm_id"));
				tuan.setTuan_desc(rs.getString("tuan_desc"));
				tuan.setTuan_endtime(rs.getLong("tuan_endtime"));
				tuan.setTuan_id(rs.getInt("tuan_id"));
				tuan.setTuan_name(rs.getString("tuan_name"));
				tuan.setTuan_starttime(rs.getLong("tuan_starttime"));
				
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
		
		return tuan;
		
	}
	
	public static void main(String[] args) {
		
		TuanDAO dao = new TuanDAO();
		Tuan f = new Tuan();
		f.setTuan_id(1);
//		f.setCategory_id(1);
//		f.setFirm_id(1);
		f.setTuan_desc("这是团的网吧上网的团购");
		f.setTuan_name("了上天网吧1.5元一小时团购票");
		f.setTuan_starttime(System.currentTimeMillis());
		f.setTuan_endtime(System.currentTimeMillis() - 2000);
		dao.updateTuan(f);
		
	}
	
}
