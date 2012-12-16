package cn.panshihao.pos.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//读取配置文件
public class PosConsoleConfig {
	
	private String nowSeparator = File.separator;
	
	/**
	 * @author penglang
	 * @param configName(配置文件名称),keyName(要获取的字段名)
	 * @return String(需要获取的字段名对应的值)
	 */
	public String getConfig(String configName,String keyName){
		
		String keyValue = "";
		
		PosLogger.log.debug("Get config porperties info,config file name = " + configName + ",keyname=" + keyName);
		
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("cn" + nowSeparator +
				"panshihao" + nowSeparator + "pos" + nowSeparator + "profile" + nowSeparator + configName);    
		
		Properties properties = new Properties();    
		
		try {    
			
			properties.load(inputStream);    
		
		} catch (IOException e) {    
			PosLogger.log.error(e.getMessage());
		}    
		
		keyValue = properties.getProperty(keyName);
		  
		return keyValue;
		
	}
	
	public static void main(String[] args) {
		PosConsoleConfig c = new PosConsoleConfig();
		String value = c.getConfig("config.properties","version");
		
		System.out.println(value);
		
	}

}
