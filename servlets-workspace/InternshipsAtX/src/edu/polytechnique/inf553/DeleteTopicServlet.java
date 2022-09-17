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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class DeleteTopicServlet
 */
@WebServlet("/DeleteTopicServlet")
public class DeleteTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteTopicServlet() {
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
				int topicId = Integer.parseInt(request.getParameter("topicId"));
				if (checkIsTaken(topicId)) {
					session.setAttribute("description", "Cannot delete topic if it is already assigned to a student!");
					session.setAttribute("method", "doGet method of DeleteTopicServlet");
					session.setAttribute("userId", String.valueOf(user.getId()));
					request.getRequestDispatcher("/ErrorPageServlet").forward(request, response);
					//response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query = "DELETE FROM internship WHERE id = ?";
					try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, topicId);
            ps.executeUpdate();
          }

					
				} catch(SQLException e) {
					e.printStackTrace();
					// db error
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private boolean checkIsTaken(int topicId) {
		boolean taken = true;
		try (Connection con = DbUtils.getConnection()) {
			if (con == null) {
				return false;
			}
			
			// get all topic list
			String query = "SELECT is_taken "
					+ "FROM internship "
					+ "WHERE id=?;";
			try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
        preparedStatement.setInt(1, topicId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          if(resultSet.next()) {
            taken = resultSet.getBoolean("is_taken");
          }
        }
      }
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return taken;
	}

}
