package cn.panshihao.pos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.User;
import cn.panshihao.pos.tools.PosLogger;

//对user表的操作
public class UserDAO extends SuperDAO {
	
	private final String tablesName = "temp_users";
	
	private final String primaryKeyName = "user_id";
	
	//添加用户操作
	public boolean addUser(User user){
		
		boolean isSuccess = false;
		
		if (user == null) {

			PosLogger.log.error("add user fail,user object is not exist");
			return isSuccess;

		}
		
		// 检查用户名是否为空
		if (user.getUser_name() == null) {

			PosLogger.log.error("add user fail,user name is not exist");
			return isSuccess;

		}

		// 检查密码是否为空
		if (user.getUser_pass() == null) {

			PosLogger.log.error("add user fail,user password is not exist");
			return isSuccess;

		}

		// 检查用户级别是否合法
		if (user.getUser_grade() > 2 || user.getUser_grade() < 0) {

			PosLogger.log.error("add user fail,user grade is not legal");
			return isSuccess;

		}
		
		PosLogger.log.debug("add user , username = "  + user.getUser_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("user_name",user.getUser_name());
		colunmsMap.put("user_pass",user.getUser_pass());
		colunmsMap.put("user_grade",user.getUser_grade());
		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap);
		
		return isSuccess;
		
	}
	
	//修改用户操作
	public boolean updateUser(User user){
		
		boolean isSuccess = false;
		
		if (user == null) {

			PosLogger.log.error("update user fail,user object is not exist");
			return isSuccess;

		}
		
		// 检查userid是否为空
		if (user.getUser_id() <= 0) {

			PosLogger.log.error("update user fail,user id is not exist");
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
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,user.getUser_id(),colunmsMap);
		
		return isSuccess;
		
	}
	
	public boolean deleteUser(int primaryKeyVaule){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("delete user fail,user id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		return isSuccess;
		
	}
	
	//根据主键获得user
	public User getUserFromDatabase(int primaryKeyVaule){
		
		User user = new User();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("select user fail,user id is not exist");
			return null;

		}

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("result is null");
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

		JSONObject josn = new JSONObject();
		try {
			josn.put("haha", "xixi");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(josn);
		
	}
	
}
