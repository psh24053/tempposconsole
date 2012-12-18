package cn.panshihao.pos.model;

import java.io.Serializable;

/**
 * @author 彭琅
 */
//对应用户日志的实体类
public class Log extends SuperModel implements Serializable {
	
	private int log_id;
	private int user_id;
	private long log_time;
	private String log_content;
	public int getLog_id() {
		return log_id;
	}
	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public long getLog_time() {
		return log_time;
	}
	public void setLog_time(long log_time) {
		this.log_time = log_time;
	}
	public String getLog_content() {
		return log_content;
	}
	public void setLog_content(String log_content) {
		this.log_content = log_content;
	}
	
	
	
}
