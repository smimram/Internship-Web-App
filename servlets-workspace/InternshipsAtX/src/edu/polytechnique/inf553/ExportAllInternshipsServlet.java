package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.nio.file.Paths;
import java.io.File;
import java.util.Arrays;
import java.io.FileInputStream;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.text.*;

public class ExportAllInternshipsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int BUFFER_SIZE = 16177215;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());

        String returnFileName = "file_not_found.jsp";

        Connection con = DbUtils.getConnection();
        try {
            if (con == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

            String filePath = Paths.get("exportInternshipData.csv").toString();
            System.out.println("filePath = " + filePath);
            File f = new File(filePath);
            System.out.println("f = " + f);
//            f.getParentFile().mkdirs(); // leads to NullPointerException if not parent folder
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(filePath));

            String query = "" +
                "SELECT i.id AS internshipId, " +
                "  i.title AS internshipTitle, " +
                "  i.institution AS intersnhipInstitution, " +
                "  i.creation_date AS internshipCreationDate, " +
                "  i.scientific_validated AS scientificValidation, " +
                "  i.administr_validated AS administrativeValidation, " +
                "  p.name AS supervisorName, " +
                "  p.email AS supervisorEmail, " +
                "  CONCAT(pr.name, '-', pr.year) AS program, " +
                "  i.confidential_internship AS confidentialInternship, " +
                "  p2.name AS studentName, " +
                "  p2.email AS studentEmail " +
                "FROM internship i LEFT JOIN person p ON i.supervisor_id = p.id LEFT JOIN program pr ON i.program_id = pr.id " +
                "LEFT JOIN person_internship pi ON pi.internship_id = i.id " +
                "LEFT JOIN person p2 ON pi.person_id = p2.id";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                fos.write("internship id".getBytes());
                fos.write("\t".getBytes());
                fos.write("internship title".getBytes());
                fos.write("\t".getBytes());
                fos.write("internship institution".getBytes());
                fos.write("\t".getBytes());
                fos.write("internship creation date".getBytes());
                fos.write("\t".getBytes());
                fos.write("scientific validation".getBytes());
                fos.write("\t".getBytes());
                fos.write("administrative validation".getBytes());
                fos.write("\t".getBytes());
                fos.write("supervisor name".getBytes());
                fos.write("\t".getBytes());
                fos.write("supervisor email".getBytes());
                fos.write("\t".getBytes());
                fos.write("program".getBytes());
                fos.write("\t".getBytes());
                fos.write("confidential internship".getBytes());
                fos.write("\t".getBytes());
                fos.write("student name".getBytes());
                fos.write("\t".getBytes());
                fos.write("student email".getBytes());
                fos.write("\n".getBytes());
                while (rs.next()) {
                    // internshipId
                    int internshipId = rs.getInt(1);
                    if (!rs.wasNull()) {
                        fos.write(Integer.toString(internshipId).getBytes());
                    }
                    fos.write("\t".getBytes());
                    // internshipTitle
                    String internshipTitle = rs.getString(2);
                    if (!rs.wasNull()) {
                        fos.write(internshipTitle.getBytes());
                    }
                    fos.write("\t".getBytes());
                    // intersnhipInstitution
                    String internshipInstitution = rs.getString(3);
                    if (!rs.wasNull()) {
                        fos.write(internshipInstitution.getBytes());
                    }
                    fos.write("\t".getBytes());
                    // internshipCreationDate
                    Date internshipCreationDate = rs.getDate(4);
                    if (!rs.wasNull()) {
                        String dateAsString = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(internshipCreationDate);
                        fos.write(dateAsString.getBytes());
                    }
                    fos.write("\t".getBytes());
                    // scientificValidation
                    Boolean scientificValidation = rs.getBoolean(5);
                    if (!rs.wasNull()) {
                        fos.write(Boolean.toString(scientificValidation).getBytes());
                    }
                    fos.write("\t".getBytes());
                    // administrativeValidation
                    Boolean administrativeValidation = rs.getBoolean(6);
                    if (!rs.wasNull()) {
                        fos.write(Boolean.toString(administrativeValidation).getBytes());
                    }
                    fos.write("\t".getBytes());
                    // supervisorName
                    String supervisorName = rs.getString(7);
                    if (!rs.wasNull()) {
                        fos.write(rs.getString(7).getBytes());
                    }
                    fos.write("\t".getBytes());
                    // supervisorEmail
                    String supervisorEmail = rs.getString(8);
                    if (!rs.wasNull()) {
                        fos.write(supervisorEmail.getBytes());
                    }
                    fos.write("\t".getBytes());
                    // program
                    String program = rs.getString(9);
                    if (!rs.wasNull()) {
                        fos.write(program.getBytes());
                    }
                    fos.write("\t".getBytes());
                    // confidentialInternship
                    Boolean confidentialInternship = rs.getBoolean(10);
                    if (!rs.wasNull()) {
                        fos.write(Boolean.toString(confidentialInternship).getBytes());
                    }
                    fos.write("\t".getBytes());
                    // studentName
                    String studentName = rs.getString(11);
                    if (!rs.wasNull()) {
                        fos.write(studentName.getBytes());
                    }
                    fos.write("\t".getBytes());
                    // studentEmail
                    String studentEmail = rs.getString(12);
                    if (!rs.wasNull()) {
                        fos.write(studentEmail.getBytes());
                    }
                    fos.write("\n".getBytes());
                }
                fos.close();
            }
            // write the file as bytes (to be sent to the JSP)
            FileInputStream fl = new FileInputStream(f);
            byte[] contentBytes = new byte[(int) f.length()];
            fl.read(contentBytes);
            fl.close();
            String encodedContent = Base64.getEncoder().encodeToString(contentBytes);
            request.setAttribute("encodedContent", encodedContent);
            returnFileName = "download_complete_export.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.releaseConnection(con);
        }

        request.getRequestDispatcher(returnFileName).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}