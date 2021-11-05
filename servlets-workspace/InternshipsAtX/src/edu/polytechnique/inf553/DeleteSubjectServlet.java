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
 * Servlet implementation class DeleteSubjectServlet
 */
@WebServlet("/DeleteSubjectServlet")
public class DeleteSubjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteSubjectServlet() {
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
				int subjectId = Integer.parseInt(request.getParameter("subjectId"));
				if (checkIsTaken(subjectId)) {
					session.setAttribute("description", "Cannot delete subject if it is already assigned to a student!");
					session.setAttribute("method", "doGet method of DeleteSubjectServlet");
					session.setAttribute("userId", String.valueOf(user.getId()));
					request.getRequestDispatcher("/ErrorPageServlet").forward(request, response);
					//response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				Connection con = null;
				try {
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
							"DELETE FROM internship\r\n" + 
							"WHERE id = ?;\r\n" + 
							"COMMIT TRANSACTION;";
					PreparedStatement ps = con.prepareStatement(query);
					ps.setInt(1, subjectId);
					ps.executeUpdate();

					
				} catch(SQLException e) {
					e.printStackTrace();
					// db error
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				} finally {
					DbUtils.getInstance().releaseConnection(con);
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
	
	private boolean checkIsTaken(int subjectId) {
		boolean taken = true;
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				return false;
			}
			
			// get all subject list
			String query = "SELECT is_taken "
					+ "FROM internship "
					+ "WHERE id=?;";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, subjectId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				taken = resultSet.getBoolean("is_taken");
			}

			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
		return taken;
	}

}
