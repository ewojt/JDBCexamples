package eu.ewojt.jdbc;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class User {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://localhost/test";
	private static final String USER = "root";
	private static final String PASSWORD = "password";
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

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
				+ "LASTSTNAME VARCHAR(10) NOT NULL, "
				+ "CREATED_BY VARCHAR(20) NOT NULL, "
				+ "CREATED_DATE DATE NOT NULL, " + "PRIMARY KEY (USER_ID) "
				+ ")";
		try {
			stat.execute(createTable);
			System.out.println("Table \"USER\" is created!");
			System.out.println(createTable);
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
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY, CREATED_DATE) "
				+ "VALUES" + "(1,'misiek','Jan','Kowalski','system', "
				+ "to_date('" + getCurrTimeStamp()
				+ "', 'yyyy/mm/dd hh24:mi:ss'))";
		try {
			stat.execute(insertUser);
			System.out.println("Użytkownik dodany");
			System.out.println(insertUser);
		} catch (SQLException e) {
			System.err.println("Blad przy dodawaniu użytkownika");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Insert a record into table via JDBC PreparedStatement
	public boolean insertUserPreStatement(int id, String login, String name,
			String sname, String who) {
		String insertUser = "INSERT INTO USER"
				+ "(USER_ID, USERNAME, FIRSTNAME, LASTNAME, CREATED_BY, CREATED_DATE) "
				+ "VALUES" + "(?,?,?,?,?,?)";
		try {
			PreparedStatement preparedStatement = conn
					.prepareStatement(insertUser);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, login);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, sname);
			preparedStatement.setString(5, who);
			preparedStatement.setTimestamp(6, getCurrentTimeStamp());
			preparedStatement.executeUpdate();

			System.out.println("Record dodany");
		} catch (SQLException e) {
			System.err.println("Blad przy dodawaniu użytkownika");
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
			System.out.println("Użytkownik zmodifikowany");
			System.out.println(updateUser);
		} catch (SQLException e) {
			System.err.println("Blad przy modifikowaniu użytkownika");
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
			System.out.println("Użytkownik zmodifikowany");
			System.out.println(updateUser);
		} catch (SQLException e) {
			System.err.println("Blad przy modifikowaniu użytkownika");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Delete a record into table via JDBC Statement
	public boolean deleteUserStatement() {
		String deleteUser = "DELETE USER WHERE USER_ID = 1";
		try {
			stat.execute(deleteUser);
			System.out.println("Użytkownik usunięty");
			System.out.println(deleteUser);
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu użytkownika");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Delete a record into table via JDBC PreparedStatement
	public boolean deleteUserPreStatement(int id) {
		String deleteUser = "DELETE USER WHERE USER_ID = ?";
		try {
			PreparedStatement preparedStatement = conn
					.prepareStatement(deleteUser);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
			System.out.println("Użytkownik usunięty");
			System.out.println(deleteUser);
		} catch (SQLException e) {
			System.err.println("Blad przy usuwaniu użytkownika");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//Select records from table via JDBC Statement
    public List<String> selectUserStatement() {
        List<String> users = new LinkedList<String>();
        String selectUser = "SELECT USER_ID, USERNAME from USER";
        try {
            ResultSet result = stat.executeQuery(selectUser);
            while(result.next()) {
               String newstr = "ID uzytkownika: " + result.getString("USER_ID") + " Login: " + result.getString("USERNAME");   
               users.add(new String(newstr));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }
    
	//Select records from table via JDBC Statement
    public String selectUserPreStatement() {
    	String newstr = null;
        String selectUser = "SELECT USER_ID, USERNAME from USER WHERE USER_ID = ?";
        try {
            ResultSet result = stat.executeQuery(selectUser);
            while(result.next()) {
               newstr = "ID uzytkownika: " + result.getString("USER_ID") + " Login: " + result.getString("USERNAME");   
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

	private String getCurrTimeStamp() {

		java.util.Date today = new java.util.Date();
		return dateFormat.format(today.getTime());
	}

	private static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
}
