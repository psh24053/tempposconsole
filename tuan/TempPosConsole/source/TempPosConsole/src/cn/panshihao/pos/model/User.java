package cn.panshihao.pos.model;

import java.io.Serializable;

/**
 * @author 彭琅
 */
/*对应用户的实体类*/
public class User extends SuperModel implements Serializable {

	private int user_id;
	private String user_name;
	private String user_pass;
	private int user_grade;
	
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}
	public int getUser_grade() {
		return user_grade;
	}
	public void setUser_grade(int user_grade) {
		this.user_grade = user_grade;
	}
	
	
	
	
	
}
