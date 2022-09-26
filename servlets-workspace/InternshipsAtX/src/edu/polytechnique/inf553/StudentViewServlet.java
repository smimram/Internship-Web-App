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
import java.util.HashMap;
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

                Topic userTopic = null;
                HashMap<Integer, ArrayList<Category>> topic2category = new HashMap<>();
                HashMap<Program, ArrayList<Topic>> topicsAvailableForTheStudentPerProgram = new HashMap<>();
                Defense studentDefense = null;
                int studentId = user.getId();

                //======================== DATA LOADING PART ========================
                Connection con = DbUtils.getConnection();
                try {
                    if (con == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    }

                    // check if the user already has an internship
                    String query = "select i.id as id, i.title as title, p.email as email, p.name as name, i.confidential_internship as confidential_internship\n" +
                            "FROM internship i\n" +
                            "INNER JOIN person p on i.supervisor_id = p.id\n" +
                            "INNER JOIN person_internship pi on i.id = pi.internship_id\n" +
                            "WHERE pi.person_id = ?";
                    try (PreparedStatement ps0 = con.prepareStatement(query)) {
                        ps0.setInt(1, studentId);
                        try (ResultSet rs0 = ps0.executeQuery()) {
                            while (rs0.next()) {
                                userTopic = new Topic(rs0.getString("title"), rs0.getInt("id"), rs0.getString("email"), rs0.getString("name"), rs0.getBoolean("confidential_internship"));
                            }
                        }
                    }

                    // get all the internships that the user can apply to
                    // i.e. those that are validated and no taken
                    // and that are in the program(s) of the student
                    // and regardless they have a category or not
                    ArrayList<Integer> topicsIds = new ArrayList<>();
                    query = "SELECT pr.id AS programId, pr.name AS programName, pr.year AS programYear, i.id AS topicId, i.title, i.confidential_internship, p.name AS supervisorName, p.email AS supervisorEmail " +
                            "FROM internship i, person p, person_program pp, program pr " +
                            "WHERE i.supervisor_id = p.id AND i.program_id = pp.program_id AND pp.person_id = ? AND pp.program_id = pr.id " +
                            "   AND i.scientific_validated = true " +
                            "   AND i.administr_validated = true " +
                            "   AND is_taken = false " +
                            "ORDER BY pp.program_id, i.id;";
                    try (PreparedStatement stmt2 = con.prepareStatement(query)) {
                        stmt2.setInt(1, studentId);
                        try (ResultSet rs = stmt2.executeQuery()) {
                            while (rs.next()) {
                                Program program = new Program(rs.getInt("programId"), rs.getString("programName"), rs.getString("programYear"));
                                if(!topicsAvailableForTheStudentPerProgram.containsKey(program)) {
                                    topicsAvailableForTheStudentPerProgram.put(program, new ArrayList<>());
                                }
                                Topic topic = new Topic(rs.getString("title"), rs.getInt("topicId"), rs.getString("supervisorEmail"), rs.getString("supervisorName"), rs.getBoolean("confidential_internship"));
                                topicsAvailableForTheStudentPerProgram.get(program).add(topic);
                                topicsIds.add(rs.getInt("topicId"));
                            }
                        }
                    }
                    System.out.println("topicsIds = " + topicsIds);

                    if(!topicsIds.isEmpty()) {
                        // get categories of each topic -- store only the topic ID and its categories
                        String topicsIdsString = "";
                        for (int topicId : topicsIds) {
                            topicsIdsString += topicId + ",";
                        }
                        topicsIdsString = topicsIdsString.substring(0, topicsIdsString.length() - 1); // remove last comma
                        query = "SELECT DISTINCT ic.internship_id AS topicId, c.id AS categoryId, c.description AS categoryDescr " +
                                "FROM categories c, internship_category ic " +
                                "WHERE ic.category_id = c.id AND ic.internship_id IN (" + topicsIdsString + ");";
                        try (PreparedStatement stmt = con.prepareStatement(query)) {
                            ResultSet rs = stmt.executeQuery();
                            while (rs.next()) {
                                if (!topic2category.containsKey(rs.getInt("topicId"))) {
                                    topic2category.put(rs.getInt("topicId"), new ArrayList<>());
                                }
                                topic2category.get(rs.getInt("topicId")).add(new Category(rs.getString("categoryDescr"), rs.getInt("categoryId")));
                            }
                        }
                    }
                    System.out.println("topic2category = " + topic2category);

                    // get the defense of the student
                    // p1 corresponds to the referent
                    // p2 corresponds to the jury2
                    query = "SELECT d.id, d.date, d.time, p1.id, p1.name, p1.email, p2.id, p2.name, p2.email, d.student_id " +
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

                boolean atLeastOneAvailableInternship = false;
                for(Map.Entry<Program, ArrayList<Topic>> entry : topicsAvailableForTheStudentPerProgram.entrySet()) {
                    if(!entry.getValue().isEmpty()) { // if at least one topic in one program, we set the boolean to true and stop
                        atLeastOneAvailableInternship = true;
                        break;
                    }
                }
                System.out.println("atLeastOneAvailableInternship = " + atLeastOneAvailableInternship);
                request.setAttribute("userTopic", userTopic);
                request.setAttribute("atLeastOneAvaialableTopic", atLeastOneAvailableInternship);
                request.setAttribute("topicsAvailableForTheStudentPerProgram", topicsAvailableForTheStudentPerProgram);
                request.setAttribute("programsAvailableForTheStudent", new ArrayList<>(topicsAvailableForTheStudentPerProgram.keySet()));
                request.setAttribute("topic2category", topic2category);
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
