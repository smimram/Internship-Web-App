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
import java.util.Objects;

/**
 * Servlet implementation class UpdateUserInfo
 */
@WebServlet("/UpdateDefenseServlet")
public class UpdateDefenseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateDefenseServlet() {
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
        System.out.println(session);
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Admin")) {
                int defenseId = Integer.parseInt(request.getParameter("defenseId"));
                LocalTime defenseTime = null;
                LocalDate defenseDate = null;
                if (!Objects.equals(request.getParameter("defenseDate"), "NULL")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH); // TODO: set it to the current locale
                    defenseDate = LocalDate.parse(request.getParameter("defenseDate"), formatter);
                }
                if (!Objects.equals(request.getParameter("defenseTime"), "NULL")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH); // TODO: set it to the current locale
                    defenseTime = LocalTime.parse(request.getParameter("defenseTime"), formatter);
                }

                try (Connection con = DbUtils.getConnection()) {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }

                    // update user role, set isolation level SERIALIZABLE
                    String query = "UPDATE defense SET date = ?, time = ? WHERE id = ?";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        if (defenseDate == null) {
                            ps.setNull(1, Types.DATE);
                        } else {
                            ps.setDate(1, Date.valueOf(defenseDate));
                        }
                        if (defenseTime == null) {
                            ps.setNull(2, Types.TIME);
                        } else {
                            ps.setTime(2, Time.valueOf(defenseTime));
                        }
                        ps.setInt(3, defenseId);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                response.setStatus(200);
            } else {
                // the user is not admin, redirect to the error page
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            // the user is not logged in, redirect to the error page
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
