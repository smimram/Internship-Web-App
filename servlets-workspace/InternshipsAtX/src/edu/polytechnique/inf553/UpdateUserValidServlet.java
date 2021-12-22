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
 * Servlet implementation class UpdateUserValidServlet
 */
@WebServlet("/UpdateUserValidServlet")
public class UpdateUserValidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateUserValidServlet() {
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
				boolean valid = Boolean.parseBoolean(request.getParameter("valid"));
				int pid = Integer.parseInt(request.getParameter("pid"));
				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					
					// update user valid, set isolation level SERIALIZABLE
					String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
							"UPDATE person SET valid = ?\r\n" + 
							"WHERE id = ?;\r\n" + 
							"COMMIT TRANSACTION;";
					try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setBoolean(1, valid);
            ps.setInt(2, pid);
            ps.executeUpdate();
          }
					
				} catch(SQLException e) {
					e.printStackTrace();
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
