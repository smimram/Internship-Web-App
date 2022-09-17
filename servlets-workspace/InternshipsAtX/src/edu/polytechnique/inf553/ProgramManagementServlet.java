package edu.polytechnique.inf553;

import javax.servlet.ServletException;
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
 * Servlet implementation class ProgramManagementServlet
 */
public class ProgramManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public ProgramManagementServlet() {
        super();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // session management
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Admin") || role.equals("Professor")) {
                List<Program> programs = new ArrayList<>();
                List<Category> categories = new ArrayList<>();


                //======================== DATA LOADING PART ========================
                try (Connection con = DbUtils.getConnection()) {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }

                    //get all the categories
                    String query0 = "SELECT * FROM categories ORDER BY id";
                    try (
                            PreparedStatement ps0 = con.prepareStatement(query0);
                            ResultSet rs0 = ps0.executeQuery();
                    ) {
                        while (rs0.next()) {
                            Category c = new Category(rs0.getString("description"), rs0.getInt("id"));
                            categories.add(c);
                        }
                    }

                    //get all the programs
                    String query1 = "SELECT DISTINCT id, name, year \n" +
                            "FROM program ORDER BY id";
                    try (
                            PreparedStatement ps1 = con.prepareStatement(query1);
                            ResultSet rs1 = ps1.executeQuery();
                    ) {
                        while (rs1.next()) {
                            Program p = new Program(rs1.getInt("id"), rs1.getString("name"), rs1.getString("year"));
                            programs.add(p);
                        }
                    }

                    // get associated categories for each program
                    for (Program program : programs) {
                        String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" +
                                "FROM categories c\n" +
                                "INNER JOIN program_category pc ON pc.cat_id = c.id\n" +
                                "WHERE pc.program_id = ? \n" +
                                "ORDER BY c.id;";
                        try (PreparedStatement stmt = con.prepareStatement(query)) {
                            stmt.setInt(1, Integer.parseInt(program.getId()));
                            try (ResultSet rs = stmt.executeQuery()) {
                                while (rs.next()) {
                                    Category c = new Category(rs.getString("desc"), rs.getInt("id"));
                                    program.addCategory(c);
                                }
                            }
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //======================== END OF DATA LOADING PART ========================

                request.setAttribute("programs", programs);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("program_management.jsp").forward(request, response);
            } else {
                // the user is not admin, redirect to the error page
                request.setAttribute("errorMessage", "Please check your user role.");
                request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
            }
        } else {
            // the user is not logged in, redirect to the error page
            request.setAttribute("errorMessage", "Please log in first.");
            request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
