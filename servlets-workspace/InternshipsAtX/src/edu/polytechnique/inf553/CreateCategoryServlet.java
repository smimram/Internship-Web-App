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
 * Servlet implementation class CreateCategoryServlet
 */
@WebServlet("/CreateCategoryServlet")
public class CreateCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateCategoryServlet() {
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
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();

            if (role.equals("Admin") || role.equals("Professor")) {
                String name = request.getParameter("name");

                Connection con = DbUtils.getConnection();
                try {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }
                    String query = "insert into categories(description) values (?)";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setString(1, name);
                        ps.executeUpdate();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    // db error
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                } finally {
                    DbUtils.releaseConnection(con);
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

}
