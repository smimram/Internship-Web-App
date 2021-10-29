<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Log In</title>
	<%@ include file="meta.jsp" %>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100">
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178" method="post" action="login">
					<span class="login100-form-title">
						Log In
					</span>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name=email placeholder="Email">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input" data-validate = "Please enter password">
						<input class="input100" type="password" name="pass" placeholder="Password">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input">
						<span class="focus-input100"></span>
					</div>

					<div class="container-login100-form-btn p-t-40">
						<button type="submit" class="login100-form-btn">
							Log In
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