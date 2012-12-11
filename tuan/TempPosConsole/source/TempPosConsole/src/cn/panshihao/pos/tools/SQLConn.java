package cn.panshihao.pos.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//获得数据库连接 
public class SQLConn {
	
private static String databaseUrl = "jdbc:mysql://127.0.0.1/tempposdb?useUnicode=true&characterEncoding=UTF-8";
	
	private static String driverClassName = "com.mysql.jdbc.Driver";
	
	private static String databaseUserName = "posdba";
	
	private static String databaseUserPassword = "posdba";
	
	
	public static Connection getConnection(){
		
		Connection conn = null;
		
		try {
			
			Class.forName(driverClassName);
			
			conn = DriverManager.getConnection(databaseUrl,databaseUserName,databaseUserPassword);
		
		} catch (ClassNotFoundException e) {
			
			PosLogger.log.error(e.getMessage());
		
		} catch (SQLException e) {

			PosLogger.log.error(e.getMessage());
			
		}
		
		return conn;
		
	}
	
}
