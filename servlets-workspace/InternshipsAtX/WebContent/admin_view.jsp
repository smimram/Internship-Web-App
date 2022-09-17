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
						<h1>Welcome ${user.name}! </h1>
					</span>
				</form>

				<%-- ${ (user.role == "Admin") ? ' --%>
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="user-management">	
					<div class="container-login100-form-btn p-t-50 p-b-25">
						<button type="submit" class="login100-form-btn">
							User management
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>

				<%-- ${ (user.role == "Admin" || user.role == "Professor") ? ' --%>
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="student-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Student management	
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>
				
				<%-- ${ (user.role == "Admin" || user.role == "Professor") ? ' --%>
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="program-management">	
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Program management
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>
				
				<%-- ${ (user.role == "Admin" || user.role == "Assistant" || user.role == "Professor") ? ' --%>
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="topic-validation">
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Topic validation
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>

				<%-- ${ (user.role == "Admin" || user.role == "Professor") ? ' --%>
				<form class="login100-form validate-form p-l-55 p-r-55" method="get" action="topic-management">
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Topic management
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>
				
				<%-- ${ (user.role == "Admin" || user.role == "Professor") ? ' --%>
				<form class="login100-form validate-form  p-l-55 p-r-55" method="get" action="topic-attribution">
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Topic attribution
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>
				
				<%-- ${ (user.role == "Admin" || user.role == "Assistant") ? ' --%>
				<form class="login100-form validate-form  p-l-55 p-r-55" method="get" action="topic-deletion">
					<div class="container-login100-form-btn p-t-25 p-b-25">
						<button type="submit" class="login100-form-btn">
							Topic deletion
						</button>
					</div>			
				</form>
				<%-- ' : '' } --%>
					
			</div>
		</div>
	</div>
</body>
</html>