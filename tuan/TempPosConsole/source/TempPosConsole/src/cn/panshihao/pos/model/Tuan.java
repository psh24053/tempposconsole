package cn.panshihao.pos.model;

import java.io.Serializable;

public class Tuan extends SuperModel implements Serializable {
	
	private int tuan_id;
	private int category_id;
	private int firm_id;
	private String tuan_name;
	private String tuan_desc;
	private long tuan_starttime;
	private long tuan_endtime;
	public int getTuan_id() {
		return tuan_id;
	}
	public void setTuan_id(int tuan_id) {
		this.tuan_id = tuan_id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getFirm_id() {
		return firm_id;
	}
	public void setFirm_id(int firm_id) {
		this.firm_id = firm_id;
	}
	public String getTuan_name() {
		return tuan_name;
	}
	public void setTuan_name(String tuan_name) {
		this.tuan_name = tuan_name;
	}
	public String getTuan_desc() {
		return tuan_desc;
	}
	public void setTuan_desc(String tuan_desc) {
		this.tuan_desc = tuan_desc;
	}
	public long getTuan_starttime() {
		return tuan_starttime;
	}
	public void setTuan_starttime(long tuan_starttime) {
		this.tuan_starttime = tuan_starttime;
	}
	public long getTuan_endtime() {
		return tuan_endtime;
	}
	public void setTuan_endtime(long tuan_endtime) {
		this.tuan_endtime = tuan_endtime;
	}
	
	
	
	
}
