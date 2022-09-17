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
 * Servlet implementation class TopicAttributionServlet
 */
@WebServlet("/TopicAttributionServlet")
public class TopicAttributionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicAttributionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // session management
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Person user = (Person) session.getAttribute("user");
            String role = user.getRole();
            if (role.equals("Admin") || role.equals("Professor") || role.equals("Assistant")) {

                //======================== DATA LOADING PART ========================
                List<Topic> topics = getTopics();
                List<Person> students = getStudents();

                request.setAttribute("students", students);
                request.setAttribute("topics", topics);
                request.getRequestDispatcher("topic_attribution.jsp").forward(request, response);
            } else {
                // the user is not professor, redirect to the error page
                session.setAttribute("errorMessage", "Please check your user role.");
                request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
            }
        } else {
            // the user is not logged in, redirect to the error page
            session.setAttribute("errorMessage", "Please log in first.");
            request.getRequestDispatcher("no_access_page.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    private List<Topic> getTopics() {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return null;
            }

            List<Topic> topics = new ArrayList<>();
            // get all topic list
            String query = "SELECT DISTINCT id, title, program_id, administr_validated, scientific_validated, confidential_internship, timestamp_fiche, timestamp_report, timestamp_slides "
                    + "FROM internship "
                    + "WHERE is_taken IS FALSE AND administr_validated IS TRUE AND scientific_validated IS TRUE;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Topic topic = new Topic(resultSet.getInt("id"),
                                resultSet.getString("title"),
                                resultSet.getInt("program_id"),
                                resultSet.getBoolean("administr_validated"),
                                resultSet.getBoolean("scientific_validated"),
                                resultSet.getBoolean("confidential_internship"));
                        topics.add(topic);
                    }
                }
            }

            return topics;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Person> getStudents() {
        Person user = null;
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return null;
            }

            List<Person> students = new ArrayList<>();
            String query = "select name, role, person_id, valid, email "
                    + "from person p inner join person_roles pr on pr.person_id = p.id inner join role_type rt on rt.id = pr.role_id "
                    + "where rt.role = 'Student' AND valid IS TRUE;";

            try (
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet resultSet = stmt.executeQuery();
            ) {
                while (resultSet.next()) {
                    user = new Person(resultSet.getString("name"),
                            resultSet.getInt("person_id"),
                            resultSet.getString("role"),
                            resultSet.getBoolean("valid"),
                            resultSet.getString("email"));
                    students.add(user);
                }
            }

            return students;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
