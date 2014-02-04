package vp.freez.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author vp.song
 * 
 */
public class Dao {

	private static Dao dao = new Dao();

	private Dao() {
	}

	public static Dao getDao() {
		return dao;
	}

	public int insert(Object obj) throws SQLException {
		PreparedStatement stmt = StatementManager.getStatement(obj);
		int ret = stmt.executeUpdate();
		stmt.close();
		return ret;
	}

}
