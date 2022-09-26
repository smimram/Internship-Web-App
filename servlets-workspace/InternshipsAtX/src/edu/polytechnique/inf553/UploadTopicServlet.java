package edu.polytechnique.inf553;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class UploadTopicServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of UploadTopicServlet
     * Query the programs and their categories
     */
    public UploadTopicServlet() {
        super();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI());

        List<Program> programs = loadData();
        request.setAttribute("programs", programs);
        request.getRequestDispatcher("upload_topic.jsp").include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost called with parameter " + request.getQueryString());

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email").toLowerCase();
        String topicTitle = request.getParameter("topicTitle");
        String institution = request.getParameter("institution");
        String programIdString = request.getParameter("programs");
        String categoryIdString = request.getParameter("categories");
        String confidentiality = request.getParameter("confidentiality");
        Part uploadFile = request.getPart("uploadFile");

        String errorMessage = checkEntries(fullName, email, topicTitle, institution, programIdString, categoryIdString, uploadFile);
        if (errorMessage.equals("None")) {

            int programId = -1;
            int categoryId = -1;
            if(programIdString != null && !programIdString.equals("")) {
                programId = Integer.parseInt(programIdString);
            }
            if(categoryIdString != null && !categoryIdString.equals("")) {
                categoryId = Integer.parseInt(categoryIdString);
            }
            boolean confidentialTopic = Objects.equals(confidentiality, "on"); // the checkbox is checked
            Connection con = DbUtils.getConnection();
            try {
                int supervisorId;

                if (con == null) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }

                if (!checkEmail(email)) {
                    String defaultPass = "password";
                    String query1 = "insert into person(name, email, creation_date, valid, password)" +
                            " values (?, ?, ?, true, crypt(?, gen_salt('bf'))) ;";

                    try (PreparedStatement ps1 = con.prepareStatement(query1)) {
                        ps1.setString(1, fullName);
                        ps1.setString(2, email);
                        ps1.setDate(3, Date.valueOf(java.time.LocalDate.now()));
                        ps1.setString(4, defaultPass);
                        ps1.executeUpdate();
                    }

                    String query2 = "select id from person where email =? ;";
                    try (PreparedStatement ps2 = con.prepareStatement(query2)) {
                        ps2.setString(1, email);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            rs2.next();
                            supervisorId = rs2.getInt("id");
                        }
                    }

                    String query3 = "insert into person_roles(role_id, person_id) values (4, ?);"; //'proponent' id=4
                    try (PreparedStatement ps3 = con.prepareStatement(query3)) {
                        ps3.setInt(1, supervisorId);
                        ps3.executeUpdate();
                    }
                }

                String query4 = "select id from person where email =? ;";
                try (PreparedStatement ps4 = con.prepareStatement(query4)) {
                    ps4.setString(1, email);
                    try (ResultSet rs4 = ps4.executeQuery()) {
                        rs4.next();
                        supervisorId = rs4.getInt("id");
                    }
                }

                String query5 = "insert into internship(title, creation_date, content, institution, supervisor_id, scientific_validated, administr_validated, is_taken, program_id, confidential_internship)" +
                        " values (?, ?, ?, ?, ?, false, false, false, ?, ?) ;";

                InputStream inputStream = uploadFile.getInputStream();

                if (inputStream != null) {
                    // insert the topic
                    try (PreparedStatement ps5 = con.prepareStatement(query5)) {
                        ps5.setString(1, topicTitle);
                        ps5.setDate(2, Date.valueOf(java.time.LocalDate.now()));
                        ps5.setBinaryStream(3, inputStream);
                        ps5.setString(4, institution);
                        ps5.setInt(5, supervisorId);
                        if(programId == -1) {
//                            ps5.setNull(6, Types.INTEGER);
                            ps5.setInt(6, -1);
                        } else {
                            ps5.setInt(6, programId);
                        }
                        ps5.setBoolean(7, confidentialTopic);
                        int row = ps5.executeUpdate();
                        if (row <= 0) {
                            System.out.println("ERROR: Topic was not uploaded and saved into database");
                        }
                    }

                    // insert the topic category
                    if(categoryId != -1) {
                        String query6 = "select id from internship where title =? ;";
                        try (PreparedStatement ps6 = con.prepareStatement(query6)) {
                            ps6.setString(1, topicTitle);
                            try (ResultSet rs6 = ps6.executeQuery()) {
                                rs6.next();
                                int internshipId = rs6.getInt("id");

                                String query7 = "insert into internship_category(internship_id, category_id)" +
                                        " values (?, ?) ;";
                                try (PreparedStatement ps7 = con.prepareStatement(query7)) {
                                    ps7.setInt(1, internshipId);
                                    ps7.setInt(2, categoryId);
                                    ps7.executeUpdate();
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtils.releaseConnection(con);
            }
            request.setAttribute("topicTitle", topicTitle);
            request.getRequestDispatcher("upload_complete.jsp").forward(request, response);
        } else {
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("topicTitle", topicTitle);
            request.setAttribute("institution", institution);
            request.setAttribute("err_message", errorMessage);
            List<Program> programs = loadData();
            request.setAttribute("programs", programs);
            request.getRequestDispatcher("upload_topic.jsp").forward(request, response);
        }
    }

    private List<Program> loadData() {
        List<Program> programs = new ArrayList<>();
        Connection con = DbUtils.getConnection();
        try {
            if (con == null) {
                return null;
            }

            String query1 = "SELECT DISTINCT id, name, year FROM program;";
            try (PreparedStatement ps1 = con.prepareStatement(query1); ResultSet rs1 = ps1.executeQuery();) {
                while (rs1.next()) {
                    Program p = new Program(rs1.getInt("id"), rs1.getString("name"), rs1.getString("year"));
                    programs.add(p);
                }
            }

            for (Program program : programs) {
                String query = "SELECT DISTINCT c.description AS desc, c.id as id \n" +
                        "FROM categories c\n" +
                        "INNER JOIN program_category pc ON pc.cat_id = c.id\n" +
                        "WHERE pc.program_id = ? ;";
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
        } finally {
            DbUtils.releaseConnection(con);
        }
        return programs;
    }

    private String checkEntries(String fullName, String email, String topicTitle, String institution, String program_id_string, String category_id_string, Part uploadFile) {
        if (fullName == null || fullName.equals("")) {
            return "Please enter a full name.";
        } else if (email == null || email.equals("")) {
            return "Please enter an email.";
        } else if (topicTitle == null || topicTitle.equals("")) {
            return "Please choose a topic title.";
        } else if (checkTitle(topicTitle)) {
            return "The topic title is already taken.";
        } else if(institution == null || institution.equals("")) {
            return "Please enter your institution.";
//        } else if (program_id_string == null || program_id_string.equals("0")) {
//            return "Please choose a program.";
//        } else if (category_id_string == null || category_id_string.equals("0") || category_id_string.equals("-1")) {
//            return "Please choose a category of your program.";
        } else {
            boolean emailIsValid = true;
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
            } catch (AddressException e) {
                e.printStackTrace();
                emailIsValid = false;
            }
            if (emailIsValid) {
                if (uploadFile.getSize() == 0) { // DOESN'T WORK
                    return "Please submit your internship description as a PDF file. (16Mo max)";
                }
                return "None";
            } else {
                return "Please enter a valid email.";
            }
        }
    }

    private boolean checkEmail(String email) {
        boolean st = false;
        Connection con = DbUtils.getConnection();
        try {
            if (con == null) {
                return false;
            }

            String query = "select * from person where email=?;";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    st = rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.releaseConnection(con);
        }
        return st;
    }

    private boolean checkTitle(String topicTitle) {
        boolean st = false;
        Connection con = DbUtils.getConnection();
        try {
            if (con == null) {
                return false;
            }

            String query = "select * from internship where title=?;";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, topicTitle);
                try (ResultSet rs = ps.executeQuery()) {
                    st = rs.next();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.releaseConnection(con);
        }
        return st;
    }
}
