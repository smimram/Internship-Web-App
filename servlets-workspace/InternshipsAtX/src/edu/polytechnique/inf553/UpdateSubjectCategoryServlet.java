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
 * Servlet implementation class UpdateSubjectCategoryServlet
 */
@WebServlet("/UpdateSubjectCategoryServlet")
public class UpdateSubjectCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateSubjectCategoryServlet() {
        super();
        try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
			if (role.equals("Admin") || role.equals("Professor")  ) {
				int subjectId = Integer.parseInt(request.getParameter("subjectId"));
				int categoryId = Integer.parseInt(request.getParameter("categoryId"));
				boolean addCategory = Boolean.parseBoolean(request.getParameter("select"));
				Connection con = null;
				try {
					con = DbUtils.getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query = null;
					// update user program, set isolation level SERIALIZABLE
					if (addCategory) {
						// add program
						query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
								"insert into internship_category(internship_id, category_id)\r\n" + 
								"values (?,?);\r\n" + 
								"COMMIT TRANSACTION;";
					}else {
						// delete program
						query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
								"DELETE FROM internship_category\r\n" + 
								"  WHERE internship_id = ? AND category_id = ?;\r\n" + 
								"COMMIT TRANSACTION;";
					}
					PreparedStatement ps = con.prepareStatement(query);
					ps.setInt(1, subjectId);
					ps.setInt(2, categoryId);
					ps.executeUpdate();

					
				} catch(SQLException e) {
					e.printStackTrace();
					// query errors
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
