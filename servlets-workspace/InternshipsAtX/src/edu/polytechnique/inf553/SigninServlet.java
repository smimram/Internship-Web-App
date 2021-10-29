package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
		List<Program> programs = getAllPrograms();
		System.out.println(programs);
		request.setAttribute("programs", programs);
		System.out.println(request.getAttributeNames().toString());
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
		int programStudent = Integer.parseInt(request.getParameter("programStudent"));

		String concatName = lastName+", "+firstName;

		String errorMessage = checkEntries(firstName, lastName, email, confirmEmail, pass, confirmPass, role);
		if(errorMessage.equals("None"))
		{
			Connection con = null;
			try {
				//creating connection with the database
				con = DbUtils.getInstance().getConnection();
				if (con == null) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				
				//add the user into 'person' table
				String query = "insert into person(name, email, creation_date, valid, password)\n" + 
						" values (?, ?, '"+java.time.LocalDate.now().toString()+"', false, crypt(?, gen_salt('bf'))) ;";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, concatName);
				ps.setString(2, email);
				ps.setString(3, pass);
				ps.executeUpdate();
				
				//add a person_role relation into 'person_roles' table
				String query2 = "insert into person_roles(role_id ,person_id)\n" + 
						"SELECT rt.id, p.id\n" + 
						"FROM role_type rt, person p\n" + 
						"WHERE rt.role = ? AND p.email = ?";
				PreparedStatement ps2 = con.prepareStatement(query2);
				ps2.setString(1, role);
				ps2.setString(2, email);
				ps2.executeUpdate();

				if(role.equals("Student")) {
					query2 = "INSERT INTO person_program (program_id, person_id) VALUES (" + programStudent + " AS program_id, SELECT p.id FROM person p WHERE p.email = ?)";
					ps = con.prepareStatement(query2);
					ps.setString(1, email);
					ps.executeUpdate();
				}

			}
			catch(SQLException e) {
				e.printStackTrace();
			} finally {
				DbUtils.getInstance().releaseConnection(con);
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
				Connection con = null;
				try {
					String query = "SELECT COUNT(*) as count\n" + 
							"FROM person \n" + 
							"WHERE email = ? ;";
					//creating connection with the database
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						return "failed connection to fatabase!";
					}
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, email);
					ResultSet rs = ps.executeQuery();
					rs.next();
					emailTaken = rs.getInt("count")>0;

				}
				catch(SQLException e) {
					e.printStackTrace();
					emailTaken = true;
				} finally {
					DbUtils.getInstance().releaseConnection(con);
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

	private List<Program> getAllPrograms() {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				return null;
			}

			List<Program> programs = new ArrayList<>();
			// get all program list
			String query = "SELECT DISTINCT id, name, year FROM program ORDER BY name;";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Program program = new Program(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("year"));
				programs.add(program);
			}

			return programs;

		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
	}
}
