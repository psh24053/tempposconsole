package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.Tuan;
import cn.panshihao.pos.tools.GenerateKeyCode;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

//对tuan表的操作
public class TuanDAO extends SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	private ResultSet rs = null;
	
	private final String tablesName = "temp_tuan";
	
	private final String primaryKeyName = "tuan_id";
	
	//添加团购操作
	private boolean addKey(Tuan tuan,int userID){
		
		boolean isSuccess = false;
		
		if (tuan == null) {

			PosLogger.log.error("Add tuan fail,tuan object is not exist");
			return isSuccess;

		}
		
		// 检查团购名称是否为空
		if (tuan.getTuan_name() == null) {

			PosLogger.log.error("Add tuan fail,tuan name is not exist");
			return isSuccess;

		}

		// 检查团购描述是否为空
		if (tuan.getTuan_desc() == null) {

			PosLogger.log.error("Add tuan fail,tuan description is not legal");
			return isSuccess;

		}
		
		// 检查团购开始时间是否为空
		if (tuan.getTuan_starttime() <= 0L) {

			PosLogger.log.error("Add tuan fail,tuan starttime is not legal");
			return isSuccess;

		}
		
		// 检查团购结束时间是否为空
		if (tuan.getTuan_endtime() <= 0L) {

			PosLogger.log.error("Add tuan fail,tuan endtime is not legal");
			return isSuccess;

		}

		PosLogger.log.debug("Add tuan,tuan name " + tuan.getTuan_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("tuan_name",tuan.getTuan_name());
		colunmsMap.put("tuan_desc",tuan.getTuan_desc());
		colunmsMap.put("tuan_starttime",tuan.getTuan_starttime());
		colunmsMap.put("tuan_endtime",tuan.getTuan_endtime());
		
		if(tuan.getCategory_id() <= 0 ){
			
			PosLogger.log.info("Not input category_id");
		}else{
			
			colunmsMap.put("category_id",tuan.getCategory_id());
		}
		
		if(tuan.getFirm_id() <= 0 ){
			
			PosLogger.log.info("Not input firm_id");
		}else{
			
			colunmsMap.put("firm_id",tuan.getFirm_id());
		}

		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//修改团购操作
	public boolean updateTuan(Tuan tuan,int userID){
		
		boolean isSuccess = false;
		
		if (tuan == null) {

			PosLogger.log.error("Update tuan fail,tuan object is not exist");
			return isSuccess;

		}
		
		// 检查tuanid是否为空
		if (tuan.getTuan_id() <= 0) {

			PosLogger.log.error("Update tuan fail,tuan id is not exist");
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
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,tuan.getTuan_id(),colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//删除团购
	public boolean deleteTuan(int primaryKeyVaule,int userID){
		
		Tuan tuan = getTuanFromDatabase(primaryKeyVaule);
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Delete tuan fail,tuan id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule,userID,tuan.getTuan_name());
		
		return isSuccess;
		
	}
	
	//根据主键获得Tuan
	public Tuan getTuanFromDatabase(int primaryKeyVaule){
		
		Tuan tuan = new Tuan();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Select tuan fail,tuan id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("Result is null");
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
	
	/**
	 * @author penglang
	 * @param start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取全部的团购信息，并且按开始时间排序
	 * json说明:"list"-包含所有日志的数组名,"count"-实际得到的日志条数,"cat"-团购类别名称,"tid"-团购ID
	 * "name"-团购名字,"desc"-团购描述,"firm"-团购商家名称,"sta"-团购开始时间,"end"-团购结束时间,
	 * "count"-该团购总兑换码数量,"remain"-该团购剩余兑换码数量
	 * json例:{count:2,list:[{cat:"餐饮",tid:1,name:"南方高新火锅城团购券",desc:"南方高新火锅城70一人随便吃",
	 * firm:"南方高新火锅城",sta:1234567891011,end:1234567891012,count:10,remain:5}
	 * ,{cat:"娱乐",tid:1,name:"南方高新台球俱乐部券",desc:"南方高新台球俱乐部20一人一下午",
	 * firm:"南方高新台球俱乐部",sta:1234567891011,end:1234567891012,count:10,remain:5}]}
	 */
	public JSONObject getAllTuan(int start,int count){
		
		JSONObject tuanArray = new JSONObject();
		
		PosLogger.log.debug("Get all of Tuan");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("SELECT * FROM temp_firm f,temp_tuan t,temp_category c " +
					" WHERE t.category_id=c.category_id AND f.firm_id=t.firm_id ORDER BY t.tuan_starttime desc " +
							"limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("Have no tuan");
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject tuanJson = new  JSONObject();
				
				int tuanID = rs.getInt("t.tuan_id");
				tuanJson.put("tid", tuanID);
				tuanJson.put("name", rs.getString("t.tuan_name"));
				tuanJson.put("cat", rs.getString("c.category_name"));
				tuanJson.put("desc", rs.getString("t.tuan_desc"));
				tuanJson.put("firm", rs.getString("f.firm_name"));
				tuanJson.put("sta", rs.getLong("t.tuan_starttime"));
				tuanJson.put("end", rs.getLong("rs.tuan_endtime"));
				
				
				//获得团购总数量
				PreparedStatement ps_2 = null;
				
				ResultSet rs_2 = null;
				
				try {
					
					ps_2 = conn.prepareStatement("select count(*) from temp_key where tuan_id=" + tuanID); 
					
					rs_2 = ps_2.executeQuery();
					
					if(rs_2 == null){
						
						PosLogger.log.error("Get count error ,tuan_id = " + tuanID);
						
					}
					
					tuanJson.put("count", rs_2.getInt(1));
					
				}finally{
					
					if(ps_2 != null){
						ps_2.close();
					}
					
					if(rs_2 != null){
						rs_2.close();
					}
					
				}
				
				//获得团购剩余数量
				PreparedStatement ps_3 = null;
				
				ResultSet rs_3 = null;
				
				try {
					
					ps_3 = conn.prepareStatement("select count(*) from temp_key where tuan_id=" + tuanID + " and key_status=1"); 
					
					rs_3 = ps_3.executeQuery();
					
					if(rs_3 == null){
						
						PosLogger.log.error("Get remain count error ,tuan_id = " + tuanID);
						
					}
					
					tuanJson.put("remain", rs_3.getInt(1));
					
				}finally{
					
					if(ps_3 != null){
						ps_3.close();
					}
					
					if(rs_3 != null){
						rs_3.close();
					}
					
				}
				
				array.put(tuanJson);
				
			}
			
			tuanArray.put("list", array);
			tuanArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return tuanArray;
	}
	
	/**
	 * @author penglang
	 * @param tuan(团购对象)
	 * @param keyCount(团购下的兑换码数量)
	 * @param userID(当前操作者ID)
	 * @return boolean是否成功
	 */
	public boolean addTuanAndKey(Tuan tuan,int keyCount,int userID){
		
		boolean isSuccess = true;
		
		PosLogger.log.debug("Add tuan and key,add keycount=" + keyCount);
		
		conn = SQLConn.getConnection();
		
		try {
			
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("insert into temp_tuan(category_id,firm_id,tuan_name,tuan_desc,tuan_starttime,tuan_endtime) values(?,?,?,?,?,?)");
			
			ps.setInt(1, tuan.getCategory_id());
			ps.setInt(2, tuan.getFirm_id());
			ps.setString(3, tuan.getTuan_name());
			ps.setString(4, tuan.getTuan_desc());
			ps.setLong(5, tuan.getTuan_starttime());
			ps.setLong(6, tuan.getTuan_endtime());
			
			int result = ps.executeUpdate();
			
			int tuanID = -1;
			
			if (result > 0) {

				PosLogger.log.info("Insert into " + tablesName + " success");
				
				ps = conn.prepareStatement("SELECT @@IDENTITY;");
				
				rs = ps.executeQuery();
				
				if(rs == null){
					
					isSuccess = false;
					conn.setAutoCommit(true);
					return isSuccess;
					
				}
				
				if(rs.next()){
					
					tuanID = rs.getInt(1);
					
				}
				
			} else {
				PosLogger.log.error("Insert into database error");
				
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			//生成兑换码头
			String keyHead = GenerateKeyCode.generateKeyCodeHead();
			
			KeyDAO key = new KeyDAO();
			
			for(int i = 0 ; i < keyCount ; i++){
				
				//添加兑换码
				ps = conn.prepareStatement("insert into temp_key(tuan_id,key_code,key_status) values(?,?,?)");
				
				ps.setInt(1, tuanID);
				while(true){
					String uniqueCode = keyHead + GenerateKeyCode.generateKeyCodeTail();
					//兑换码唯一，可以使用
					if(key.CheckUniqueCode(uniqueCode)){
						ps.setString(2, uniqueCode);
						break;
					}
					
				}
				ps.setInt(3, 0);
				
				int result_2 = ps.executeUpdate();
				
				if (result_2 > 0) {
					
					PosLogger.log.info("Insert into key success");
					
					
				} else {
					
					PosLogger.log.error("Insert into database error");
					isSuccess = false;
					conn.setAutoCommit(true);
					return isSuccess;
					
				}
				
			}
			
			//添加到log
			ps = conn.prepareStatement("insert into temp_log(user_id,log_time,log_content) values(?,?,?)");
			
			ps.setInt(1, userID);
			ps.setLong(2, System.currentTimeMillis());
			ps.setString(3, "添加团购:\"" + tuan.getTuan_name() + "\",并添加" + keyCount + "个兑换码");
			
			result = ps.executeUpdate();
			
			if (result > 0) {

				PosLogger.log.info("Insert into temp_log success");
				
			} else {
				PosLogger.log.error("Insert into database error");
				
				isSuccess = false;
				conn.setAutoCommit(true);
				return isSuccess;

			}
			
			conn.commit();
			
		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());
		}

		return isSuccess;
		
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
		
		TuanDAO dao = new TuanDAO();
		Tuan f = new Tuan();
//		f.setTuan_id(1);
		f.setCategory_id(2);
		f.setFirm_id(1);
		f.setTuan_desc("这是团的网吧上网的团购");
		f.setTuan_name("了上天网吧1.5元一小时团购票");
		f.setTuan_starttime(System.currentTimeMillis());
		f.setTuan_endtime(System.currentTimeMillis() - 2000);
//		dao.updateTuan(f);
//		Tuan t = dao.getTuanFromDatabase(2);
//		System.out.println(t.getCategory_id());
//		System.out.println(t.getFirm_id());
//		System.out.println(t.getTuan_endtime());
//		JSONObject json = new JSONObject();
//		try {
//			json.put("df", "df");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(json);
		System.out.println(dao.addTuanAndKey(f, 10,1));
	}
	
}
