package eu.ewojt.jdbc;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//JDBC example with Statement ( interface to execute a simple SQL statement with no parameters)
//MySQL database is used
public class JDBCexamples {

	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://localhost/test";
	private static final String USER = "root";
	private static final String PASSWORD = "password";

	public static void main(String[] argv) {
		try {
			createUserT();
		} catch (SQLException e) {
			System.out.println("Error message: " + e.getMessage());
		}
	}

	private static void createUserT() throws SQLException {

		Connection dbConnection = null;
		Statement statement = null;

		String createTable = "CREATE TABLE USER("
				+ "USER_ID INT(5) NOT NULL, "
				+ "USERNAME VARCHAR(20) NOT NULL, "
				+ "FIRSTNAME VARCHAR(10) NOT NULL, "
				+ "LASTSTNAME VARCHAR(10) NOT NULL, "
				+ "CREATED_BY VARCHAR(20) NOT NULL, "
				+ "CREATED_DATE DATE NOT NULL, " + "PRIMARY KEY (USER_ID) "
				+ ")";
		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			statement.execute(createTable);
			System.out.println("Table \"USER\" is created!" );
			System.out.println(createTable);

		} catch (SQLException e) {
			System.out.println("ERROR message: "+ e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	private static Connection getDBConnection() {

		Connection dbConnect = null;

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR message: " + e.getMessage());
		}

		try {
			dbConnect = DriverManager.getConnection(CONNECTION, USER,
					PASSWORD);
			return dbConnect;
		} catch (SQLException e) {
			System.out.println("ERROR message: "+ e.getMessage());
		}
		return dbConnect;
	}
}