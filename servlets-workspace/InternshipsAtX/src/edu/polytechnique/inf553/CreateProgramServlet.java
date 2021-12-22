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

@WebServlet("/CreateProgramServlet")
public class CreateProgramServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CreateProgramServlet() {
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
			if (role.equals("Admin") || role.equals("Professor" )) {
				String name = request.getParameter("name");
				int year = Integer.parseInt(request.getParameter("year"));
				Connection con = null;
				try {
					con = DbUtils.getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
							"insert into program(name, year)\r\n" + 
							"values (?,?);\r\n" + 
							"COMMIT TRANSACTION;";
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, name);
					ps.setInt(2, year);
					ps.executeUpdate();

					
				} catch(SQLException e) {
					e.printStackTrace();
					// db error
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				} finally {
					DbUtils.releaseConnection(con);
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
