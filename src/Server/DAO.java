package Server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * This class used to load and install JDBC drivers for communicate
 * With MySQL database, Also used for execute and update Application database.
 * @author Stanislav Bodik
 */

public class DAO {

	//DB connecting information
	private String dbAdress,dbName,dbUserName,dbPassword;
	
	// Statement for executing queries
	private Statement dbStateMent;

	public DAO(String dbAdress, String dbName, String dbUserName, String dbPassword) {
		super();
		this.dbAdress = dbAdress;
		this.dbName = dbName;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		
		initializeDB();
		

	 	FileInputStream fstream;
		try {
			fstream = new FileInputStream("firespace_player.sql");
		 	createDB(fstream);
			fstream = new FileInputStream("firespace_game.sql");
		 	createDB(fstream);
		 	fstream = new FileInputStream("firespace_hitevent.sql");
		 	createDB(fstream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// create new DB if not exists , loads create commands from file
	private void createDB(FileInputStream fstream){
		 
		try {
	       // Get the object of DataInputStream
	       DataInputStream in = new DataInputStream(fstream);
	       BufferedReader br = new BufferedReader(new InputStreamReader(in));
	       String strLine;
	       //Read File Line By Line
	       while ((strLine = br.readLine()) != null)
	       { System.out.println(strLine);
	         if (strLine != null && !strLine.equals(""))
				
					dbStateMent.execute(strLine);       
	       }
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	 }
		
	public int update(String query){
		try {
			return dbStateMent.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public  ResultSet executeQuery(String query){
		
		try {
			return dbStateMent.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
 	
	private void initializeDB(){ 
		try
		{ 
			// Load the JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// Establish a connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://"+dbAdress+"/"+dbName,dbUserName,dbPassword);
			// Create a statement
			dbStateMent = connection.createStatement();
		}
		catch (Exception ex)
		{ 
			ex.printStackTrace();
		}
	}
	
}
