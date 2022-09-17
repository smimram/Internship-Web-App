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
 * Servlet implementation class UpdateTopicAdminValidServlet
 */
@WebServlet("/UpdateTopicAdminValidServlet")
public class UpdateTopicAdminValidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTopicAdminValidServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		// session management
		HttpSession session = request.getSession(false);
		if(session!=null && session.getAttribute("user")!= null) {
			Person user = (Person)session.getAttribute("user");
			String role = user.getRole();
			if (role.equals("Admin") || role.equals("Assistant") || role.equals("Professor")) {
				Boolean valid = Boolean.parseBoolean(request.getParameter("valid"));
				int topicId = Integer.parseInt(request.getParameter("topicId"));
				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					
					// update user valid, set isolation level SERIALIZABLE
					String query = "UPDATE internship SET administr_validated = ? WHERE id = ?";
					try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setBoolean(1, valid);
            ps.setInt(2, topicId);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
