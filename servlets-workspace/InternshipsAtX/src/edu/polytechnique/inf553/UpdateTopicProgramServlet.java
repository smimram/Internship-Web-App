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
import java.util.HashSet;
import java.util.Set;

/**
 * Servlet implementation class UpdateTopicProgramServlet
 */
@WebServlet("/UpdateTopicProgramServlet")
public class UpdateTopicProgramServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTopicProgramServlet() {
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
				Connection con = null;
				int topicId = Integer.parseInt(request.getParameter("topicId"));
				int programId = Integer.parseInt(request.getParameter("programId"));
				
				// update the program
				try {
					con = DbUtils.getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}

					// update user valid, set isolation level SERIALIZABLE
					String query = "UPDATE internship SET program_id = ? WHERE id = ?;";
					PreparedStatement ps = con.prepareStatement(query);
					ps.setInt(1, programId);
					ps.setInt(2, topicId);
					ps.executeUpdate();
				} catch(SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.releaseConnection(con);
				}
				
				// if there are no categories in common between the program and the topic, reset the topic category
				
				Set<Integer> topicCategories = getCategoriesForTopic(topicId);
				Set<Integer> programCategories = getCategoriesForProgram(programId);
				topicCategories.retainAll(programCategories);
				if (topicCategories.isEmpty()) {
					try {
						con = DbUtils.getConnection();
						if (con == null) {
							response.sendError(HttpServletResponse.SC_FORBIDDEN);
						}

						String query = "DELETE FROM internship_category WHERE internship_id = ?;";
						PreparedStatement ps = con.prepareStatement(query);
						ps.setInt(1, topicId);
						ps.executeUpdate();
					} catch(SQLException e) {
						e.printStackTrace();
					} finally {
						DbUtils.releaseConnection(con);
					}
				}
				
				response.setStatus( 200 );
			} else {
				// the user is not admin, redirect to the error page
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} else {
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
	
	private Set<Integer> getCategoriesForTopic(int topicId) {
		try (Connection con = DbUtils.getConnection()) {
			if (con == null) {
				return null;
			}
			
			String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" + 
					"FROM categories c\n" + 
					"INNER JOIN internship_category ic ON ic.category_id = c.id\n" + 
					"WHERE ic.internship_id =?\n" + 
					"ORDER BY c.id;";
			Set<Integer> categories = new HashSet<>();
			try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
        preparedStatement.setInt(1, topicId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          while(resultSet.next()) {
            categories.add(resultSet.getInt("id"));
          }
        }
      }

			return categories;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Set<Integer> getCategoriesForProgram(int programId) {
		try (Connection con = DbUtils.getConnection()) {
			if (con == null) {
				return null;
			}
			String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" + 
					"FROM categories c\n" + 
					"INNER JOIN program_category pc ON pc.cat_id = c.id\n" + 
					"WHERE pc.program_id =?\n" + 
					"ORDER BY c.id;";
			Set<Integer> categories = new HashSet<>();
			try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
        preparedStatement.setInt(1, programId);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          while(resultSet.next()) {
            categories.add(resultSet.getInt("id"));
          }
        }
      }

			return categories;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
