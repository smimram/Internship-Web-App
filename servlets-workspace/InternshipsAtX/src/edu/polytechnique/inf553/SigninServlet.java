package edu.polytechnique.inf553;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SigninServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SigninServlet() {
        super();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
        List<Program> programs = getAllPrograms();
        request.setAttribute("programs", programs);
        request.getRequestDispatcher("signin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doPost method called with path " + request.getRequestURI());

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email").toLowerCase();
        String confirmEmail = request.getParameter("confirmEmail").toLowerCase();
        String pass = request.getParameter("pass");
        String confirmPass = request.getParameter("confirmPass");
        String role = request.getParameter("role");
        int programStudent = -1;
        if (request.getParameter("programStudent") != null && !Objects.equals(request.getParameter("programStudent"), "null")) {
            programStudent = Integer.parseInt(request.getParameter("programStudent"));
        }
        if(role.equals("Student")) {
            pass = "3AstudentDIX";
            confirmPass = "3AstudentDIX";
        }

        String errorMessage = checkEntries(fullName, email, confirmEmail, pass, confirmPass, role);
        if (errorMessage.equals("None") || errorMessage.equals("student upgrade")) {
            Connection con = DbUtils.getConnection();
            try {
                if (con == null) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }

                if(errorMessage.equals("student upgrade")) {
                    // let's upgrade the account from 'Proponent' to 'Student'
                    String upgradeToStudent = "update person_roles " +
                            "set role_id=rt.id " + // set the type to be the one for Student
                            "from role_type rt, person p " +
                            "where rt.role='Student' " +
                            "  and p.email=? " + // set the user email
                            "  and p.id=person_id;"; // and get only the person concerned
                    try (PreparedStatement ps3 = con.prepareStatement(upgradeToStudent)) {
                        ps3.setString(1, email);
                        ps3.executeUpdate();
                    }

                    // and update his/her password
                    String updatePassword = "update person set password = crypt(?, gen_salt('bf')) where email=?;";
                    try (PreparedStatement ps4 = con.prepareStatement(updatePassword)) {
                        ps4.setString(1, "3AstudentDIX");
                        ps4.setString(2, email);
                        ps4.executeUpdate();
                    }
                    role = "Student";
                } else {
                    //add the user into 'person' table
                    String query = "insert into person(name, email, creation_date, valid, password)\n" +
                            " values (?, ?, ?, false, crypt(?, gen_salt('bf'))) ;";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setString(1, fullName);
                        ps.setString(2, email);
                        ps.setDate(3, Date.valueOf(java.time.LocalDate.now()));
                        ps.setString(4, pass);
                        ps.executeUpdate();
                    }

                    //add a person_role relation into 'person_roles' table
                    String query2 = "insert into person_roles(role_id ,person_id)\n" +
                            "SELECT rt.id, p.id\n" +
                            "FROM role_type rt, person p\n" +
                            "WHERE rt.role = ? AND p.email = ?";
                    try (PreparedStatement ps2 = con.prepareStatement(query2)) {
                        ps2.setString(1, role);
                        ps2.setString(2, email);
                        ps2.executeUpdate();
                    }
                }

                if (role.equals("Student")) {
                    if (programStudent != -1) {
                        String query3 = "SELECT p.id FROM person p WHERE p.email = ?;";
                        PreparedStatement ps = con.prepareStatement(query3);
                        ps.setString(1, email);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        int idPerson = rs.getInt(1);

                        String query4 = "INSERT INTO person_program (program_id, person_id) VALUES (" + programStudent + " , " + idPerson + ")";
                        ps = con.prepareStatement(query4);
                        ps.executeUpdate();
                    } else {
                        request.setAttribute("fullName", fullName);
                        request.setAttribute("email", email);
                        request.setAttribute("confirmEmail", confirmEmail);
                        request.setAttribute("role", role);
                        request.setAttribute("err_message", "You have to choose a program.");
                        request.getRequestDispatcher("signin.jsp").forward(request, response);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtils.releaseConnection(con);
            }
            request.setAttribute("email", email);
            request.getRequestDispatcher("signin_complete.jsp").forward(request, response);
        } else {
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("confirmEmail", confirmEmail);
            request.setAttribute("role", role);
            request.setAttribute("err_message", errorMessage);
            request.getRequestDispatcher("signin.jsp").forward(request, response);
        }
    }

    private String checkEntries(String fullName, String email, String confirmEmail, String password, String confirmPassword, String role) {
        if (fullName == null || fullName.equals("")) {
            return "Please enter a full name.";
        } else if (email == null || email.equals("")) {
            return "Please enter an email.";
        } else if (confirmEmail == null || confirmEmail.equals("")) {
            return "Please confirm email.";
        } else if (!confirmEmail.equals(email)) {
            return "Emails don't match.";
        } else if (!role.equals("Student") && (password == null || password.equals(""))) {
            return "Please enter a password.";
        } else if (!role.equals("Student") && (confirmPassword == null || confirmPassword.equals(""))) {
            return "Please confirm password.";
        } else if (role == null) {
            return "Please choose a role.";
        } else if (!role.equals("Student") && password.length() < 8) {
            return "Password must have at least 8 characters.";
        } else if (!role.equals("Student") && !password.equals(confirmPassword)) {
            return "Passwords don't match.";
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
                boolean emailTaken;
                Connection con = DbUtils.getConnection();
                try {
                    if (con == null) {
                        return "failed connection to database!";
                    }
                    String query = "SELECT COUNT(*) as count\n" +
                            "FROM person \n" +
                            "WHERE email = ? ;";
                    try (PreparedStatement ps = con.prepareStatement(query)) {
                        ps.setString(1, email);
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            emailTaken = rs.getInt("count") > 0;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    emailTaken = true;
                } finally {
                    DbUtils.releaseConnection(con);
                }
                if (emailTaken) {
                    // NB Feb 21, 2024
                    // if this email is taken by a proponent, and the person tries to register as a sudent
                    // we chaneg the existing (proponent) account to be a student account
                    // mail Feb 13, 2024:
                    // the solution consists of changing the Web app code so that
                    //- when a Proponent tries to sign in as a Student
                    //- instead of rejecting them because the email is already taken
                    //- the person should get "upgraded" to become a student (and be able to interact with the app).
                    String checkIsProponent = "select p.* " +
                            "from person p " +
                            "  inner join person_roles pr on p.id=pr.person_id " +
                            "  inner join role_type rt on rt.id = pr.role_id" +
                            "where p.email=? and rt.role = 'Proponent';";
                    try (PreparedStatement ps2 = con.prepareStatement(checkIsProponent)) {
                        ps2.setString(1, email);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                // this person exists as a proponent in the database
                                // we now check whether he/she tries to create a student account
                                if(role.equals("Student")) {
                                    // the user tries to connect as a student
                                    return "student upgrade";
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DbUtils.getInstance().releaseConnection(con);
                    }
                    return "The email is already used.";
                } else {
                    return "None";
                }
            } else {
                return "Please enter a valid email.";
            }
        }
    }

    private List<Program> getAllPrograms() {
        Connection con = DbUtils.getConnection();
        try {
            if (con == null) {
                return null;
            }

            List<Program> programs = new ArrayList<>();
            // get all program list
            String query = "SELECT DISTINCT id, name, year, description FROM program ORDER BY name;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Program program = new Program(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("year"), resultSet.getString("description"));
                        programs.add(program);
                    }
                }
            }

            return programs;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DbUtils.releaseConnection(con);
        }
    }
}
