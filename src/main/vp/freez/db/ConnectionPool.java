package vp.freez.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author vp.song
 * 
 */
public class ConnectionPool {

	private BlockingQueue<Connection> connectionQueue = new LinkedBlockingQueue<Connection>();
	private static ConnectionPool pool = new ConnectionPool(50);

	private ConnectionPool(int initSize) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			for (int i = 0; i < initSize; ++i) {
				Connection con = DriverManager
						.getConnection("jdbc:mysql://localhost:3306/freez",
								"nobody", "nobody");
				connectionQueue.add(con);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ConnectionPool getPool() {
		return pool;
	}

	@SuppressWarnings("resource")
	public Connection getConnection() throws SQLException {
		Connection con = connectionQueue.poll();
		while (con == null || con.isClosed()) {
			con = connectionQueue.poll();
		}
		return con;
	}

	public void retrieve(Connection con) {
		connectionQueue.add(con);
	}
}
