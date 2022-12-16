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

public class ExportAllInternshipsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int BUFFER_SIZE = 16177215;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());

        String returnFileName = "file_not_found.jsp";

        Connection con = DbUtils.getConnection();
//      try {
        if (con == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        String filePath = Paths.get("/tmp", "exportsInternshipsData", "exportInternshipData.csv").toString();
        File f = new File(filePath);
        f.getParentFile().mkdirs();

        // write the csv under /tmp
//        CopyManager copyManager = new CopyManager((BaseConnection) con); // temporary copy manager
//        String query = "SELECT i.id AS internshipId, i.title AS internshipTitle, i.institution AS intersnhipInstitution, i.creation_date AS internshipCreationDate, i.scientific_validated AS scientificValidation, i.administr_validated AS administrativeValidation, p.name AS supervisorName, p.email AS supervisorEmail, CONCAT(pr.name, pr.year) AS program, i.confidential_internship, p2.name AS studentName, p2.email AS studentEmail FROM internship i LEFT JOIN person p ON i.supervisor_id = p.id LEFT JOIN program pr ON i.program_id = pr.id LEFT JOIN person_internship pi ON pi.internship_id = i.id LEFT JOIN person p2 ON pi.person_id = p2.id";
//        copyManager.copyOut("COPY ( " + query + " ) TO STDOUT WITH CSV HEADER DELIMITER ';'", new FileOutputStream(f));

        try {
            String query = "\\copy ( " +
                    "SELECT i.id AS internshipId, i.title AS internshipTitle, i.institution AS intersnhipInstituion, i.creation_date AS internshipCreationDate, i.scientific_validated AS scientificValidation, i.administr_validated AS administrativeValidation, p.name AS supervisorName, p.email AS supervisorEmail, CONCAT(pr.name, pr.year) AS program, i.confidential_internship, p2.name AS studentName, p2.email AS studentEmail " +
                    "FROM internship i LEFT JOIN person p ON i.supervisor_id = p.id LEFT JOIN program pr ON i.program_id = pr.id " +
                    "LEFT JOIN person_internship pi ON pi.internship_id = i.id " +
                    "LEFT JOIN person p2 ON pi.person_id = p2.id" +
                    ") TO " + filePath + ";";
            System.out.println(query);
            ProcessBuilder builderLoad = new ProcessBuilder("psql", "-d", "InternshipsDB", "-c", query);
            System.out.println(builderLoad.command());
            Process processLoad = builderLoad.inheritIO().start(); // builderLoad.start()
            processLoad.waitFor();
            if (processLoad.exitValue() != 0) {
                throw new IllegalStateException("Something went wrong while exporting the CSV file.");
            }

            // write the file as bytes (to be sent to the JSP)
            FileInputStream fl = new FileInputStream(f);
            byte[] contentBytes = new byte[(int)f.length()];
            fl.read(contentBytes);
            fl.close();
            String encodedContent = Base64.getEncoder().encodeToString(contentBytes);
            request.setAttribute("encodedContent", encodedContent);
            returnFileName = "download_complete_export.jsp";
        } catch(InterruptedException e) {
            System.out.println("Cannot write CSV internship data.");
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