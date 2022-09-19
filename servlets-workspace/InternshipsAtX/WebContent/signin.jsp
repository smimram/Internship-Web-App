<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Login</title>
	<%@ include file="meta.jsp" %>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100">
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178" method="post" action="sign-in">
					<span class="login100-form-title">
						Register now
					</span>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name="firstName" placeholder="First Name" value="${firstName}">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name="lastName" placeholder="Last Name" value="${lastName}">
						<span class="focus-input100"></span>
					</div>
					
					
					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name="email" placeholder="Email" value="${email}">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Please confirm your email">
						<input class="input100" type="text" name="confirmEmail" placeholder="Confirm Email" value="${confirmEmail}">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate = "Please enter password">
						<input class="input100" type="password" name="pass" placeholder="Password (at least 8 characters)">
						<span class="focus-input100"></span>
					</div>
					
					<div class="wrap-input100 validate-input m-b-16" data-validate = "Please confirm password">
						<input class="input100" type="password" name="confirmPass" placeholder="Confirm password">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input" data-validate = "Please choose your role">
						<select name="role" class="input100" id="selectRole" onchange="displayProgramsForStudents();">
								<option value="" selected disabled hidden>--Please choose a role--</option>
								<option value="Admin" selected>Admin</option>
								<option value="Assistant">Assistant</option>
								<option value="Professor">Professor</option>
								<option value="Student">Student</option>
							</select>
						<span class="focus-input100"></span>
					</div>

					<div id ="programsForStudents" class="wrap-input100 validate-input" data-validate = "Please choose your program">
						<select name="programStudent" class="input100">
							<option value="null" selected>Select a program</option>
							<c:forEach items="${programs}" var="program">
								<option value="${program.id}">${program.name} - ${program.year}</option>
							</c:forEach>
						</select>
					</div>

					<div class="wrap-input100 validate-input">
						<span class="focus-input100"></span>
					</div>

					<div class="container-login100-form-btn p-t-40">
						<button type="submit" class="login100-form-btn">
							Sign in
						</button>
					</div>
					
					<div class="text-red flex-col-c p-t-100 p-b-40">
						<p class="text-red" style="color:red;">${err_message}</p>
					</div>
				</form>
				
			</div>
		</div>
	</div>
	
	

</body>
</html>

<script type="text/javascript">
$("#programsForStudents").hide(); // by default, hide the program select. Show it only for students

function displayProgramsForStudents() {
	if($("#selectRole option:selected").text() == "Student") {
		$("#programsForStudents").show();
	} else {
		$("#programsForStudents").hide();
	}
}
</script>