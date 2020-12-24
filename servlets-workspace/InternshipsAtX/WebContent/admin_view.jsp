<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
<!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100">
				
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1>Welcome ${name} ! </h1>
						<h1 class="subtitle" style="color:red;"> ${role} </h1>
					</span>
				</form>

					
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="user-management">	
					<div class="container-login100-form-btn p-t-50 p-b-25">
						<button type="submit" class="login100-form-btn">
							User management
						</button>
					</div>			
				</form>
				

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="program-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Program management
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
	
	

</body>
</html>