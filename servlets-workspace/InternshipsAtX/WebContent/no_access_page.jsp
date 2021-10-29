<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>No access</title>
	<%@ include file="meta.jsp" %>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">


					<form class="login100-form validate-form p-l-55 p-r-55 p-t-178 p-b-40">
						<span class="login100-form-title">
							<h1>Sorry, You Are Not Allowed to Access This Page :(  </h1>
						</span>
						<div class="text-center">
							<h3>${errorMessage}</h3>
							<a href="./home"><h3 class="m-t-20">Click here to go home</h3></a>
						</div>
					</form>
				
			</div>
		</div>
	</div>
</body>
</html>