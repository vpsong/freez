package vp.freez.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import vp.freez.db.annotation.Table;

public class StatementManager {

	private static Logger logger = Logger.getLogger("StatementManager");

	public static PreparedStatement getStatement(Object obj)
			throws SQLException {
		Class<?> cls = obj.getClass();
		Table tableName = (Table) cls.getAnnotation(Table.class);
		Field[] fields = cls.getDeclaredFields();
		StringBuilder sql = new StringBuilder("insert into ");
		StringBuilder values = new StringBuilder(" values(");
		List<Object> args = new ArrayList<Object>(fields.length);
		sql.append(tableName != null ? tableName.value() : cls.getSimpleName())
				.append("(");
		try {
			for (Field f : fields) {
				f.setAccessible(true);
				Object fieldValue = f.get(obj);
				if (fieldValue != null) {
					sql.append(f.getName()).append(",");
					values.append("?,");
					args.add(fieldValue);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		sql.setCharAt(sql.length() - 1, ')');
		values.setCharAt(values.length() - 1, ')');
		sql.append(values);
		logger.info(sql.toString());
		Connection con = ConnectionPool.getPool().getConnection();
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		for (int i = 0; i < args.size(); ++i) {
			stmt.setObject(i + 1, args.get(i));
		}
		return stmt;
	}
}
