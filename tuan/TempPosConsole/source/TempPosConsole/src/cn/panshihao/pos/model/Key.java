package cn.panshihao.pos.model;

import java.io.Serializable;

public class Key implements Serializable {
	
	private int key_id;
	private int tuan_id;
	private String key_code;
	private int key_status;
	public int getKey_id() {
		return key_id;
	}
	public void setKey_id(int key_id) {
		this.key_id = key_id;
	}
	public int getTuan_id() {
		return tuan_id;
	}
	public void setTuan_id(int tuan_id) {
		this.tuan_id = tuan_id;
	}
	public String getKey_code() {
		return key_code;
	}
	public void setKey_code(String key_code) {
		this.key_code = key_code;
	}
	public int getKey_status() {
		return key_status;
	}
	public void setKey_status(int key_status) {
		this.key_status = key_status;
	}
	
	
	
	
}
