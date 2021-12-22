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
			if (role.equals("Admin") || role.equals("Professor") || role.equals("Assistant")) {
				LocalDate defenseDate = null;
				if(!request.getParameter("defenseDate").equals("") && !request.getParameter("defenseDate").equals("null")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH); // TODO: set it to the current locale
					defenseDate = LocalDate.parse(request.getParameter("defenseDate"), formatter);
				}
				LocalTime defenseTime = null;
				if(!request.getParameter("defenseTime").equals("") && !request.getParameter("defenseTime").equals("null")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH); // TODO: set it to the current locale
					defenseTime = LocalTime.parse(request.getParameter("defenseTime"), formatter);
				}
				int studentId = -1;
				if(!request.getParameter("studentId").equals("") && !request.getParameter("studentId").equals("null")) {
					studentId = Integer.parseInt(request.getParameter("studentId"));
				}
				int referentId =-1;
				if(!request.getParameter("referentId").equals("") && !request.getParameter("referentId").equals("null")) {
					referentId = Integer.parseInt(request.getParameter("referentId"));
				}
				int jury2Id = -1;
				if(!request.getParameter("jury2Id").equals("") && !request.getParameter("jury2Id").equals("null")) {
					jury2Id = Integer.parseInt(request.getParameter("jury2Id"));
				}

				try (Connection con = DbUtils.getConnection()) {
					if (con == null) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}

					String query =
							"insert into defense(date, time, referent_id, jury2_id, student_id)\r\n" +
							"values (?,?,?,?,?)";

					try (PreparedStatement ps = con.prepareStatement(query)) {
            if(defenseDate == null) {
              ps.setNull(1, Types.DATE);
            } else {
              ps.setDate(1, Date.valueOf(defenseDate));
            }
            if(defenseTime == null) {
              ps.setNull(2,Types.TIME);
            } else {
              ps.setTime(2, Time.valueOf(defenseTime));
            }
            if(referentId == -1) {
              ps.setNull(3, Types.INTEGER);
            } else {
              ps.setInt(3, referentId);
            }
            if(jury2Id == -1) {
              ps.setNull(4, Types.INTEGER);
            } else {
              ps.setInt(4, jury2Id);
            }
            if(studentId == -1) {
              ps.setNull(5, Types.INTEGER);
            } else {
              ps.setInt(5, studentId);
            }
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
