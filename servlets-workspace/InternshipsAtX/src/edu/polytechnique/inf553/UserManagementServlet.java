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

public class UserManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public UserManagementServlet() {
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
				List<Person> persons = new ArrayList<Person>();
				List<Program> programs = new ArrayList<Program>();
				
				//======================== DATA LOADING PART ========================
				Connection con = null;
				try {
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					
					// get user list
					String query0 = "SELECT p.id as id, p.name as name, rt.role as role, p.valid as valid\n" + 
							"FROM person p inner join person_roles pr on p.id = pr.person_id\n" + 
							"inner join role_type rt on pr.role_id = rt.id\n" + 
							"WHERE rt.role != 'Proponent'\n" + 
							"ORDER BY p.id";
					PreparedStatement ps0 = con.prepareStatement(query0);
					ResultSet rs0 = ps0.executeQuery();
					while(rs0.next()) {
						Person p = new Person(rs0.getString("name"), rs0.getInt("id"), rs0.getString("role"), rs0.getBoolean("valid"));
						persons.add(p);
					}
					
					// get program list for each user
					for(int i = 0; i < persons.size(); i++) {
						String query1 = "SELECT DISTINCT program_id, name, year\n" + 
								"FROM program p inner join person_program pp on p.id = pp.program_id\n" + 
								"WHERE pp.person_id = ?";
						PreparedStatement ps1 = con.prepareStatement(query1);
						ps1.setInt(1, persons.get(i).getId());
						ResultSet rs1 = ps1.executeQuery();
						while(rs1.next()) {
							Program pr = new Program(rs1.getString("program_id"), rs1.getString("name"), rs1.getString("year"));
							persons.get(i).addProgram(pr);
						}
					}
					
					// get all program list
					String query2 = "SELECT DISTINCT id, name, year FROM program;";
					PreparedStatement ps2 = con.prepareStatement(query2);
					ResultSet rs2 = ps2.executeQuery();
					while(rs2.next()) {
						Program p = new Program(rs2.getString("id"), rs2.getString("name"), rs2.getString("year"));
						programs.add(p);
					}
					
				} catch(SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.getInstance().releaseConnection(con);
				}
				
				request.setAttribute("persons", persons);
				request.setAttribute("programs", programs);
				request.getRequestDispatcher("user_management.jsp").forward(request, response);
			}else {
				// the user is not admin, redirect to the error page
				request.setAttribute("errorMessage", "Please check your user role.");
				request.getRequestDispatcher("no_auser_managementccess_page.jsp").forward(request, response);
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
