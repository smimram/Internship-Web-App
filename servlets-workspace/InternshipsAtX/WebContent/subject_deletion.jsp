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
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	<link rel="stylesheet" type="text/css" href="css/table.css">
<!--===============================================================================================-->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet" />
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
<!--===============================================================================================-->

</head>
<style>
	.responsive-table li.table-row{
		margin-bottom:10px;
		padding: 10px 30px;
	}
</style>
<body>

	<!-- navigation bar -->
	<nav class="navbar navbar-dark bg-dark">
	  <div class="container-fluid justify-content-start">
	    <a class="navbar-brand" href="/InternshipsAtX/dashboard">
	      <img src="images/logo.png" style="max-height: 35px;">
	      Internship Management
	    </a>
	    <div class="ml-auto d-flex">
	        <div class="nav-item dropdown">
	          <a class="text-white dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
	            ${user.role}: ${user.name}
	          </a>
	          <ul class="dropdown-menu" aria-labelledby="navbarDropdown" style="right:0;left:auto;">
	            ${ (user.role == "Admin") ? '<li><a class="dropdown-item" href="./user-management">User management</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./program-management">Program management</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Assistant" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./subject-validation">Subject validation</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./subject-management">Subject management</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./subject-attribution">Subject attribution</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Assistant") ? '<li><a class="dropdown-item" href="./subject-deletion">Subject deletion</a></li>' : '' }
	            <li><hr class="dropdown-divider"></li>
	            <li><a class="dropdown-item" href="./LogoutServlet">Log out</a></li>
	          </ul>
	        </div>
	    </div>
	  </div>
	</nav>
	
	<!-- program management -->
	<div class="limiter">
		<div class="container-login100 background_style" style="min-height:30vh;">
			<div class="wrap-login100-V2">
				<div class="login100-form validate-form p-l-55 p-r-55 p-t-140 p-b-40">
					<span class="login100-form-title">
						<h1> Delete subjects </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-2"> ID </div>
								<div class="col col-4"> SUBJECT TITLE </div>
								<div class="col col-4"> DELETE </div>
							</li>
							<c:forEach items="${subjects}" var="subject">
								<li class="table-row">
									<div class="col col-2" data-label="Id">${subject.id}</div>
									<div class="col col-4" data-label="Title">${subject.title}</div>
									<div class="col col-4" data-label="Delete">
										<button type="button" class="btn btn-primary ml-3" onclick="deleteSubject(${subject.id}, '${subject.title}');">Delete</button>
									</div>
								</li>
							</c:forEach>
							
						</ul>
					</div>
					
				</div>
				
			</div>
		</div>
	</div>
	
<script>

function deleteSubject(id, title){
	alert("dad");
	var r = confirm("Are you sure you want to delete the subject " + title + " ?");
	if (r == true) {
	    $.ajax({
	        type : "GET",
	        url : "DeleteSubjectServlet",
	        data : "subjectId=" + id ,
	        success : function(data) {
	        	console.log("delete subject " + title);
	        	location.reload();
	        },
	        error: function(res){
	        	alert("Failed to delete the subject" + title);
	        }
	    });
	}
}

</script>

</body>
</html>