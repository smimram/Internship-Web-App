package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@WebServlet("/CreateDefenseServlet")
public class CreateDefenseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CreateDefenseServlet() {
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
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH); // TODO: set it to the current locale
				LocalDate defenseDate = LocalDate.parse(request.getParameter("defenseDate"), formatter);
				formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH); // TODO: set it to the current locale
				LocalTime defenseTime = LocalTime.parse(request.getParameter("defenseTime"), formatter);
				int studentId = Integer.parseInt(request.getParameter("studentId"));
				int referentId = Integer.parseInt(request.getParameter("referentId"));
				int jury2Id = Integer.parseInt(request.getParameter("jury2Id"));

				Connection con = null;
				try {
					con = DbUtils.getInstance().getConnection();
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					String query = "START TRANSACTION ISOLATION LEVEL SERIALIZABLE;\r\n" + 
							"insert into defense(date, time, referent_id, jury2_id, student_id)\r\n" +
							"values (?,?,?,?,?);\r\n" +
							"COMMIT TRANSACTION;";;
					PreparedStatement ps = con.prepareStatement(query);
					ps.setDate(1, Date.valueOf(defenseDate));
					ps.setTime(2, Time.valueOf(defenseTime));
					ps.setInt(3, referentId);
					ps.setInt(4, jury2Id);
					ps.setInt(5, studentId);
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
