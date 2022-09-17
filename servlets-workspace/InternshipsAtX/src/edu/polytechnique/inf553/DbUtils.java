package edu.polytechnique.inf553;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbUtils {
    public static Connection getConnection() {
        try {
            Context ic = new InitialContext();
            Context ec = (Context) ic.lookup("java:comp/env");
            DataSource ds = (DataSource) ec.lookup("jdbc/InternshipsDB");

            return ds.getConnection();
        } catch (NamingException | SQLException e) {
            return null;
        }
    }

    public static void releaseConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
        }
    }
}
