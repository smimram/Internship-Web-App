package edu.polytechnique.inf553;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InternshipServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor (see InternshipServlet)
	 */
	public LoginServlet() {
		super();
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Override of method doGet of InternshipServlet
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Informs that a user requested access with parameters
		System.out.println(this.getClass().getName() + " doGet method called with path " + request.getRequestURI() + " and parameters " + request.getQueryString());
		response.getWriter().append("<!DOCTYPE html><html><head><title>Login</title></head>");
		response.getWriter().append("<body>");
		response.getWriter().append("<div class='bg-light' style='width: 200px; height: 200px; position: absolute; left:50%; top:50%;  margin:-100px 0 0 -100px; padding-top: 40px; padding-left: 10px;'>");
		response.getWriter().append("<input id='reservationID' style='display: none' value='' />");
		response.getWriter().append("<div>Email : </div>");
		response.getWriter().append("<div><input id='email' type='text' onKeyPress='return checkIt(event);' name='email' maxlength='4' /></div>");
		response.getWriter().append("<div>Password : </div>");
		response.getWriter().append("<div><input id='password' type='text' onKeyPress='return checkIt(event);' name='password' maxlength='4' /></div>");
		response.getWriter().append("<span style='font-size: 75%;'>"+"</span>");
		response.getWriter().append("<div><input type='button'  name='buttonsave' value='Login' onclick='makePayment();' /></div>");
		response.getWriter().append("<div><input type='button'  name='buttoncancel' value='Forgot password' onclick='cancelPayment();' /></div>");
		response.getWriter().append("</div>");
		response.getWriter().append("</body>");
		response.getWriter().append("<result>");
		String creditno = request.getParameter("email");
		String s = DbUtils.dbName;
	}
}
