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

public class StudentViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public StudentViewServlet() {
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
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Student")) {

                List<Program> programs = new ArrayList<>();
                List<TopicsPerCategory> topicsPerCategory = new ArrayList<>();
                Defense studentDefense = null;
                int studentId = user.getId();

                //======================== DATA LOADING PART ========================
                Connection con = DbUtils.getConnection();
                try {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }

                    // check if the user already has an internship
                    String query0 = "select i.id as id, i.title as title, p.email as email, p.name as name, i.confidential_internship as confidential_internship\n" +
                            "FROM internship i\n" +
                            "INNER JOIN person p on i.supervisor_id = p.id\n" +
                            "INNER JOIN person_internship pi on i.id = pi.internship_id\n" +
                            "WHERE pi.person_id = ?";
                    try (PreparedStatement ps0 = con.prepareStatement(query0)) {
                        ps0.setInt(1, studentId);
                        try (ResultSet rs0 = ps0.executeQuery()) {
                            while (rs0.next()) {
                                Topic userTopic = new Topic(rs0.getString("title"),
                                        rs0.getInt("id"),
                                        rs0.getString("email"),
                                        rs0.getString("name"),
                                        rs0.getBoolean("confidential_internship"));
                                request.setAttribute("userTopic", userTopic);
                            }
                        }
                    }

                    //show only programs associated to the user
                    String query1 = "SELECT DISTINCT p.id as id, p.name as name, p.year as year\n" +
                            "FROM program p inner join person_program pp on p.id = pp.program_id\n" +
                            "WHERE pp.person_id = ?";
                    try (PreparedStatement ps1 = con.prepareStatement(query1)) {
                        ps1.setInt(1, studentId);
                        try (ResultSet rs1 = ps1.executeQuery()) {
                            while (rs1.next()) {
                                Program p = new Program(rs1.getInt("id"), rs1.getString("name"), rs1.getString("year"));
                                programs.add(p);
                            }
                        }
                    }

                    for (Program program : programs) {
                        String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" +
                                "FROM categories c\n" +
                                "INNER JOIN program_category pc ON pc.cat_id = c.id\n" +
                                "WHERE pc.program_id = ?;";
                        try (PreparedStatement stmt = con.prepareStatement(query)) {
                            stmt.setInt(1, Integer.parseInt(program.getId()));
                            try (ResultSet rs = stmt.executeQuery()) {
                                while (rs.next()) {
                                    int categoryId = rs.getInt("id");

                                    String queryTopics = "SELECT i.id as id, i.title as title, i.confidential_internship as confidential_internship, p.email as email, p.name as name " +
                                            "FROM internship i " +
                                            "INNER JOIN internship_category ic ON i.id = ic.internship_id " +
                                            "INNER JOIN categories c ON c.id = ic.category_id " +
                                            "INNER JOIN person p on i.supervisor_id = p.id " +
                                            "WHERE program_id = ? AND c.id = ? AND i.is_taken=false AND scientific_validated=true AND administr_validated=true;";
                                    try (PreparedStatement stmt2 = con.prepareStatement(queryTopics)) {
                                        stmt2.setInt(1, Integer.parseInt(program.getId()));
                                        stmt2.setInt(2, categoryId);
                                        try (ResultSet rsTopics = stmt2.executeQuery()) {
                                            List<Topic> topicsOfCategory = new ArrayList<>();
                                            while (rsTopics.next()) {
                                                Topic s = new Topic(rsTopics.getString("title"),
                                                        rsTopics.getInt("id"),
                                                        rsTopics.getString("email"),
                                                        rsTopics.getString("name"),
                                                        rsTopics.getBoolean("confidential_internship"));
                                                topicsOfCategory.add(s);
                                            }
                                            topicsPerCategory.add(new TopicsPerCategory(Integer.parseInt(program.getId()), categoryId, topicsOfCategory));
                                        }
                                    }
                                    Category c = new Category(rs.getString("desc"), categoryId);
                                    program.addCategory(c);
                                }
                            }
                        }
                    }

                    // get the defense of the student
                    // p1 corresponds to the referent
                    // p2 corresponds to the jury2
                    String query = "SELECT d.id, d.date, d.time, p1.id, p1.name, p1.email, p2.id, p2.name, p2.email, d.student_id " +
                            "FROM defense d, person p1, person p2 " +
                            "WHERE d.referent_id = p1.id AND d.jury2_id = p2.id AND student_id = ?;";
                    try(PreparedStatement stmt = con.prepareStatement(query)) {
                        stmt.setInt(1, studentId);
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next()) {
                            studentDefense = new Defense(rs.getInt(1), rs.getDate(2), rs.getTime(3), new Person(rs.getInt(4), rs.getString(5), rs.getString(6)), new Person(rs.getInt(7), rs.getString(8), rs.getString(9)), new Person(rs.getInt(10)));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    DbUtils.releaseConnection(con);
                }
                //======================== END OF DATA LOADING PART ========================


                request.setAttribute("programs", programs);
                request.setAttribute("topicsPerCategory", topicsPerCategory);
                request.setAttribute("studentDefense", studentDefense);
                request.getRequestDispatcher("student_view.jsp").forward(request, response);

            } else {
                // the user is not admin, redirect to the error page
                session.setAttribute("errorMessage", "Please check your user role.");
                request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
            }
        } else {
            // the user is not logged in, redirect to the error page
            session.setAttribute("errorMessage", "Please log in first.");
            request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
        }
    }

}
