package cn.panshihao.pos.tools;

import java.io.File;

import org.apache.log4j.*;

public class PosLogger {

	public static Logger log = Logger.getLogger("cn.panshihao.pos");
	
	private static String nowSeparator = File.separator;
	
	//加载配置文件
	static {
		PropertyConfigurator.configure("src" + nowSeparator + "cn" + nowSeparator +
				"panshihao" + nowSeparator + "pos" + nowSeparator + "profile" + nowSeparator + "log4j.properties");
	}
	
	private PosLogger() {
		
	}
	
}
