package vp.freez.db;

import java.sql.SQLException;

public class Model {
	
	private Dao dao = Dao.getDao();
	
	public int save() {
		try {
			return dao.insert(this);
		} catch (SQLException e) {
			e.printStackTrace();
			ConnectionPool.getPool().markRollback();
		}
		return 0;
	}

}
