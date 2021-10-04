<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
			
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="login">	
					<div class="container-login100-form-btn p-t-50 p-b-25">
						<button type="submit" class="login100-form-btn">
							Log In
						</button>
					</div>			
				</form>

				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="sign-in">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Register now
						</button>
					</div>			
				</form>
				
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="upload-topic">	
					<div class="container-login100-form-btn p-t-25 p-b-50">
						<button type="submit" class="login100-form-btn">
							Upload A Topic
						</button>
					</div>			
				</form>
				<h1 class="easter-egg" style="visibility:hidden; font-size:0;">Remi Delacourt was here</h1> <!-- An easter-egg ! -->
			</div>
		</div>
	</div>
	
	

</body>
</html>