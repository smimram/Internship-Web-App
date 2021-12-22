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

public class DownloadSlidesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 16177215;

	/**
	 * Constructor
	 */
	public DownloadSlidesServlet() {
		super();	
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());

		int internshipId = Integer.parseInt(request.getParameter("internshipId"));
		String returnFileName = "file_not_found.jsp";
		
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			if (con == null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
			
			String query = "SELECT slides, title " +
						   "FROM internship " +
						   "WHERE internship.id = ? AND slides IS NOT NULL;";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, internshipId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				returnFileName = "download_complete_slides.jsp";
				InputStream inputStream = rs.getBinaryStream("slides");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                 
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);                  
                }
                 
                byte[] contentBytes = outputStream.toByteArray();
                String encodedContent =  Base64.getEncoder().encodeToString(contentBytes);
                
                inputStream.close();
                outputStream.close();

                request.setAttribute("internshipId", internshipId);
                request.setAttribute("subjectTitle", rs.getString("title"));
                request.setAttribute("encodedContent", encodedContent);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.releaseConnection(con);
		}

		request.getRequestDispatcher(returnFileName).forward(request, response);
	}
	
}
