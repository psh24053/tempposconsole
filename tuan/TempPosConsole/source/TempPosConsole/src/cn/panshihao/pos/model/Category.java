package cn.panshihao.pos.model;

import java.io.Serializable;

/**
 * @author 彭琅
 */
//类别实体类
public class Category implements Serializable{
	
	private int category_id;
	private String category_name;
	private String category_desc;
	private String category_form;
	
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getCategory_desc() {
		return category_desc;
	}
	public void setCategory_desc(String category_desc) {
		this.category_desc = category_desc;
	}
	public String getCategory_form() {
		return category_form;
	}
	public void setCategory_form(String category_form) {
		this.category_form = category_form;
	}
	
	
	
	
}
