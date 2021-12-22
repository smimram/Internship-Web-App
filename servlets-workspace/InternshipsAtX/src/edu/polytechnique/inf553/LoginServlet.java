package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			System.out.println(err_message);
			Person user = getUserInfo(email);
			String role = user.getRole();
			String userName = user.getName();
			System.out.println("user " + userName + " log in as " + role);
			
			// create a new session
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			if (role.equals("Student")) {
				response.sendRedirect("./student-view");
			} 
			else {
				response.sendRedirect("./dashboard");
			}
		}	
		else {
			request.setAttribute("err_message", err_message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	private String checkUser(String email, String password) {
		String err_message = "None";
		Connection con = null;
		try {
			String query = "select * from person where email=? and password=crypt(?, password);";
			//creating connection with the database
			con = DbUtils.getConnection();
			if (con == null) {
				return "Failed connection to database!";
			}
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

		}
		catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.releaseConnection(con);
		}
		return err_message;      
	}
	
	private Person getUserInfo(String email) {
		Person user = null;
		Connection con = null;
		try {
			String query = "select name, role, person_id, valid, email " +
					"from person p " +
					"inner join person_roles pr on pr.person_id = p.id " +
					"inner join role_type rt on rt.id = pr.role_id " +
					"where email = ?;";
			//creating connection with the database
			con = DbUtils.getConnection();
			if (con == null) {
				return null;
			}
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				user = new Person(rs.getString("name"), rs.getInt("person_id"), rs.getString("role"), rs.getBoolean("valid"), rs.getString("email"));
			}

		}
		catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.releaseConnection(con);
		}
		return user;  
	}


	
}
