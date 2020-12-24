package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.mail.internet.AddressException;

public class SigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SigninServlet() {
		super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		request.getRequestDispatcher("signin.jsp").include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doPost method called with path " + request.getRequestURI());

		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email").toLowerCase();
		String confirmEmail = request.getParameter("confirmEmail").toLowerCase();
		String pass = request.getParameter("pass");
		String confirmPass = request.getParameter("confirmPass");
		String role = request.getParameter("role");

		String concatName = lastName+", "+firstName;

		String errorMessage = checkEntries(firstName, lastName, email, confirmEmail, pass, confirmPass, role);
		if(errorMessage.equals("None"))
		{
			try {
				String query = "insert into person(name, email, creation_date, valid, password)\n" + 
						" values (?, ?, '"+java.time.LocalDate.now().toString()+"', false, ?) ;";
				//creating connection with the database
				Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, concatName);
				ps.setString(2, email);
				ps.setString(3, pass);
				ps.executeUpdate();
				con.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			request.setAttribute("email", email);
			request.getRequestDispatcher("signin_complete.jsp").forward(request, response);
		}
		else
		{
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.setAttribute("email", email);
			request.setAttribute("confirmEmail", confirmEmail);
			request.setAttribute("role", role);
			request.setAttribute("err_message", errorMessage);
			request.getRequestDispatcher("signin.jsp").forward(request, response);
		}
	}

	private String checkEntries(String firstName, String lastName, String email, String confirmEmail, String password, String confirmPassword, String role) {
		if(firstName==null || firstName.equals("")) {
			return "Please enter a first name.";
		} else if(lastName==null || lastName.equals("")) {
			return "Please enter a last name.";
		} else if(email==null || email.equals("")) {
			return "Please enter an email.";
		} else if(confirmEmail==null || confirmEmail.equals("")) {
			return "Please confirm email.";
		} else if(!confirmEmail.equals(email)) {
			return "Emails don't match.";
		} else if(password==null || password.equals("")) {
			return "Please enter a password.";
		} else if(confirmPassword==null || confirmPassword.equals("")) {
			return "Please confirm password.";
		} else if(role==null) {
			return "Please choose a role.";
		} else if(password.length()<8) {
			return "Password must have at least 8 characters.";
		} else if(!password.equals(confirmPassword)) {
			return "Passwords don't match.";
		} else {
			boolean emailIsValid = true;
			try {
				InternetAddress emailAddr = new InternetAddress(email);
				emailAddr.validate();
			} catch (AddressException e) {
				e.printStackTrace();
				emailIsValid = false;
			}
			if(emailIsValid) {
				boolean emailTaken = false;
				try {
					String query = "SELECT COUNT(*) as count\n" + 
							"FROM person \n" + 
							"WHERE email = ? ;";
					//creating connection with the database
					Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, email);
					ResultSet rs = ps.executeQuery();
					rs.next();
					emailTaken = rs.getInt("count")>0;
					con.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
					emailTaken = true;
				}
				if(emailTaken) {
					return "The email is already used.";
				} else {
					return "None";
				}
			} else {
				return "Please enter a valid email.";
			}
		}  
	}
}
