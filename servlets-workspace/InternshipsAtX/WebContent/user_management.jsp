<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Login V8</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	<link rel="stylesheet" type="text/css" href="css/table.css">
<!--===============================================================================================-->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js" integrity="sha384-w1Q4orYjBQndcko6MimVbzY0tgp4pWB4lZ7lr30WKz0vr/aWKhXdBNmNb5D92v7s" crossorigin="anonymous"></script>
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet" />
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
<!--===============================================================================================-->
</head>
<style>
.mul-select{
	width:100%;
	height:auto !important;
}
.select2-container .select2-selection--multiple .select2-selection__rendered{
	display:block;
}
.responsive-table .selection li{
	margin:0px;
}
</style>
<body>

<%
Person user = (Person)session.getAttribute("user");
String name = user.getName();
String role = user.getRole();
%>

	<nav class="navbar navbar-dark bg-dark">
	  <div class="container-fluid justify-content-start">
	    <a class="navbar-brand" href="/InternshipsAtX/admin-view">
	      <img src="images/logo.png" style="max-height: 35px;">
	      Internship Management
	    </a>
	    <div class="ml-auto d-flex">
	        <div class="nav-item dropdown">
	          <a class="text-white dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
	            <%=role %>: <%=name %>
	          </a>
	          <ul class="dropdown-menu" aria-labelledby="navbarDropdown" style="right:0;left:auto;">
	            <li><a class="dropdown-item" href="./user-management">User management</a></li>
	            <li><a class="dropdown-item" href="./program-management">Program management</a></li>
	            <li><a class="dropdown-item" href="./subject-management">Subject management</a></li>
	            <li><a class="dropdown-item" href="./subject-attribution">Subject attribution</a></li>
	            <li><hr class="dropdown-divider"></li>
	            <li><a class="dropdown-item" href="./LogoutServlet">Log out</a></li>
	          </ul>
	        </div>
	    </div>
	  </div>
	</nav>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1> User Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1"> Id </div>
								<div class="col col-2">Name</div>
								<div class="col col-2">Role</div>
								<div class="col col-4">Program</div>
								<div class="col col-2">Validate</div>
							</li>
							
							<c:forEach items="${persons}" var="person">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${person.id}</div>
									<div class="col col-2" data-label="Subject Title">${person.name}</div>
									<div class="col col-2" data-label="Supervisor Name">
										<select class="custom-select">
					        				<option value="Admin" ${person.role == "Admin" ? 'selected' : ''}>Admin</option>
					        				<option value="Assistant" ${person.role == "Assistant" ? 'selected' : ''} >Assistant</option>
					        				<option value="Professor" ${person.role == "Professor" ? 'selected' : ''} >Professor</option>
					        				<option value="Student" ${person.role == "Student" ? 'selected' : ''} >Student</option>
										</select>
									</div>
									<div class="col col-4" data-label="Supervisor Email">
										<select class="mul-select" id="mul-select-${person.id}" name="programs[]" multiple="multiple">
											<c:forEach items="${programs}" var="program">
												<option value="${program.id}">${program.name} - ${program.year}</option>
											</c:forEach>
										</select>
									</div>
									<div class="col col-2" data-label="Subject">
										<!-- need to select at least one program before validate -->
										<select class="custom-select" ${person.programSize() == 0 ? 'disabled' : ''}>
										  <option value="true" ${person.valid ? 'selected' : ''}>Validate</option>
										  <option value="false" ${person.valid ? '' : 'selected'}>Invalidate</option>
										</select>
									</div>
								</li>
							</c:forEach>
							
						</ul>
					</div>
				</form>
				

			</div>
		</div>
	</div>
	
<script>

// initialize the multiple seletion box
$(document).ready(function() {
	<c:forEach items="${persons}" var="person">
		var vals = []
		<c:forEach items="${person.getPrograms()}" var="program">
			vals.push("${program.id}")
		</c:forEach>
		// select the pre-selected progams for each users
	    $('#mul-select-' + "${person.id}").select2({
	    	placeholder:"-- select programs --"
	    }).val(vals).trigger('change');
	</c:forEach>
});
</script>

</body>
</html>