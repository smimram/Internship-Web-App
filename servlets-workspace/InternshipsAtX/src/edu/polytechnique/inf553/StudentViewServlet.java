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
import java.util.ArrayList;
import java.util.List;

public class StudentViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
				
				List<Program> programs = new ArrayList<>();
				List<SubjectsPerCategory> subjectsPerCategory = new ArrayList<>();
				int userId = Integer.parseInt(user.getId());
				
				//======================== DATA LOADING PART ========================
				Connection con = null;
				try {
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					
					// check if the user already has an internship
					String query0 = "select i.id as id, i.title as title, p.email as email, p.name as name, i.confidential_internship as confidential_internship\n" +
							"FROM internship i\n" + 
							"INNER JOIN person p on i.supervisor_id = p.id\n" + 
							"INNER JOIN person_internship pi on i.id = pi.internship_id\n" + 
							"WHERE pi.person_id = ?";
					PreparedStatement ps0 = con.prepareStatement(query0);
					ps0.setInt(1, userId);
					ResultSet rs0 = ps0.executeQuery();
					while(rs0.next()) {
						Subject userSubject = new Subject(rs0.getString("title"), rs0.getInt("id"), rs0.getString("email"), rs0.getString("name"), rs0.getBoolean("confidential_internship"));
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
						Program p = new Program(rs1.getInt("id"), rs1.getString("name"), rs1.getString("year"));
						programs.add(p);
					}

					for (Program program : programs) {
						String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" +
								"FROM categories c\n" +
								"INNER JOIN program_category pc ON pc.cat_id = c.id\n" +
								"WHERE pc.program_id = ?;";
						PreparedStatement stmt = con.prepareStatement(query);
						stmt.setInt(1, Integer.parseInt(program.getId()));
						ResultSet rs = stmt.executeQuery();

						while (rs.next()) {
							int categoryId = rs.getInt("id");

							String query_subjects = "SELECT i.id as id, i.title as title, i.confidential_internship as confidential_internship, p.email as email, p.name as name " +
									"FROM internship i " +
									"INNER JOIN internship_category ic ON i.id = ic.internship_id " +
									"INNER JOIN categories c ON c.id = ic.category_id " +
									"INNER JOIN person p on i.supervisor_id = p.id " +
									"WHERE program_id = ? AND c.id = ? AND i.is_taken=false AND scientific_validated=true AND administr_validated=true;";
							PreparedStatement stmt2 = con.prepareStatement(query_subjects);
							stmt2.setInt(1, Integer.parseInt(program.getId()));
							stmt2.setInt(2, categoryId);
							ResultSet rs_subjects = stmt2.executeQuery();
							List<Subject> subjectsOfCategory = new ArrayList<>();
							while (rs_subjects.next()) {
								Subject s = new Subject(rs_subjects.getString("title"), rs_subjects.getInt("id"), rs_subjects.getString("email"), rs_subjects.getString("name"), rs_subjects.getBoolean("confidential_internship"));
								subjectsOfCategory.add(s);
							}
							subjectsPerCategory.add(new SubjectsPerCategory(Integer.parseInt(program.getId()), categoryId, subjectsOfCategory));

							Category c = new Category(rs.getString("desc"), categoryId);
							program.addCategory(c);
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
