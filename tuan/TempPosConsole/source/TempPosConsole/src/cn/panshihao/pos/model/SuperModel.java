package cn.panshihao.pos.model;

import java.io.Serializable;
import java.util.HashMap;

public class SuperModel implements Serializable {

	public Serializable data = new HashMap();

	public Object getValue(Object key){
		return ((HashMap)data).get(key);
	}
	
	public void putValue(Object key, Object value){
		((HashMap)data).put(key, value);
	}
	
	
	
}
