package vp.freez.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static ConnectionPool pool = new ConnectionPool();
	
	public static ConnectionPool getPool() {
		return pool;
	}
	
	public Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(  
				  "jdbc:mysql://localhost:3306/freez", "nobody", "nobody");
		return con;
	}
}
