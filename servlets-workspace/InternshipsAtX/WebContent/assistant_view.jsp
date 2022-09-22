<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Home</title>
	<%@ include file="meta.jsp" %>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100">
				
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1>Welcome ${user.name} ! </h1>
					</span>
				</form>

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="student-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Student registration by program
						</button>
					</div>			
				</form>

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="topic-validation">
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Topic validation
						</button>
					</div>			
				</form>
				
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="program-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Topic deletion
						</button>
					</div>			
				</form>
				
			</div>
		</div>
	</div>
</body>
</html>