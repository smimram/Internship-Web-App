package edu.polytechnique.inf553;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SubjectManagementServlet
 */
@WebServlet("/SubjectManagementServlet")
public class SubjectManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubjectManagementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			if (role.equals("Admin") || role.equals("Professor") || role.equals("Assistant")) {
				
				//======================== DATA LOADING PART ========================
				List<Subject> subjects = getSubjects();
				getCategoriesForSubjects(subjects);
				List<Program> programs = getAllPrograms();
				List<Category> categories = getAllCategories();
				List<Person> students = getStudents();
				
				request.setAttribute("students", students);
				request.setAttribute("programs", programs);
				request.setAttribute("categories", categories);
				request.setAttribute("subjects", subjects);
				request.getRequestDispatcher("subject_management.jsp").forward(request, response);
			}else {
				// the user is not admin or professor, redirect to the error page
				session.setAttribute("errorMessage", "Please check your user role.");
				request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
			}
		}else {
			// the user is not logged in, redirect to the error page
			session.setAttribute("errorMessage", "Please log in first.");
			request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private List<Subject> getSubjects() {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				return null;
			}
			
			List<Subject> subjects = new ArrayList<>();
			// get all subject list
			String query = "SELECT DISTINCT id, title, program_id, administr_validated, scientific_validated "
					+ "FROM internship "
					+ "ORDER BY id;";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Subject subject = new Subject(resultSet.getString("id"), resultSet.getString("title"), resultSet.getString("program_id"), 
						resultSet.getBoolean("administr_validated"), resultSet.getBoolean("scientific_validated"));
				subjects.add(subject);
			}
			
			return subjects;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
	}
	
	private List<Category> getAllCategories() {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				return null;
			}
			
			List<Category> categories = new ArrayList<>();
			//get all the categories
			String query = "SELECT * FROM categories ORDER BY id";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Category category = new Category(resultSet.getString("description"), resultSet.getString("id"));
				categories.add(category);
			}

			return categories;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.getInstance().releaseConnection(con);
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
			String query = "SELECT DISTINCT id, name, year FROM program;";
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
	
	private void getCategoriesForSubjects(List<Subject> subjects) {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				return;
			}
			
			// get associated categories for each subject
			for(int i=0; i<subjects.size(); ++i) {
				String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" + 
						"FROM categories c\n" + 
						"INNER JOIN internship_category ic ON ic.category_id = c.id\n" + 
						"WHERE ic.internship_id ="+subjects.get(i).getId()+"\n" + 
						"ORDER BY c.id;";
				ResultSet resultSet = con.prepareStatement(query).executeQuery();
				
				while(resultSet.next()) {
					Category category = new Category(resultSet.getString("desc"), resultSet.getString("id"));
					subjects.get(i).addCategory(category);
				}
			}

			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
	}
	
	private List<Person> getStudents() {
		Connection con = null;
		Person user = null;
		try {
			
			List<Person> students = new ArrayList<>();
			String query = "select name, role, person_id, valid "
					+ "from person p inner join person_roles pr on pr.person_id = p.id inner join role_type rt on rt.id = pr.role_id "
					+ "where rt.role = 'Student' AND valid IS TRUE;";
			//creating connection with the database
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				return null;
			}
			ResultSet resultSet = con.prepareStatement(query).executeQuery();
			
			while (resultSet.next()) {
				user = new Person(resultSet.getString("name"), resultSet.getInt("person_id"), resultSet.getString("role"), resultSet.getBoolean("valid"));
				students.add(user);
			}

			return students;

		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
	}

	
}
