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
	<link rel="stylesheet" type="text/css" href="css/table.css">
<!--===============================================================================================-->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
	<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet" />
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
<!--===============================================================================================-->
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
	left:0;
	top:0
}
.select2-container--default .select2-selection--multiple .select2-selection__rendered li{
	margin: 5px 0px 0px 5px;
}
</style>
<body>

	<nav class="navbar navbar-dark bg-dark">
	  <div class="container-fluid justify-content-start">
	    <a class="navbar-brand" href="/InternshipsAtX/admin-view">
	      <img src="images/logo.png" style="max-height: 35px;">
	      Internship Management
	    </a>
	    <div class="ml-auto d-flex">
	        <div class="nav-item dropdown">
	          <a class="text-white dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
	            ${user.role}: ${user.name}
	          </a>
	          <ul class="dropdown-menu" aria-labelledby="navbarDropdown" style="right:0;left:auto;">
	            <li><a class="dropdown-item" href="./user-management">User management</a></li>
	            <li><a class="dropdown-item" href="./program-management">Program management</a></li>
	            <li><a class="dropdown-item" href="./subject-management">Subject management</a></li>
	            <li><a class="dropdown-item" href="./subject-attribution">Subject attribution</a></li>
	            <li><hr class="dropdown-divider"></li>
	            <li><a class="dropdown-item" href="./LogoutServlet">Log out</a></li>
	          </ul>
	        </div>
	    </div>
	  </div>
	</nav>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1> User Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1"> Id </div>
								<div class="col col-2">Name</div>
								<div class="col col-2">Role</div>
								<div class="col col-4">Program</div>
								<div class="col col-2">Validate</div>
							</li>
							
							<c:forEach items="${persons}" var="person">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${person.id}</div>
									<div class="col col-2" data-label="Name">${person.name}</div>
									<div class="col col-2" data-label="Role">
										<!-- update the role of a user -->
										<select class="custom-select" name="role" onchange="updateUserRole(${person.id}, this);">
					        				<option value="5" ${person.role == "Admin" ? 'selected' : ''}>Admin</option>
					        				<option value="2" ${person.role == "Assistant" ? 'selected' : ''} >Assistant</option>
					        				<option value="3" ${person.role == "Professor" ? 'selected' : ''} >Professor</option>
					        				<option value="1" ${person.role == "Student" ? 'selected' : ''} >Student</option>
										</select>
									</div>
									<div class="col col-4" data-label="Program">
										<!-- update the programs of a user -->
										<select class="mul-select" id="mul-select-${person.id}" name="programs[]" multiple="multiple" data-pid= "${person.id}">
											<c:forEach items="${programs}" var="program">
												<option value="${program.id}">${program.name} - ${program.year}</option>
											</c:forEach>
										</select>
									</div>
									<div class="col col-2" data-label="Validate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<select class="custom-select" ${person.programSize() == 0 ? 'disabled' : ''} onchange="updateUserValid(${person.id}, this);">
										  <option value="true" ${person.valid ? 'selected' : ''}>Valid</option>
										  <option value="false" ${person.valid ? '' : 'selected'}>Invalid</option>
										</select>
									</div>
								</li>
							</c:forEach>
							
						</ul>
					</div>
				</form>
				

			</div>
		</div>
	</div>
	
<script>


$(document).ready(function() {
	
	<c:forEach items="${persons}" var="person">
		var vals = []
		<c:forEach items="${person.getPrograms()}" var="program">
			vals.push("${program.id}")
		</c:forEach>
		
		// initialize the multiple seletion box
		// select the pre-selected progams for each users
	    $('#mul-select-' + "${person.id}").select2({
	    	placeholder:"-- select programs --"
	    }).val(vals).trigger('change');
		
		// listen for the select event
		$('#mul-select-' + "${person.id}").on('select2:select', function (e) {
		    var programid = e.params.data.id;
		    var pid = ${person.id}
		    updateUserProgram(pid, programid, true)
		    
		});
		
		// listen for the unselect event
		$('#mul-select-' + "${person.id}").on('select2:unselect', function (e) {
		    var programid = e.params.data.id;
		    var pid = ${person.id}
		    updateUserProgram(pid, programid, false)
		    
		});
		
	</c:forEach>
	
});


function updateUserProgram(pid, programid, select){
    $.ajax({
        type : "GET",
        url : "UpdateUserProgramServlet",
        data : "pid=" + pid + "&programid=" + programid + "&select=" + select,
        success : function(data) {
        	console.log("update user " + pid + ", program " + programid + " select " + select)
        },
        error: function(res){
        	alert("Failed to update user program");
        	location.reload();
        }
    });
}


function updateUserRole(pid, sel){
	var rid = sel.value;
    $.ajax({
        type : "GET",
        url : "UpdateUserRoleServlet",
        data : "pid=" + pid + "&rid=" + rid,
        success : function(data) {
        	console.log("update user " + pid + " role into " + rid)
        },
        error: function(res){
        	alert("Failed to update user role");
        	location.reload();
        }
    });
}

function updateUserValid(pid, sel){
	var valid = sel.value;
    $.ajax({
        type : "GET",
        url : "UpdateUserValidServlet",
        data : "pid=" + pid + "&valid=" + valid,
        success : function(data) {
        	console.log("update user " + pid + " valid into " + valid)
        },
        error: function(res){
        	alert("Failed to update user valid");
        	location.reload();
        }
    });
}
</script>

</body>
</html>