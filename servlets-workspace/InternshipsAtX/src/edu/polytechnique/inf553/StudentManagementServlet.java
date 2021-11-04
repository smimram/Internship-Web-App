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

public class StudentManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public StudentManagementServlet() {
        super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			if (role.equals("Admin")) {
				List<Program> programs = new ArrayList<>();
				
				//======================== DATA LOADING PART ========================
				Connection con = null;
				try {
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}

					// get program list
					String query = "SELECT * " +
							"FROM program " +
							"ORDER BY year, name";
					PreparedStatement ps0 = con.prepareStatement(query);
					ResultSet rs0 = ps0.executeQuery();
					while(rs0.next()) {
						Program p = new Program(rs0.getInt(1), rs0.getString(2), rs0.getString(3));
						programs.add(p);
					}

					// get students for each program
					for(Program p : programs) {
						query = "SELECT p.name, p.id, r.role, p.valid, p.email " +
								"FROM person p " +
								"INNER JOIN person_roles pr ON pr.person_id = p.id " +
								"INNER JOIN role_type r ON r.id = pr.role_id " +
								"INNER JOIN person_program pp ON pp.person_id = p.id " +
								"WHERE pp.program_id = ?"+
								"ORDER BY p.name";
						ps0 = con.prepareStatement(query);
						ps0.setInt(1, Integer.parseInt(p.getId()));
						rs0 = ps0.executeQuery();
						while(rs0.next()) {
							Person student = new Person(rs0.getString(1), rs0.getInt(2), rs0.getString(3), rs0.getBoolean(4), rs0.getString(5));
							p.addStudent(student);
						}
					}
				} catch(SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.getInstance().releaseConnection(con);
				}
				
				request.setAttribute("programs", programs);
//				request.setAttribute("students", students);
				request.getRequestDispatcher("student_management.jsp").forward(request, response);
			}else {
				// the user is not admin, redirect to the error page
				request.setAttribute("errorMessage", "Please check your user role.");
				request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
			}
		}else {
			// the user is not logged in, redirect to the error page
			request.setAttribute("errorMessage", "Please log in first.");
			request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
