package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.nio.file.Paths;
import java.io.File;
import java.util.Arrays;
import java.io.FileInputStream;

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

            // CREATE TABLE exports (id SERIAL, date DATE, file BYTEA);
            String filePath = Paths.get(System.getProperty("user.dir"), "exportsInternshipsData", "exportInternshipData.csv").toString();
            File f = new File(filePath);
            f.getParentFile().mkdirs();
            String query = "COPY (" +
                    "  SELECT i.id AS internship_id, i.title AS internship_title, i.institution AS intersnhip_instituion, i.creation_date AS internship_creation_date, i.scientific_validated AS scientific_validation, i.administr_validated AS administrative_validation, p.name AS supervisor_name, p.email AS supervisor_email, CONCAT(pr.name, ' - ', pr.year) AS program, i.confidential_internship, p2.name AS student_name, p2.email AS student_email " +
                    "  FROM internship i " +
                    "  LEFT JOIN person p ON i.supervisor_id = p.id " +
                    "  LEFT JOIN program pr ON i.program_id = pr.id " +
                    "  LEFT JOIN person_internship pi ON pi.internship_id = i.id " +
                    "  LEFT JOIN person p2 ON pi.person_id = p2.id" +
                    ") " +
                    "TO '" + filePath + "' " +
                    "DELIMITER ',' CSV HEADER;";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.execute();

                FileInputStream fl = new FileInputStream(f);
                byte[] contentBytes = new byte[(int)f.length()];
                fl.read(contentBytes);
                fl.close();
                String encodedContent = Base64.getEncoder().encodeToString(contentBytes);
                request.setAttribute("encodedContent", encodedContent);
                returnFileName = "download_complete_export.jsp";
            }
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