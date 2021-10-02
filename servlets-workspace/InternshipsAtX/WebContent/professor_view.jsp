<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Home</title>
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
<!--===============================================================================================-->
</head>
<body>

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
	            ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./subject-management">Subject management</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./defense-management">Defense management</a></li>' : '' }
	            <li><hr class="dropdown-divider"></li>
	            <li><a class="dropdown-item" href="./LogoutServlet">Log out</a></li>
	          </ul>
	        </div>
	    </div>
	  </div>
	</nav>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100">
				
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1>Welcome ${user.name} ! </h1>
					</span>
				</form>
				

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="program-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Program management
						</button>
					</div>			
				</form>

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="subject-validation">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Subject validation
						</button>
					</div>			
				</form>

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="subject-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Subject management	
						</button>
					</div>			
				</form>
				
				<form class="login100-form validate-form  p-l-55 p-r-55" method="get" action="subject-attribution">	
					<div class="container-login100-form-btn p-t-25 p-b-50">
						<button type="submit" class="login100-form-btn">
							Subject attribution
						</button>
					</div>			
				</form>	
				
			</div>
		</div>
	</div>
	
<!-- Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>