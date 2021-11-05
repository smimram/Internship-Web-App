package edu.polytechnique.inf553;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/*
 * Test out the hack! (only works if forgotten to put the '?' instead of the strings concatenation) Put this in passwords in SigninServlet : 
 * pppppppp'); drop table error; insert into person(name, email, creation_date, valid, password) values ('fds, aaaa', 'fds.aaaa@gmail.com', '2020-12-14', false, '12345678
 */

@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class UploadFicheServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of UploadTopicServlet
	 * Query the programs and their categories
	 */
	public UploadFicheServlet() {
		super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost called with parameter " + request.getQueryString());

		int subjectId = Integer.parseInt(request.getParameter("subjectId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		Part file = request.getPart("fiche");

		Connection con = null;
		try {
			con = DbUtils.getInstance().getConnection();
			if (con == null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}

			// update the slides
			String query = "UPDATE internship SET fiche = ?, timestamp_fiche = ? WHERE id = ?;";
			PreparedStatement ps = con.prepareStatement(query);
			InputStream inputStream = file.getInputStream();
			if (inputStream != null) {
				ps.setBinaryStream(1, inputStream);
				ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				ps.setInt(3, subjectId);
				int row = ps.executeUpdate();
				if (row <= 0) {
					System.out.println("ERROR: File was not uploaded and saved into database");
				} else {
					System.out.println(row);
				}
			} else {
				System.out.println("input stream is null");
			}

			// get the internship to display it in the student view
			request.setAttribute("userSubject", CommonInterface.getSubjectOfUSer(userId, con));
			// also set the list of programs of the user as well as the categories of each program
			List<Program> programs = CommonInterface.getProgramsOfUser(userId, con);
			request.setAttribute("programs", programs);
			request.setAttribute("subjectsPerCategory", CommonInterface.getSubjectsPerCategory(programs, con));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.getInstance().releaseConnection(con);
		}
		request.getRequestDispatcher("student_view.jsp").forward(request, response);
	}
}
