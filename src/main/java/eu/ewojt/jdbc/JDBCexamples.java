package eu.ewojt.jdbc;

//JDBC example with Statement ( interface to execute a simple SQL statement with no parameters)
//MySQL database is used
public class JDBCexamples {

	public static void main(String[] argv) {
       //Table USER is created
	   User user = new User();
       
       user.closeConnection();
	}
}