package edu.polytechnique.inf553;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of UploadTopicServlet
	 * Query the programs and their categories
	 */
	public UploadTopicServlet() {
		super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI());

		
		String programId = request.getParameter("program");

		if(programId==null) {
			List<Program> programs = loadData();
			Iterator<Program> iter = programs.iterator();
			request.setAttribute("programs", programs);
			request.setAttribute("iter", iter);
		} else {
			
		}
		//"<input type=\"file\" accept=\".pdf\">");
		request.getRequestDispatcher("uploadtopic.jsp").include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost called with parameter "+request.getQueryString());
		doGet(request, response);
	}
	
	private List<Program> loadData() {
		List<Program> programs = new ArrayList<Program>();
		try {
			Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
			
			String query1 = "SELECT id, name FROM program;";
			PreparedStatement ps1 = con.prepareStatement(query1);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) {
				Program p = new Program(rs1.getString("id"), rs1.getString("name"));
				programs.add(p);
			}
			
			for(int i=0; i<programs.size(); ++i) {
				String query = "SELECT c.description AS desc, c.id as id \n" + 
						"FROM categories c\n" + 
						"INNER JOIN program_category pc ON pc.cat_id = c.id\n" + 
						"WHERE pc.program_id ="+programs.get(i).getId()+";";
				ResultSet rs = con.prepareStatement(query).executeQuery();
				while(rs.next()) {
					Category c = new Category(rs.getString("desc"), rs.getString("id"));
					programs.get(i).addCategory(c);
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return programs;
	}

	private String checkEntries(String firstName, String lastName, String email, String password, String confirmPassword, String role) {
		if(firstName==null || firstName=="") {
			return "Please enter a first name.";
		} else if(lastName==null || lastName=="") {
			return "Please enter a last name.";
		} else if(email==null || email=="") {
			return "Please enter an email.";
		} else if(password==null || password=="") {
			return "Please enter a password.";
		} else if(confirmPassword==null || confirmPassword=="") {
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
							"WHERE email = '"+email+"' ;";
					//creating connection with the database
					Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
					PreparedStatement ps = con.prepareStatement(query);
					ResultSet rs = ps.executeQuery();
					rs.next();
					emailTaken = rs.getInt("count")>0;
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
