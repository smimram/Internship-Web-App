<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"
		 import="edu.polytechnique.inf553.Person"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>User management</title>
	<%@ include file="meta.jsp" %>
</head>
<style>
	.mul-select{
		width:100%;
		height:auto !important;
	}
	.select2-container .select2-selection--multiple .select2-selection__rendered{
		display:block;
	}
	.select2-container--default .select2-selection--multiple{
		position:relative;
	}
	.select2-container .select2-search--inline {
		position:absolute;
		right:0;
		top:0
	}
	.select2-container--default .select2-selection--multiple .select2-selection__rendered li{
		margin: 5px 0px 0px 5px;
	}
	/* The switch - the box around the slider */
	.switch {
		position: relative;
		display: inline-block;
		width: 60px;
		height: 34px;
	}
	/* Hide default HTML checkbox */
	.switch input {
		opacity: 0;
		width: 0;
		height: 0;
	}
	/* The slider */
	.slider {
		position: absolute;
		cursor: pointer;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: #ccc;
		-webkit-transition: .4s;
		transition: .4s;
	}
	.slider:before {
		position: absolute;
		content: "";
		height: 26px;
		width: 26px;
		left: 4px;
		bottom: 4px;
		background-color: white;
		-webkit-transition: .4s;
		transition: .4s;
	}
	input:checked + .slider {
		background-color: #2196F3;
	}
	input:focus + .slider {
		box-shadow: 0 0 1px #2196F3;
	}
	input:checked + .slider:before {
		-webkit-transform: translateX(26px);
		-ms-transform: translateX(26px);
		transform: translateX(26px);
	}
	/* Rounded sliders */
	.slider.round {
		border-radius: 34px;
	}
	.slider.round:before {
		border-radius: 50%;
	}
</style>
<body>
<!-- navigation bar -->
<jsp:include page="header.jsp"></jsp:include>

<div class="limiter">
	<div class="container-login100 background_style">
		<div class="wrap-login100-V2">
			<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1> Student list by program </h1>
					</span>

				<c:forEach items="${programs}" var="program">
					<h2 class="login100-form-btn-V2 p-l-5 p-r-5 d-inline-flex ml-3" style="width:100%; margin-top: 3rem; margin-bottom: 1rem;"> ${program.name} - ${program.year} </h2>
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1">Id
									<a href="/topic-management?orderByColumn=id&orderBySort=ASC"><i class="fas fa-sort-numeric-down" title="sort by increasing order"></i></a>
									<a href="/topic-management?orderByColumn=id&orderBySort=DESC"><i class="fas fa-sort-numeric-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-8">Name
									<a href="/topic-management?orderByColumn=title&orderBySort=ASC"><i class="fas fa-sort-alpha-down" title="sort by increasing order"></i></a>
									<a href="/topic-management?orderByColumn=title&orderBySort=DESC"><i class="fas fa-sort-alpha-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-3">Topic title</div>
							</li>

							<c:forEach items="${program.getStudents()}" var="student">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${student.id}</div>
									<div class="col col-8" data-label="Title">${student.name}<button type="button" class="btn btn-secondary btn-sm" style="margin-left: 10px;" onclick="displayEmail('${student.email}')"><i class="fas fa-at" style="color: white"></i></button></div>
									<div class="col col-3" data-label="Title">${student.internshipTitle}</div>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:forEach>
			</form>
		</div>
	</div>
</div>
</body>

<script>
	function displayEmail(email) {
		alert("Email: " + email);
	}
</script>
</html>