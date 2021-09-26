package edu.polytechnique.inf553;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that holds constants useful for accessing the database
 */
public class DbUtils {

	private static final String dbName = "internship_webapp";
	private static final String dbHost = "localhost";
	private static final String dbPort = "5432";
	private static final String dbUser = "postgres";
	private static final String dbPassword = "postgres";
	
	private static final String dbUrl = "jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;

	private List<Connection>availableConnections = new ArrayList<>();
	private List<Connection>usedConnections = new ArrayList<>();
	private static final int MAX_CONNECTIONS = 5;
	
	private static DbUtils db = null;
	
	private DbUtils() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			for (int count = 0; count <MAX_CONNECTIONS; count++) {
	            availableConnections.add(createConnection());
	        }
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
	
	public static DbUtils getInstance(){
        if(db == null) {
			try {
				db = new DbUtils();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return db;
    }
	
    public Connection getConnection() {
        if (availableConnections.size() == 0) {
            System.out.println("All connections are Used !!");
            return null;
        } else {
            Connection con = availableConnections.remove(availableConnections.size() - 1);
            usedConnections.add(con);
            return con;
        }
    }
    
    public boolean releaseConnection(Connection con) {
        if (null != con) {
            usedConnections.remove(con);
            availableConnections.add(con);
            return true;
        }
        return false;
    }

}