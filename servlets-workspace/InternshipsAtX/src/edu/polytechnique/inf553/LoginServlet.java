package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor 
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
		
		request.setAttribute("email", request.getParameter("email"));

		String email = request.getParameter("email").toLowerCase();
		String pass = request.getParameter("pass");

		//Informs that a user requested access with parameters
		System.out.println(this.getClass().getName() + " doPost method called with path " + request.getRequestURI() + " and Email '"+email);

		String err_message = checkUser(email, pass);
		if(err_message.equals("None"))
		{

			List<String> name_role = getNameRole(email);
			String role = name_role.get(1);
			request.setAttribute("name", name_role.get(0));
			request.setAttribute("role", name_role.get(1));
			if (role.equals("Admin")) {
				response.sendRedirect("./admin-view");
			}
			else if (role.equals("Professor")) {
				request.getRequestDispatcher("./professor-view").forward(request, response);
			}
			else if (role.equals("Student")) {
				System.out.println("Login as student");
				response.sendRedirect("./student-view");
			}
		}
		else {
			request.setAttribute("err_message", err_message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	private String checkUser(String email, String password) {
		String err_message = "None";
		try {
			String query = "select * from person where email=? and password=?;";
			//creating connection with the database
			Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (!rs.getBoolean("valid")) {
					err_message = "Your profile has not been validated yet. <br> Please wait or contact your teacher for support.";
				}
			}
			else {
				err_message = "Username or Password incorrect.";
			}
			con.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return err_message;      
	}
	
	private List<String> getNameRole(String email) {
		List<String> output = new ArrayList<String>();
		try {
			String query = "select name, role from person p inner join person_roles pr on pr.person_id = p.id inner join role_type rt on rt.id = pr.role_id where email = ?;";
			//creating connection with the database
			Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			output.add(rs.getString("name"));
			output.add(rs.getString("role"));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return output ;      
	}


	
}
