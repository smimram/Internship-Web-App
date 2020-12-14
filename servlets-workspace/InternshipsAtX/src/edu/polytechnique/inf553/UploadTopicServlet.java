package edu.polytechnique.inf553;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.servlet.http.Part;

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
			request.setAttribute("programs", programs);
		} else {
			
		}
		
		request.getRequestDispatcher("uploadtopic.jsp").include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost called with parameter "+request.getQueryString());
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String topicTitle = request.getParameter("topicTitle");
		String program_id = request.getParameter("programs");
		String category_id = request.getParameter("categories");
		Part uploadFile = request.getPart("uploadFile");
		
		String errorMessage = checkEntries(firstName, lastName, email, topicTitle, program_id, category_id, uploadFile);
		if(errorMessage.equals("None")) {
			try {
				String supervisor_id;
				
				Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);

				if(!checkEmail(email)) {
					String defaultPass = "12345678";
					String concatName = lastName+", "+firstName;
					String query1 = "insert into person(name, email, creation_date, valid, password)" + 
							" values ('"+concatName+"', '"+email+"', '"+java.time.LocalDate.now().toString()+"', true, '"+defaultPass+"') ;";
					Statement ps1 = con.createStatement();
					ps1.executeUpdate(query1);
					
					
					String query2 = "select id from person where email =? ;";
					PreparedStatement ps2 = con.prepareStatement(query2);
					ps2.setString(1, email);
					supervisor_id = ps1.executeQuery(query2).getString("id");
					
					
					String query3 = "insert into person_roles(role_id, person_id) values (4, "+supervisor_id+");"; //'proponent' id=4
					Statement ps3 = con.createStatement();
					ps3.executeUpdate(query3);
				}
				String query4 = "select id from person where email =? ;";
				PreparedStatement ps4 =  con.prepareStatement(query4);
				ps4.setString(1, email);
				supervisor_id = ps4.executeQuery(query4).getString("id");
				
				
				String query5 = "insert into internship(title, creation_date, content, supervisor_id, scientific_validated, administr_validated, is_taken, program_id)" + 
						" values ("+topicTitle+", "+java.time.LocalDate.now().toString()+", "+uploadFile+", "+supervisor_id+", false, false, false, "+program_id+") ;";
				Statement ps5 = con.createStatement();
				ps5.executeUpdate(query5);
				
				
				String query6 = "select id from internship where name =? ;";
				PreparedStatement ps6 = con.prepareStatement(query6);
				ps6.setString(1, topicTitle);
				String internship_id = ps6.executeQuery(query6).getString("id");
				
				
				String query7 = "insert into internship_category(internship_id, category_id)" + 
						" values ("+internship_id+", "+category_id+") ;";
				Statement ps7 = con.createStatement();
				ps7.executeUpdate(query7);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			
		}
	}
	
	private List<Program> loadData() {
		List<Program> programs = new ArrayList<Program>();
		try {
			Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
			
			String query1 = "SELECT DISTINCT id, name FROM program;";
			PreparedStatement ps1 = con.prepareStatement(query1);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) {
				Program p = new Program(rs1.getString("id"), rs1.getString("name"));
				programs.add(p);
			}
			
			for(int i=0; i<programs.size(); ++i) {
				String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" + 
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

	private String checkEntries(String firstName, String lastName, String email, String topicTitle, String programs, String category_id, Part uploadFile) {
		if(firstName==null || firstName=="") {
			return "Please enter a first name.";
		} else if(lastName==null || lastName=="") {
			return "Please enter a last name.";
		} else if(email==null || email=="") {
			return "Please enter an email.";
		} else if(topicTitle==null || topicTitle=="") {
			return "Please choose a topic title.";
		} else if(programs==null || programs.equals("")) {
			return "Please choose a program.";
		} else if(category_id==null || category_id.equals("na0000")) {
			return "Please choose a category of your program.";
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
					try {
						InputStream inputStream = uploadFile.getInputStream();
						if (uploadFile != null) {
				            inputStream = uploadFile.getInputStream();
				        }
					} catch (IOException e) {
						return "An error occured :( could not load file...";
					}
					return "None";
				}
			} else { 
				return "Please enter a valid email.";
			}
		}  
	}
	
	private boolean checkEmail(String email) {
		boolean st = false;
		try {
			String query = "select * from person where email=?;";
			//creating connection with the database
			Connection con = DriverManager.getConnection(DbUtils.dbUrl, DbUtils.dbUser, DbUtils.dbPassword);
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			st = rs.next();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return st;      
	}
}
