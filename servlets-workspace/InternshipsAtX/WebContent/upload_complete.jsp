<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>Upload complete</title>
	<%@ include file="meta.jsp" %>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">


					<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
						<span class="login100-form-title">
							<h1>Thank you for uploading your topic: <br> ${topicTitle}  </h1>
						</span>
					</form>
					
					<form>
						<span class="subtitle" style="color:black;"> <h4> <br> <br> Your topic submission will be shortly reviewed by teachers and administrators. <br>
						It should then be visible on the website. </h4> <br> <br> </span>
					</form>
					
				
			</div>
		</div>
	</div>
	
	

</body>
</html>