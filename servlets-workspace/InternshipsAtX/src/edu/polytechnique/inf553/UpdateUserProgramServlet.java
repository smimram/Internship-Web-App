package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class UpdateUserProgramServlet
 */
@WebServlet("/UpdateUserProgramServlet")
public class UpdateUserProgramServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateUserProgramServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			if (role.equals("Admin")) {
				boolean add = Boolean.parseBoolean(request.getParameter("select"));
				int pid = Integer.parseInt(request.getParameter("pid"));
				int programId = Integer.parseInt(request.getParameter("programid"));
				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query;
					// update user program, set isolation level SERIALIZABLE
					if (add) {
						// add program
						query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
								"insert into person_program(program_id, person_id)\r\n" + 
								"values (?,?);\r\n" + 
								"COMMIT TRANSACTION;";
					}else {
						// delete program
						query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
								"DELETE FROM person_program\r\n" + 
								"  WHERE program_id = ? AND person_id = ?;\r\n" + 
								"COMMIT TRANSACTION;";
					}
					try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, programId);
            ps.setInt(2, pid);
            ps.executeUpdate();
          }
					
				} catch(SQLException e) {
					e.printStackTrace();
					// query errors
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				
				response.setStatus( 200 );
			}else {
				// the user is not admin, redirect to the error page
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}else {
			// the user is not logged in, redirect to the error page
			response.setStatus( HttpServletResponse.SC_FORBIDDEN );
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
