package eu.ewojt.jdbc;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class User {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://localhost/test";
	private static final String USER = "root";
	private static final String PASSWORD = "password";

	private Connection conn;
	private Statement stat;

	public User() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Brak sterownika JDBC");
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
			stat = conn.createStatement();
		} catch (SQLException e) {
			System.err.println("Problem z otwarciem polaczenia");
			e.printStackTrace();
		}
		// Statement
		createTableUser();
	}

	public boolean createTableUser() {
		String createTable = "CREATE TABLE USER(" + "USER_ID INT(5) NOT NULL, "
				+ "USERNAME VARCHAR(20) NOT NULL, "
				+ "FIRSTNAME VARCHAR(10) NOT NULL, "
				+ "LASTNAME VARCHAR(10) NOT NULL, "
				+ "CREATED_BY VARCHAR(20) NOT NULL, "
				+ "PRIMARY KEY (USER_ID) "
				+ ")";
		try {
			stat.execute(createTable);
			System.out.println("Table \"USER\" is created!");
		} catch (SQLException e) {
			System.err.println("Blad przy tworzeniu tabeli \"USER\"");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Insert a record into table via JDBC Statement
	public boolean insertUserStatement() {
		String insertUser = "INSERT INTO USER"
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY) "
				+ "VALUES" + "(1,'misiek','Jan','Kowalski','system')";
		try {
			stat.execute(insertUser);
			System.out.println("U¿ytkownik dodany MISIEK");
		} catch (SQLException e) {
			System.err.println("Blad przy dodawaniu u¿ytkownika - misiek");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Insert a record into table via JDBC PreparedStatement
	public boolean insertUserPreStatement(int id, String login, String name,
			String sname, String who) {
		String insertUser = "INSERT INTO USER"
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY) "
				+ "VALUES" + "(?,?,?,?,?)";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(insertUser);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, login);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, sname);
			preparedStatement.setString(5, who);
			preparedStatement.executeUpdate();

			System.out.println("U¿ytkownik dodany - " + login);
		} catch (SQLException e) {
			System.err.println("Blad przy dodawaniu u¿ytkownika - " + login);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Update a record into table via JDBC Statement
	public boolean updateUserStatement() {
		String updateUser = "UPDATE USER" + " SET USERNAME = 'monky' "
				+ " WHERE USER_ID = 1";
		try {
			stat.execute(updateUser);
			System.out.println("U¿ytkownik zmodifikowany - monky");
		} catch (SQLException e) {
			System.err.println("Blad przy modifikowaniu u¿ytkownika = monky");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Update a record into table via JDBC PreparedStatement
	public boolean updateUserPreStatement(String login, int id) {
		String updateUser = "UPDATE USER" + " SET USERNAME = ? "
				+ " WHERE USER_ID = ?";
		try {
			PreparedStatement preparedStatement = conn
					.prepareStatement(updateUser);
			preparedStatement.setString(1, login);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
			System.out.println("U¿ytkownik zmodifikowany - " + login);
		} catch (SQLException e) {
			System.err.println("Blad przy modifikowaniu u¿ytkownika - " + login);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Delete a record into table via JDBC Statement
	public boolean deleteUserStatement() {
		String deleteUser = "DELETE FROM USER WHERE USER_ID = 999";
		try {
			stat.execute(deleteUser);
			System.out.println("U¿ytkownik usuniêty id: 999");
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu u¿ytkownika id: 999");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Delete a record into table via JDBC PreparedStatement
	public boolean deleteUserPreStatement(int id) {
		String deleteUser = "DELETE FROM USER WHERE USER_ID = ?";
		try {
			PreparedStatement preparedStatement = conn
					.prepareStatement(deleteUser);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			System.out.println("U¿ytkownik usuniêty id:" + id);
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu u¿ytkownika id:" + id);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Select records from table via JDBC Statement
	public List<String> selectUserStatement() {
		List<String> users = new LinkedList<String>();
		String selectUser = "SELECT USER_ID, USERNAME from USER";
		try {
			ResultSet result = stat.executeQuery(selectUser);
			while (result.next()) {
				String newstr = "ID uzytkownika: "
						+ result.getString("USER_ID") + " Login: "
						+ result.getString("USERNAME");
				users.add(new String(newstr));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return users;
	}

	// Select records from table via JDBC Statement
	public String selectUserPreStatement(int id) {
		String newstr = null;
		String selectUser = "SELECT USER_ID, USERNAME from USER WHERE USER_ID = ?";
		try {
			PreparedStatement preparedStatement = conn
					.prepareStatement(selectUser);
			preparedStatement.setInt(1, id);
			ResultSet result = preparedStatement.executeQuery(selectUser);
			while (result.next()) {
				newstr = "ID uzytkownika: " + result.getString("USER_ID")
						+ " Login: " + result.getString("USERNAME");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return newstr;
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("Problem z zamknieciem polaczenia");
			e.printStackTrace();
		}
	}

	// Batch process via JDBC Statement
	public boolean batchStatement() {
		String insertUser1 = "INSERT INTO USER"
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY) "
				+ "VALUES" + "(101,'us1','Zosia','Kapusta','root' )";

		String insertUser2 = "INSERT INTO USER"
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY) "
				+ "VALUES" + "(102,'us2','Marek','Kalafior','root' )";

		String insertUser3 = "INSERT INTO USER"
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY) "
				+ "VALUES" + "(103,'us3','Tomy','Lee','root')";

		try {
			conn.setAutoCommit(false);
			stat.addBatch(insertUser1);
			stat.addBatch(insertUser2);
			stat.addBatch(insertUser3);
			stat.executeBatch();
			conn.commit();
			System.out.println("U¿ytkownicy dodani!");
		} catch (SQLException e) {
			System.err.println("Blad przy dodawaniu u¿ytkowników");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Batch process via JDBC PreparedStatement
	public boolean batchPreStatement() {
		String insertUser = "INSERT INTO USER"
			+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY) "
			+ "VALUES" + "(?,?,?,?,?)";

		try {
			//Commit transaction manually
			conn.setAutoCommit(false);
			PreparedStatement preparedStatement = conn.prepareStatement(insertUser);
			 
			preparedStatement.setInt(1, 10);
			preparedStatement.setString(2, "us10");
			preparedStatement.setString(3, "Liliy");
			preparedStatement.setString(4, "Cotty");
			preparedStatement.setString(5, "root");
			
			preparedStatement.addBatch();
			 
			preparedStatement.setInt(1, 11);
			preparedStatement.setString(2, "us11");
			preparedStatement.setString(3, "Mike");
			preparedStatement.setString(4, "Knigth");
			preparedStatement.setString(5, "root");
			
			preparedStatement.addBatch();
			
			preparedStatement.executeBatch();
			conn.commit();
			System.out.println("U¿ytkownicy dodani!");
		} catch (SQLException e) {
			System.err.println("Blad przy dodawaniu u¿ytkowników");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
