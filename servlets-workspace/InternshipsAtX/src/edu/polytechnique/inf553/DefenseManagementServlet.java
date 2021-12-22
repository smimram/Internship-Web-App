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
 * Servlet implementation class SubjectManagementServlet
 */
@WebServlet("/DefenseManagementServlet")
public class DefenseManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DefenseManagementServlet() {
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
				List<Defense> defenses = getDefenses(orderByColumn, orderBySort);
				List<Person> professors = getProfessors(orderByColumn, orderBySort);
				List<Person> students = getStudents(orderByColumn, orderBySort);
				request.setAttribute("defenses", defenses);
				request.setAttribute("professors", professors);
				request.setAttribute("students", students);
				request.getRequestDispatcher("defense_management.jsp").forward(request, response);
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

	private String getSortColumn(String orderByColumn) {
		if (orderByColumn == null)
			orderByColumn = "id"; // if no parameter is provided
		if (orderByColumn.startsWith("'") && orderByColumn.endsWith("'"))
			orderByColumn = orderByColumn.substring(1, orderByColumn.length() - 1); // if the value is encapsulated into '', e.g. 'id'
		return orderByColumn;
	}

	private String getSortOrder(String orderBySort) {
		if (orderBySort == null)
			orderBySort = "ASC"; // if no parameter is provided
		if (orderBySort.startsWith("'") && orderBySort.endsWith("'"))
			orderBySort = orderBySort.substring(1, orderBySort.length() - 1); // if the value is encapsulated into '', e.g. 'id'
		return orderBySort;
	}

	private List<Defense> getDefenses(String orderByColumn, String orderBySort) {
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			if (con == null) {
				return null;
			}

			List<Defense> defenses = new ArrayList<>();
			orderByColumn = this.getSortColumn(orderByColumn);
			orderBySort = this.getSortOrder(orderBySort);
			String query = "SELECT DISTINCT "
					+ "d.id, d.date, d.time, " // 1, 2, 3
					+ "r.id, r.name, rt.role, r.valid, r.email, " // 4, 5, 6, 7, 8
					+ "j.id, j.name, rt2.role, j.valid, j.email, " // 9, 10, 11, 12, 13
					+ "s.id, s.name, rt3.role, s.valid, s.email " // 14, 15, 16, 17, 18
					+ "FROM defense d "
					+ "LEFT JOIN person r ON r.id = d.referent_id "
					+ "LEFT JOIN person_roles pr ON pr.person_id = r.id "
					+ "LEFT JOIN role_type rt ON rt.id = pr.role_id "
					+ "LEFT JOIN person j ON j.id = d.jury2_id "
					+ "LEFT JOIN person_roles pr2 ON pr2.person_id = j.id "
					+ "LEFT JOIN role_type rt2 ON rt2.id = pr2.role_id "
					+ "LEFT JOIN person s ON s.id = d.student_id "
					+ "LEFT JOIN person_roles pr3 ON pr3.person_id = s.id "
					+ "LEFT JOIN role_type rt3 ON rt3.id = pr3.role_id "
					+ "ORDER BY d." + orderByColumn + " " + orderBySort + ";";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) { // TODO NELLY: check roles
				Person referent = new Person(resultSet.getString(5), resultSet.getInt(4), resultSet.getString(6), resultSet.getBoolean(7), resultSet.getString(8));
				Person jury2 = new Person(resultSet.getString(10), resultSet.getInt(9), resultSet.getString(11), resultSet.getBoolean(12), resultSet.getString(13));
				Person student = new Person(resultSet.getString(15), resultSet.getInt(14), resultSet.getString(16), resultSet.getBoolean(17), resultSet.getString(18));
				Defense defense = new Defense(resultSet.getInt(1), resultSet.getDate(2), resultSet.getTime(3), referent, jury2, student);
				defenses.add(defense);
			}
			return defenses;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.releaseConnection(con);
		}
	}

	private List<Person> getProfessors(String orderByColumn, String orderBySort) {
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			if (con == null) {
				return null;
			}

			List<Person> professors = new ArrayList<>();
			// get professors list
			orderByColumn = this.getSortColumn(orderByColumn);
			orderBySort= this.getSortOrder(orderBySort);
			String query = "" +
					"SELECT p.id, p.name, rt.role, p.valid, p.email " +
					"FROM person p " +
					"LEFT JOIN person_roles pr ON p.id = pr.person_id " +
					"LEFT JOIN role_type rt ON pr.role_id = rt.id " +
					"WHERE rt.role = 'Professor' " +
					"ORDER BY p." + orderByColumn + " " + orderBySort + ";";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) { // TODO NELLY: check roles
				Person professor = new Person(resultSet.getString(2), resultSet.getInt(1), resultSet.getString(3), resultSet.getBoolean(4), resultSet.getString(5));
				professors.add(professor);
			}

			return professors;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.releaseConnection(con);
		}
	}

	private List<Person> getStudents(String orderByColumn, String orderBySort) {
		Connection con = null;
		try {
			con = DbUtils.getConnection();
			if (con == null) {
				return null;
			}

			List<Person> professors = new ArrayList<>();
			// get professors list
			orderByColumn = this.getSortColumn(orderByColumn);
			orderBySort = this.getSortOrder(orderBySort);
			String query = "" +
					"SELECT p.id, p.name, rt.role, p.valid, p.email " +
					"FROM person p " +
					"LEFT JOIN person_roles pr ON p.id = pr.person_id " +
					"LEFT JOIN role_type rt ON pr.role_id = rt.id " +
					"WHERE rt.role = 'Student' AND p.id IN (SELECT pi.person_id FROM person_internship pi) AND p.id NOT IN (SELECT student_id FROM defense) " +
					"ORDER BY p." + orderByColumn + " " + orderBySort + ";";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) { // TODO NELLY: check roles
				Person professor = new Person(resultSet.getString(2), resultSet.getInt(1), resultSet.getString(3), resultSet.getBoolean(4), resultSet.getString(5));
				professors.add(professor);
			}

			return professors;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtils.releaseConnection(con);
		}
	}

}