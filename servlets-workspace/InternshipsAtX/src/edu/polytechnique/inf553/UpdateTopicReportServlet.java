package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class UpdateTopicSciValidServlet
 */
@WebServlet("/UpdateTopicReportServlet")
public class UpdateTopicReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTopicReportServlet() {
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
			if (role.equals("Admin") || role.equals("Professor")) {
				Part uploadFiche = request.getPart("uploadReport");
				int topicId = Integer.parseInt(request.getParameter("topicId"));
				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					
					// update user valid, set isolation level SERIALIZABLE
					String query = "UPDATE internship SET report = ? WHERE id = ?";
					try (PreparedStatement ps = con.prepareStatement(query)) {
            InputStream inputStream = uploadFiche.getInputStream();
            ps.setBinaryStream(1, inputStream);
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
