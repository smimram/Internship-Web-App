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
import java.sql.Types;

/**
 * Servlet implementation class UpdateSubjectCategoryServlet
 */
@WebServlet("/UpdatePersonDefenseServlet")
public class UpdatePersonDefenseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePersonDefenseServlet() {
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
				// get ids
				int defenseId = Integer.parseInt(request.getParameter("defenseId"));
				int studentId = Integer.parseInt(this.getId("studentId", request));
				int referentId =  Integer.parseInt(this.getId("referentId", request));
				int jury2Id = Integer.parseInt(this.getId("jury2Id", request));
				System.out.println("defenseId="+defenseId+";studentId="+studentId+";referentId="+referentId+";jury2Id="+jury2Id);
				// update the database;
				if(referentId != -2) {
					setReferent(referentId, defenseId, response);
				}
				if(studentId != -2) {
					setStudent(studentId, defenseId, response);
				}
				if(jury2Id != -2) {
					setJury2(jury2Id, defenseId, response);
				}
			}else {
				// the user is not admin, redirect to the error page
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}else {
			// the user is not logged in, redirect to the error page
			response.setStatus( HttpServletResponse.SC_FORBIDDEN );
		}
	}

	private String getId(String idName, HttpServletRequest request) {
		if(request.getParameter(idName).equals("NULL")) {
			return String.valueOf("-1");
		} else if(request.getParameter(idName).equals("SAME")) {
			return String.valueOf(-2);
		} else {
			return request.getParameter(idName);
		}
	}

	private void setReferent(int referentId, int defenseId, HttpServletResponse response) throws IOException {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			// update defense persons, set isolation level SERIALIZABLE
			String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n"
					+ "UPDATE defense SET referent_id = ? "
					+ "where id = ?; "
					+ "COMMIT TRANSACTION;";
			PreparedStatement ps = con.prepareStatement(query);
			if (referentId == -1) { ps.setNull(1, Types.INTEGER); }
			else { ps.setInt(1, referentId); }
			ps.setInt(2, defenseId);
			ps.executeUpdate();
		} catch(SQLException | IOException e) {
			e.printStackTrace();
			// query errors
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
		response.setStatus( 200 );
	}

	private void setStudent(int studentId, int defenseId, HttpServletResponse response) throws IOException {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			// update defense persons, set isolation level SERIALIZABLE
			String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n"
					+ "UPDATE defense SET student_id = ? "
					+ "where id = ?; "
					+ "COMMIT TRANSACTION;";
			PreparedStatement ps = con.prepareStatement(query);
			if (studentId == -1) { ps.setNull(1, Types.INTEGER); }
			else { ps.setInt(1, studentId); }
			ps.setInt(2, defenseId);
			ps.executeUpdate();
		} catch(SQLException | IOException e) {
			e.printStackTrace();
			// query errors
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
		response.setStatus( 200 );
	}

	private void setJury2(int jury2Id, int defenseId, HttpServletResponse response) throws IOException {
		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			// update defense persons, set isolation level SERIALIZABLE
			String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n"
					+ "UPDATE defense SET jury2_id = ? "
					+ "where id = ?; "
					+ "COMMIT TRANSACTION;";
			PreparedStatement ps = con.prepareStatement(query);
			if (jury2Id == -1) { ps.setNull(1, Types.INTEGER); }
			else { ps.setInt(1, jury2Id); }
			ps.setInt(2, defenseId);
			ps.executeUpdate();
		} catch(SQLException | IOException e) {
			e.printStackTrace();
			// query errors
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
		response.setStatus( 200 );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
