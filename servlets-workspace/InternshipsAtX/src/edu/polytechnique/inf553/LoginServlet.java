package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor (see InternshipServlet)
	 */
	public LoginServlet() {
		super();	
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doPost method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		
		request.setAttribute("email", request.getParameter("email"));

		String email = request.getParameter("email");
		String pass = request.getParameter("pass");

		//Informs that a user requested access with parameters
		System.out.println(this.getClass().getName() + " doPost method called with path " + request.getRequestURI() + " and Email '"+email+"' and password '"+pass+"'");

		if(checkUser(email, pass))
		{
			request.getRequestDispatcher("login_home.jsp").forward(request, response);
		}
		else
		{
			request.setAttribute("err_message", "Username or Password incorrect");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	private boolean checkUser(String email, String password) {
		boolean st = false;
		try {
			String query = "select * from person where email=? and password=?;";
			//creating connection with the database
			Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			st = rs.next();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return st;      
	}

	
}
