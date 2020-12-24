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
		
		
		List<Program> programs = new ArrayList<Program>();
		List<SubjectsPerCategory> subjectsPerCategory = new ArrayList<SubjectsPerCategory>();
		
		
		//======================== DATA LOADING PART ========================
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
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		//======================== END OF DATA LOADING PART ========================

		
		request.setAttribute("programs", programs);
		request.setAttribute("subjectsPerCategory", subjectsPerCategory);
		request.getRequestDispatcher("student_view.jsp").forward(request, response);
	}

}
