package eu.ewojt.jdbc.statements;

import java.util.List;

//JDBC example with Statement ( interface to execute a simple SQL statement with no parameters)
//MySQL database is used
public class JDBCexamples {

	public static void main(String[] argv) {
       
	   // Table USER is created
	   User user = new User();
	   // Inserts  
	   user.insertUserStatement();
	   user.insertUserPreStatement(99, "zuza", "Zuzanna", "Iksinska", "admin1");
	   user.insertUserPreStatement(999, "kotek", "Tomek", "Igrek", "admin1");
	   // Update
	   user.updateUserStatement();
	   user.updateUserPreStatement("zuzka2", 99);
	   // Delete
	   user.deleteUserStatement();
	   user.deleteUserPreStatement(99);
	   // Batch
	   user.batchStatement();
	   user.batchPreStatement();
	   
	   List<String> users = user.selectUserStatement();

       System.out.println("Lista u¿ytkowników: ");
       for(String c: users)
           System.out.println(c);
       
       user.closeConnection();
	}
}