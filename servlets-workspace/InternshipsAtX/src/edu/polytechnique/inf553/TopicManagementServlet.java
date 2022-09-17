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
import java.util.HashMap;
import java.util.List;

/**
 * Servlet implementation class TopicManagementServlet
 */
@WebServlet("/TopicManagementServlet")
public class TopicManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicManagementServlet() {
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
            if (role.equals("Admin") || role.equals("Professor") || role.equals("Assistant")) {

                //======================== DATA LOADING PART ========================
                String orderByColumn = request.getParameter("orderByColumn");
                String orderBySort = request.getParameter("orderBySort");
                System.out.println("orderByColumn=" + orderByColumn + " ; orderBySort=" + orderBySort);
                List<Topic> topics = getTopics(orderByColumn, orderBySort);
                getCategoriesForTopics(topics);
                getAffiliatedStudentsForTopics(topics);
                List<Program> programs = getAllPrograms();
                HashMap<String, ArrayList<Category>> categoriesForPrograms = getAllCategories(programs);
                List<Person> students = getStudents();
                List<Person> studentsWithoutInternship = getStudentsWithoutInternship();

                request.setAttribute("students", students);
                request.setAttribute("studentsNoInternship", studentsWithoutInternship);
                request.setAttribute("programs", programs);
                request.setAttribute("categoriesForPrograms", categoriesForPrograms);
                request.setAttribute("topics", topics);
                System.out.println(topics);
                request.getRequestDispatcher("topic_management.jsp").forward(request, response);
            } else {
                // the user is not admin or professor, redirect to the error page
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

    private List<Topic> getTopics(String orderByColumn, String orderBySort) {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return null;
            }

            List<Topic> topics = new ArrayList<>();
            // get all topic list
            System.out.println("getTopics");
            if (orderByColumn == null) orderByColumn = "id"; // if no parameter is provided
            if (orderBySort == null) orderBySort = "ASC";
            if (orderByColumn.startsWith("'") && orderByColumn.endsWith("'"))
                orderByColumn = orderByColumn.substring(1, orderByColumn.length() - 1); // if the value is encapsulated into '', e.g. 'id'
            if (orderBySort.startsWith("'") && orderBySort.endsWith("'"))
                orderBySort = orderBySort.substring(1, orderBySort.length() - 1); // if the value is encapsulated into '', e.g. 'ASC'
            String query = "SELECT DISTINCT id, title, program_id, administr_validated, scientific_validated, confidential_internship, timestamp_fiche, timestamp_report, timestamp_slides "
                    + "FROM internship "
                    + "ORDER BY " + orderByColumn + " " + orderBySort + ";";
            try (
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Topic topic = new Topic(resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getInt("program_id"),
                            resultSet.getBoolean("administr_validated"),
                            resultSet.getBoolean("scientific_validated"),
                            resultSet.getBoolean("confidential_internship"));
                    topic.setDateFiche(resultSet.getTimestamp("timestamp_fiche"));
                    topic.setDateReport(resultSet.getTimestamp("timestamp_report"));
                    topic.setDateSlides(resultSet.getTimestamp("timestamp_slides"));
                    topics.add(topic);
                }
            }
            return topics;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String, ArrayList<Category>> getAllCategories(List<Program> programs) {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return null;
            }

            HashMap<String, ArrayList<Category>> categoryForEachProgram = new HashMap<>();
            //get all the categories in each program
            for (Program p : programs) {
                String query = "SELECT c.* FROM program_category pc, categories c WHERE pc.cat_id = c.id AND pc.program_id = ? ORDER BY description";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(p.getId()));
                ResultSet resultSet = preparedStatement.executeQuery();
                ArrayList<Category> categoriesInProgram = new ArrayList<>();
                while (resultSet.next()) {
                    Category category = new Category(resultSet.getString("description"), resultSet.getInt("id"));
                    categoriesInProgram.add(category);
                }
                categoryForEachProgram.put(p.getId(), categoriesInProgram);

            }

            return categoryForEachProgram;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Program> getAllPrograms() {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return null;
            }

            List<Program> programs = new ArrayList<>();
            // get all program list
            String query = "SELECT DISTINCT id, name, year FROM program ORDER BY name;";
            try (
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Program program = new Program(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("year"));
                    programs.add(program);
                }
            }

            return programs;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getCategoriesForTopics(List<Topic> topics) {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return;
            }

            // get associated categories for each topic
            for (Topic topic : topics) {
                String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" +
                        "FROM categories c\n" +
                        "INNER JOIN internship_category ic ON ic.category_id = c.id\n" +
                        "WHERE ic.internship_id = ? \n" +
                        "ORDER BY c.description;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, Integer.parseInt(topic.getId()));
                    try (ResultSet resultSet = stmt.executeQuery()) {
                        while (resultSet.next()) {
                            Category category = new Category(resultSet.getString("desc"), resultSet.getInt("id"));
                            topic.addCategory(category);
                        }
                    }
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAffiliatedStudentsForTopics(List<Topic> topics) {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return;
            }

            // get associated categories for each topic
            for (Topic topic : topics) {
                String query = "SELECT p.id AS pid, p.name AS pName, r.role AS role, p.valid AS pValid, p.email AS email " +
                        "FROM person p " +
                        "INNER JOIN person_internship pi ON pi.person_id = p.id " +
                        "LEFT JOIN person_roles pr ON pr.person_id = p.id " +
                        "LEFT JOIN role_type r ON pr.role_id = r.id " +
                        "WHERE pi.internship_id = ? ;";
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, Integer.parseInt(topic.getId()));
                    try (ResultSet resultSet = stmt.executeQuery()) {
                        while (resultSet.next()) {
                            Person person = new Person(resultSet.getString("pName"),
                                    resultSet.getInt("pid"),
                                    resultSet.getString("role"),
                                    resultSet.getBoolean("pValid"),
                                    resultSet.getString("email"));
                            topic.setAffiliatedStudent(person);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Person> getStudents() {
        Person user;
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

    private List<Person> getStudentsWithoutInternship() {
        try (Connection con = DbUtils.getConnection()) {
            if (con == null) {
                return null;
            }

            List<Person> students = new ArrayList<>();
            String query = "select p.name AS name, p.id AS person_id, rt.role AS role, p.valid AS valid, p.email AS email "
                    + "from person p "
                    + "inner join person_roles pr on pr.person_id = p.id "
                    + "inner join role_type rt on rt.id = pr.role_id "
                    + "where rt.role = 'Student' AND p.valid IS TRUE AND p.id NOT IN (SELECT pi.person_id FROM person_internship pi) " +
                    "ORDER BY  name;";

            try (
                    PreparedStatement stmt = con.prepareStatement(query);
                    ResultSet resultSet = stmt.executeQuery();
            ) {
                while (resultSet.next()) {
                    Person user = new Person(resultSet.getString("name"),
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
