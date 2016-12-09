package org.books;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DbUtil {

	private final static Logger LOGGER = Logger.getLogger(DbUtil.class.getSimpleName());

	private final static String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	private final static String DB_URL = "jdbc:derby://localhost:1527/bookstore";
        
        private final static String DBTEST_URL = "jdbc:derby://localhost:1527/bookstore-test";
        
	private final static String USER = "app";
	private final static String PASS = "app";

	private DbUtil() {}

	public static void executeSql(String sql) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Error executing SQL", ex);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
        
        public static void executeSqlTestDB(String sql) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);

			conn = DriverManager.getConnection(DBTEST_URL, USER, PASS);

			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Error executing SQL", ex);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}