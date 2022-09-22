package edu.polytechnique.inf553;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ErrorPageServlet
 */
@WebServlet("/ErrorPageServlet")
public class ErrorPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());

        HttpSession session = request.getSession(false);
        if (session != null) {
            String method = (String) session.getAttribute("method");
            String decription = (String) session.getAttribute("description");
            int user = Integer.parseInt((String) session.getAttribute("userId"));
            Connection con = DbUtils.getConnection();
            try {
                if (con == null) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
                // update user program, set isolation level SERIALIZABLE
                String query =
                        "insert into error (date, time, method_raised, description, person_id)\r\n" +
                                "values ((select current_date), (select current_timestamp), ?,?, ?)";

                try (PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setString(1, method);
                    ps.setString(2, decription);
                    ps.setInt(3, user);
                    ps.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                // query errors
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } finally {
                DbUtils.releaseConnection(con);
            }
            response.setStatus(200);
        } else {
            response.setStatus(200);
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
