package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.User;
import cn.panshihao.pos.tools.PosLogger;

//对user表的操作
public class UserDAO extends SuperDAO {
	
	private String tablesName = "temp_users";
	
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
	
	public static void main(String[] args) {
		
		UserDAO dao = new UserDAO();
		User user = new User();
		user.setUser_name("1");
		user.setUser_pass("2");
		user.setUser_grade(1);
		dao.addUser(user);
		
	}
	
}
