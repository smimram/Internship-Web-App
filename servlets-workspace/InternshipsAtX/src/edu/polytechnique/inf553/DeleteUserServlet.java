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
 * Servlet implementation class DeleteUserServlet
 */
@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserServlet() {
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
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Admin") || role.equals("Assistant") || role.equals("Professor")) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                if (isStillActive(userId)) {
                    session.setAttribute("description", "Failed to delete user.");
                    session.setAttribute("method", "doGet method of DeleteUserServlet");
                    session.setAttribute("userId", String.valueOf(user.getId()));
                    request.getRequestDispatcher("/ErrorPageServlet").forward(request, response);
                }
                try (Connection con = DbUtils.getInstance().getConnection()) {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                    // delete the user
                    String query = "DELETE FROM person WHERE id = ?";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setInt(1, userId);
                        ps.executeUpdate();
                    }
                    // delete its assignement to the internship
                    query = "DELETE FROM person_internship WHERE person_id = ?";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setInt(1, userId);
                        ps.executeUpdate();
                    }
                    // delete its assignement to the programs
                    query = "DELETE FROM person_program WHERE person_id = ?";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setInt(1, userId);
                        ps.executeUpdate();
                    }
                    // delete its assignement to the defense
                    query = "DELETE FROM defense WHERE student_id = ? OR referent_id = ? OR jury2_id = ?";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setInt(1, userId);
                        ps.setInt(2, userId);
                        ps.setInt(3, userId);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // db error
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
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

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    private boolean isStillActive(int userId) {
        boolean stillActive = true;
        ArrayList<Integer> inactivePersons = new ArrayList<>();
        try (Connection con = DbUtils.getInstance().getConnection()) {
            if (con == null) {
                return false;
            }

            // get all inactive persons
            String query = "SELECT id FROM person WHERE " +
                    "id NOT IN (SELECT DISTINCT person_id FROM person_internship) " +
                    "AND id NOT IN (SELECT DISTINCT person_id FROM person_program);";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        inactivePersons.add(resultSet.getInt(1));
                    }
                    stillActive = !inactivePersons.contains(userId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stillActive;
    }
}
