package edu.polytechnique.inf553;

/**
 * Utility class that holds constants useful for accessing the database
 */
public final class DbUtils {

	public static final String dbName = "internship_webapp";
	public static final String dbHost = "localhost";
	public static final String dbPort = "5432";
	public static final String dbUser = "postgres";
	public static final String dbPassword = "postgres";
	
	public static final String dbUrl = "jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;
}
