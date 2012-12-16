package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

//对firm表的操作
public class FirmDAO extends SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	private ResultSet rs = null;
	
	private final String tablesName = "temp_firm";
	
	private final String primaryKeyName = "firm_id";
	
	//添加企业操作
	public boolean addFirm(Firm firm,int userID){
		
		boolean isSuccess = false;
		
		if (firm == null) {

			PosLogger.log.error("Add firm fail,firm object is not exist");
			return isSuccess;

		}
		
		// 检查企业名是否为空
		if (firm.getFirm_name() == null) {

			PosLogger.log.error("Add firm fail,firm name is not exist");
			return isSuccess;

		}

		// 检查企业描述是否为空
		if (firm.getFirm_desc() == null) {

			PosLogger.log.error("Add firm fail,firm description is not exist");
			return isSuccess;

		}
		
		// 检查企业地址是否为空
		if (firm.getFirm_address() == null) {

			PosLogger.log.error("Add firm fail,firm address is not exist");
			return isSuccess;

		}
		
		// 检查企业电话是否为空
		if (firm.getFirm_phone() == null) {

			PosLogger.log.error("Add firm fail,firm phone is not exist");
			return isSuccess;

		}
		
		// 检查企业联系人是否为空
		if (firm.getFirm_person() == null) {

			PosLogger.log.error("Add firm fail,firm person is not exist");
			return isSuccess;

		}

		PosLogger.log.debug("Add firm , firmname = "  + firm.getFirm_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("firm_name",firm.getFirm_name());
		colunmsMap.put("firm_desc",firm.getFirm_desc());
		colunmsMap.put("firm_address",firm.getFirm_address());
		colunmsMap.put("firm_person", firm.getFirm_person());
		colunmsMap.put("firm_phone", firm.getFirm_phone());
		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//修改企业操作
	public boolean updateFirm(Firm firm,int userID){
		
		boolean isSuccess = false;
		
		if (firm == null) {

			PosLogger.log.error("Update firm fail,firm object is not exist");
			return isSuccess;

		}
		
		// 检查firmid是否为空
		if (firm.getFirm_id() <= 0) {

			PosLogger.log.error("Update firm fail,firm id is not exist");
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
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,firm.getFirm_id(),colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//删除企业
	public boolean deleteFirm(int primaryKeyVaule,int userID){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Delete firm fail,firm id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule,userID);
		
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
			
			PosLogger.log.error("Result is null");
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
	
	/**
	 * @author penglang
	 * @param start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取所有企业.
	 * json说明:"list"-包含所有企业的数组名,"count"-实际得到的企业条数,"fid"-日志ID,
	 * "name"-企业名称,"desc"-企业描述,"address"-企业地址,"phone"-企业电话,"person"-企业联系人名字
	 * json例:{count:2,list:[{fid:1,name:"南方高新台球俱乐部","desc":"这是一家五星级台球俱乐部",address:"雅安世纪广场",phone:"15008224402",person:"小潘"}
	 * ,{fid:2,name:"南方高新火锅城",desc:"这是一家非常有特色的四川火锅",address:"雅安天上人家",phone:"15008224401",person:"小王"}]}
	 */
	public JSONObject getAllFirm(int start,int count){
		
		JSONObject firmArray = new JSONObject();
		
		PosLogger.log.debug("Get All of firm");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from " + tablesName +
							" limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("Have no firm");
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject firmJson = new  JSONObject();
				firmJson.put("fid", rs.getInt("firm_id"));
				firmJson.put("name", rs.getString("firm_name"));
				firmJson.put("desc", rs.getString("firm_desc"));
				firmJson.put("address", rs.getString("firm_address"));
				firmJson.put("phone", rs.getString("firm_phone"));
				firmJson.put("person", rs.getString("firm_person"));
				
				array.put(firmJson);
				
			}
			
			firmArray.put("list", array);
			firmArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return firmArray;
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
	
	/**
	 * 
	 * @author penglang
	 * @param firmName(商家名)
	 * @return ture(该商家名可以使用),false(该商家名已被使用);
	 */
	public boolean checkFirmNameIsExist(String firmName){
		
		boolean isExist = false;
		
		PosLogger.log.debug("check firm name is exist");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select count(*) from " + tablesName + " where firm_name= ?");
			ps.setString(1, firmName);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("Database error");
			}
			
			if(rs.next()){

				if(rs.getInt(1) == 0){
					
					//商家名未被使用
					isExist = true;
					
				}
				
			}else{
				PosLogger.log.error("Database error");
			}
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		}

		return isExist;
		
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
		dao.updateFirm(f,1);
		
	}
	
}
