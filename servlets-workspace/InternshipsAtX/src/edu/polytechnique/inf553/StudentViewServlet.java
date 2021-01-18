package edu.polytechnique.inf553;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StudentViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 16177215;

	/**
	 * Constructor 
	 */ 
	public StudentViewServlet() {
		super();	
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			if (role.equals("Student")) {
				
				List<Program> programs = new ArrayList<Program>();
				List<SubjectsPerCategory> subjectsPerCategory = new ArrayList<SubjectsPerCategory>();
				int userId = user.getId();
				
				//======================== DATA LOADING PART ========================
				Connection con = null;
				try {
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					
					// check if the user already has an internship
					String query0 = "select i.id as id, i.title as title, p.email as email, p.name as name\n" + 
							"FROM internship i\n" + 
							"INNER JOIN person p on i.supervisor_id = p.id\n" + 
							"INNER JOIN person_internship pi on i.id = pi.internship_id\n" + 
							"WHERE pi.person_id = ?";
					PreparedStatement ps0 = con.prepareStatement(query0);
					ps0.setInt(1, userId);
					ResultSet rs0 = ps0.executeQuery();
					while(rs0.next()) {
						Subject userSubject = new Subject(rs0.getString("title"), rs0.getString("id"), rs0.getString("email"), rs0.getString("name"));
						request.setAttribute("userSubject", userSubject);
					}
					
					//show only programs associated to the user
					String query1 = "SELECT DISTINCT p.id as id, p.name as name, p.year as year\n" + 
							"FROM program p inner join person_program pp on p.id = pp.program_id\n" + 
							"WHERE pp.person_id = ?";
					PreparedStatement ps1 = con.prepareStatement(query1);
					ps1.setInt(1, userId);
					ResultSet rs1 = ps1.executeQuery();
					while(rs1.next()) {
						Program p = new Program(rs1.getString("id"), rs1.getString("name"), rs1.getString("year"));
						programs.add(p);
					}
					
					for(int i=0; i<programs.size(); ++i) {
						String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" + 
								"FROM categories c\n" + 
								"INNER JOIN program_category pc ON pc.cat_id = c.id\n" + 
								"WHERE pc.program_id ="+programs.get(i).getId()+";";
						ResultSet rs = con.prepareStatement(query).executeQuery();
						
						while(rs.next()) {
							String categoryId = rs.getString("id");
							
							String query_subjects = "SELECT i.id as id, i.title as title, p.email as email, p.name as name " + 
									"FROM internship i " + 
									"INNER JOIN internship_category ic ON i.id = ic.internship_id " + 
									"INNER JOIN categories c ON c.id = ic.category_id " + 
									"INNER JOIN person p on i.supervisor_id = p.id " + 
									"WHERE program_id ="+programs.get(i).getId()+" AND c.id = "+categoryId+" AND i.is_taken=false;";
							ResultSet rs_subjects = con.prepareStatement(query_subjects).executeQuery();
							List<Subject> subjectsOfCategory = new ArrayList<Subject>();
							while(rs_subjects.next()) {
								Subject s = new Subject(rs_subjects.getString("title"), rs_subjects.getString("id"), rs_subjects.getString("email"), rs_subjects.getString("name"));
								subjectsOfCategory.add(s);
							}
							subjectsPerCategory.add(new SubjectsPerCategory(programs.get(i).getId().toString(), categoryId, subjectsOfCategory));
							
							Category c = new Category(rs.getString("desc"), categoryId);
							programs.get(i).addCategory(c);
						}
					}
				} catch(SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.getInstance().releaseConnection(con);
				}
				//======================== END OF DATA LOADING PART ========================

				
				request.setAttribute("programs", programs);
				request.setAttribute("subjectsPerCategory", subjectsPerCategory);
				request.getRequestDispatcher("student_view.jsp").forward(request, response);
				
			}else {
				// the user is not admin, redirect to the error page
				session.setAttribute("errorMessage", "Please check your user role.");
				request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
			}
		}else {
			// the user is not logged in, redirect to the error page
			session.setAttribute("errorMessage", "Please log in first.");
			request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
		}
	}

}
