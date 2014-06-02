package vp.freez.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 数据库连接池
 * @author vp.song
 * 
 */
public class ConnectionPool {

	private static Logger logger = Logger.getLogger("ConnectionPool");
	private BlockingQueue<Connection> connectionQueue = new LinkedBlockingQueue<Connection>();
	/**
	 * 初始化连接数50
	 */
	private static ConnectionPool pool = new ConnectionPool(50);
	/**
	 * 当前线程的connection
	 */
	private ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();
	/**
	 * 是否需要rollback
	 */
	private ThreadLocal<Boolean> isRollback = new ThreadLocal<Boolean>();

	private final String DBURL = "jdbc:mysql://localhost:3306/freez";
	private final String DBUSER = "root";
	private final String DBPASSWORD = "root";

	private ConnectionPool(int initSize) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			logger.info("=====init connection pool=====");
			for (int i = 0; i < initSize; ++i) {
				Connection con = DriverManager.getConnection(DBURL, DBUSER,
						DBPASSWORD);
				// request 级别事务
				con.setAutoCommit(false);
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

	public Connection getConnection() throws SQLException {
		Connection con = currentConnection.get();
		if(con == null || con.isClosed()) {
			con = connectionQueue.poll();
			currentConnection.set(con);
		}
		if (con == null || con.isClosed()) {
			con = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
			con.setAutoCommit(false);
		}
		return con;
	}

	/**
	 * 回收
	 * @param con
	 */
	public void retrieve(Connection con) {
		connectionQueue.add(con);
	}
	
	public void commitOrRollback() {
		Boolean irb = isRollback.get();
		if(irb == null || irb == false) {
			commit();
		} else {
			rollback();
		}
	}
	
	public void commit() {
		Connection con = currentConnection.get();
		if(con != null) {
			try {
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			retrieve(con);
		}
	}
	
	/**
	 * 标记为rollback
	 */
	public void markRollback() {
		isRollback.set(true);
	}
	
	public void rollback() {
		Connection con = currentConnection.get();
		if(con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void destroy() {
		logger.info("=====destroy connection poll=====");
		Connection con = null;
		try {
			while ((con = connectionQueue.poll()) != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connectionQueue = null;
	}
}
