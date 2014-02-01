package vp.freez.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Dao {
	
	public int insert(Object obj) throws SQLException {
		PreparedStatement stmt = StatementManager.getStatement(obj);
		int ret = stmt.executeUpdate();
		stmt.close();
		ConnectionPool.getPool().retrieve(stmt.getConnection());
		return ret;
	}
	
}
