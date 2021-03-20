import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {

	private static final String DRIVERCLASS = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	private static final String URL = "jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=Student";
	private static final String USERNAME = "sa";
	private static final String PASSWORD = "";
	private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	
	protected static String ID = null;
	private static  String ID2 ;
	protected static String Name = null;
	protected static String Sex = null;
	private static  String  Administrators = String.valueOf(110); //Has the right to delete the database user
	protected static String Password = null; 

	  // Static method to load database driver
      static {
		 try {
			Class.forName(DRIVERCLASS).newInstance();// Load database driver
			DriverManager.getConnection(URL, USERNAME, PASSWORD);// Create a new database connection
			System.out.println("Driver loaded successfully ");//Console information display
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
      // Create database connection
      public static Connection getConnection() {

		 Connection conn = threadLocal.get();// Get database connection from thread
		   if (conn == null) {// No database connection available
			try {
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);// Create a new database connection
				threadLocal.set(conn);// Save the database connection to the thread
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Database connection is successful "); //Console information view
		return conn;
	}

	  // Close the database connection//not called
	  public static boolean closeConnection() {
		boolean isClosed = true;
		Connection conn = threadLocal.get();//Get database connection from thread
		threadLocal.set(null);// Empty the database connection in the thread
		if (conn != null) {// Database connection available
			try {
				conn.close();// Close database connection
			} catch (SQLException e) {
				isClosed = false;
				e.printStackTrace();
			}
		}
		System.out.println("Database closed successfully "); //Console information view
		return isClosed;
	}
	
	 
	  //User login-query student ID
	  public static int selectUserNumber(String selectUserNumber) {
		 Connection conn = getConnection(); // Get database connection
	     int id = 0; // Define the int object that holds the return value
	     ID2 = selectUserNumber;
	     try {
	          Statement  statment = conn.createStatement(); // Get the Statement object
	          String sql = "select student ID from Student where student ID  = '" + selectUserNumber + "'"; // Define query SQL statement
	          ResultSet rest = statment.executeQuery(sql); // Execute query statement to obtain query result set
	          System.out.println("Query database：" +selectUserNumber + id );//Console data view
	          while (rest.next()){
	            id = rest.getInt(1); // Get query results
	            System.out.println("Database query returns results： " + id);//Console data view
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return id; // Return query result
	    }
	  
	  //User login-query password
	  public static int selectPassword(String selectUserNumber,String Password) {
	      Connection conn = getConnection(); // Get database connection
		  int id = 0; // Define the int object that holds the return value
		   try {
		       Statement statment = conn.createStatement(); // Get the Statement object
		       String sql = "select password from Student where password = '" + Password + "' and student ID  = '" + selectUserNumber + "'"; // Define query SQL statement
		       ResultSet rest = statment.executeQuery(sql); // Execute query statement to obtain query result set
		       System.out.println("Query database： " +Password + id);//Console data view
		       while (rest.next()){
		            id = rest.getInt(1); // Get query results
		            System.out.println("Database query returns results： " + id);//Console data view
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return id; // Return query result
		    }
	  
	  //User information query
	  public static void select(String selectUserNumber) {
	      Connection conn = getConnection(); // Get database connection
	      ID2 = selectUserNumber;//Keep user ID
		   try {
		       Statement statment = conn.createStatement(); // Get the Statement object
		       String sql = "select * from Student where student ID  = '" + selectUserNumber + "'";  // Define query SQL statement
		       ResultSet rest = statment.executeQuery(sql); // Execute query statement to obtain query result set
		       while (rest.next()){
		            ID = rest.getString(1); // Get query results
		            Name = rest.getString(2);
		            Sex = rest.getString(3);
		            Password = rest.getString(4);//No window is displayed
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    } 
	  	  
	  //User information insertion-determine whether the user exists
	  public static int InsertUserNumber1(String InsertuserID,String InsertPassword) {
		   @SuppressWarnings("unused")
		Connection conn = getConnection(); //Get database connection
		   if(selectUserNumber(InsertuserID)!=0){
		    	System.out.println("User already exists");
		        return 0 ; 
		    }
		    else{
	            return 1;
		    }
	  } 
	  
	  //User information insertion
	  public static int InsertUserNumber(String InsertuserID,String InsertUserNumber,String InsertUserSex,String InsertPassword) {
		   Connection conn = getConnection(); // Get database connection
		   if(selectUserNumber(InsertUserNumber)!=0){
		    	System.out.println("The user already exists");
		     return 0 ; 
		    }
		    else{
	            try {
	            PreparedStatement statement = conn
	                    .prepareStatement("insert into Student values('" + InsertuserID + "','" + InsertUserNumber + "','" + InsertUserSex + "','" + InsertPassword + "')"); // Define prepared statements inserted into the database              
	            statement.executeUpdate(); // Execute prepared statement
	            System.out.println("Successful registration");
	            }catch (SQLException e) {
	            e.printStackTrace();
	            }
	        return 1;
		    }
	  }
    
	  //User data update
	  public static int UpdateUserNumber(String updateUserID,String updateUserNumber, String updateUserSex, String updateUserPassword) {
		Connection conn = getConnection(); // Get database connection
		if(updateUserID.equals(ID2) == true){
            try {
            	PreparedStatement statement1 = conn
      	                  .prepareStatement("update Student set Name = '" + updateUserNumber + "' where student ID = '" + updateUserID + "'"); // Define prepared statements inserted into the database
            	PreparedStatement statement2 = conn
              	          .prepareStatement("update Student set gender  = '" + updateUserSex + "' where student ID = '" + updateUserID + "'");
              	PreparedStatement statement3 = conn
            	          .prepareStatement("update Student set password  = '" + updateUserPassword + "' where student ID = '" + updateUserID + "'");
              	statement1.executeUpdate();
              	statement2.executeUpdate();
            	statement3.executeUpdate(); // Execute prepared statement
	            System.out.println("Update runs successfully");
	            }catch (SQLException e) {
	            e.printStackTrace();
	            }
            return 1;
		}
		else
			return 0;

	}
	  
	  //User data deletion
	  public static int DeleteUserNumber(String DeleteuserID) {
		   Connection conn = getConnection(); // Get database connection
			if(Administrators.equals(ID2) == true){
	            try {
	            	PreparedStatement statement1 = conn
	      	                  .prepareStatement("delete from Student where student ID = '" + DeleteuserID + "'"); // Define prepared statements inserted into the database
	              	statement1.executeUpdate(); // Execute prepared statement
		            System.out.println("Delete run successfully");
		            }catch (SQLException e) {
		            e.printStackTrace();
		            }
	            return 1;
			}
			else
				return 0;

		}
	  
      //*********************************************************************	  
	  //All user information query //2017/6/10//Console function is realized, window function is not realized
	  public static int SelectUser() {
		   Connection conn = getConnection(); // Get database connection
				try {
				       Statement statment = conn.createStatement(); // Get the Statement object
				       String sql = "select * from Student ";  // Define query SQL statement
				       ResultSet rest = statment.executeQuery(sql); // Execute query statement to obtain query result set
				       while (rest.next()){
				            ID = rest.getString(1); // Get query results
				            Name = rest.getString(2);
				            Sex = rest.getString(3);
				            Password = rest.getString(4);
				            System.out.println("student ID：" + ID + "   Name：" + Name + "        gender：" + Sex + "        password：" + Password);
				            }
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
	            return 1;
			
		}
	   //**********************************************************************  
}
