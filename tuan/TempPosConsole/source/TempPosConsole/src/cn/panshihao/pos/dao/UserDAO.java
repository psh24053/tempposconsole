package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.User;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

//对user表的操作
public class UserDAO extends SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	private ResultSet rs = null;
	
	private final String tablesName = "temp_users";
	
	private final String primaryKeyName = "user_id";
	
	//添加用户操作
	public boolean addUser(User user,int userID){
		
		boolean isSuccess = false;
		
		if (user == null) {

			PosLogger.log.error("Add user fail,user object is not exist");
			return isSuccess;

		}
		
		// 检查用户名是否为空
		if (user.getUser_name() == null) {

			PosLogger.log.error("Add user fail,user name is not exist");
			return isSuccess;

		}

		// 检查密码是否为空
		if (user.getUser_pass() == null) {

			PosLogger.log.error("Add user fail,user password is not exist");
			return isSuccess;

		}

		// 检查用户级别是否合法
		if (user.getUser_grade() > 2 || user.getUser_grade() < 0) {

			PosLogger.log.error("Add user fail,user grade is not legal");
			return isSuccess;

		}
		
		PosLogger.log.debug("Add user , username = "  + user.getUser_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("user_name",user.getUser_name());
		colunmsMap.put("user_pass",user.getUser_pass());
		colunmsMap.put("user_grade",user.getUser_grade());
		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	//修改用户操作
	public boolean updateUser(User user,int userID){
		
		boolean isSuccess = false;
		
		if (user == null) {

			PosLogger.log.error("Update user fail,user object is not exist");
			return isSuccess;

		}
		
		// 检查userid是否为空
		if (user.getUser_id() <= 0) {

			PosLogger.log.error("Update user fail,user id is not exist");
			return isSuccess;

		}

		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		
		if (user.getUser_name() != null) {

			colunmsMap.put("user_name",user.getUser_name());
		}

		if (user.getUser_pass() != null) {

			colunmsMap.put("user_pass",user.getUser_pass());

		}
		
		if (user.getUser_grade() < 3 || user.getUser_grade() > 0) {

			colunmsMap.put("user_grade",user.getUser_grade());

		}
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,user.getUser_id(),colunmsMap,userID);
		
		return isSuccess;
		
	}
	
	public boolean deleteUser(int primaryKeyVaule,int userID){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Delete user fail,user id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule,userID);
		
		return isSuccess;
		
	}
	
	//根据主键获得user
	public User getUserFromDatabase(int primaryKeyVaule){
		
		User user = new User();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("Select user fail,user id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("Result is null");
			return null;
			
		}
		
		//加入数据
		if(dataMap.get("user_id") != null){
			user.setUser_id((int)dataMap.get("user_id"));
		}
		if(dataMap.get("user_grade") != null){
			
			user.setUser_grade((int)dataMap.get("user_grade"));
		}
		if(dataMap.get("user_name") != null){
			
			user.setUser_name((String)dataMap.get("user_name"));
		}

		return user;
		
	}
	
	/**
	 * @author penglang
	 * @param start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取所有用户包括管理员和操作员.
	 * json说明:"list"-包含所有用户的数组名,"count"-实际得到的用户条数,"uid"-用户ID,
	 * "name"-用户名称,"grade"-用户级别(1为管理员,2为操作员).
	 * json例:{count:2,list:[{uid:1,name:"潘老板",grade:1},{uid:2,name:"小王",grade:2}]}.
	 */
	public JSONObject getAllUser(int start,int count){
		
		JSONObject allUserArray = new JSONObject();
		
		PosLogger.log.debug("Get all of users");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from " + tablesName + " limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("Database have no user");
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject userJson = new  JSONObject();
				userJson.put("uid", rs.getInt("user_id"));
				userJson.put("name", rs.getString("user_name"));
				userJson.put("grade", rs.getInt("user_grade"));
				
				array.put(userJson);
				
			}
			
			allUserArray.put("list", array);
			allUserArray.put("count", array.length());
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return allUserArray;
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
		
//		UserDAO dao = new UserDAO();
//		User user = new User();
//		user.setUser_id(1);
//		user.setUser_name("李姐和我");
//		user.setUser_pass("666888");
//		user.setUser_grade(1);
//		dao.updateUser(user);
//		User user = dao.getUserFromDatabase(1);
//		System.out.println(user.getUser_name());

		UserDAO user = new UserDAO();
		user.getAllUser(0,10);
		
	}
	
}
