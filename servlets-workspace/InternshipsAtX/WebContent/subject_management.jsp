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
	right:0;
	top:0
}
.select2-container--default .select2-selection--multiple .select2-selection__rendered li{
	margin: 5px 0px 0px 5px;
}
</style>
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
	            ${ (user.role == "Admin" || user.role == "Professor") ? '<li><a class="dropdown-item" href="./subject-attribution">Subject attribution</a></li>' : '' }
	            ${ (user.role == "Admin" || user.role == "Assistant") ? '<li><a class="dropdown-item" href="./subject-deletion">Subject deletion</a></li>' : '' }
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
						<h1> Subject Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1"> Id </div>
								<div class="col col-1">Title</div>
								<div class="col col-2">Categories</div>
								<div class="col col-2">Program</div>
								<div class="col col-2">Admin validate</div>
								<div class="col col-2">Sci validate</div>
								<div class="col col-1">Download</div>
								<div class="col col-1">Delete</div>
							</li>
							
							<c:forEach items="${subjects}" var="subject">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${subject.id}</div>
									<div class="col col-1" data-label="Title">${subject.title}</div>
									<div class="col col-2" data-label="Categories">
										<!-- update the categories of a subject -->
										<select class="mul-select" id="mul-select-${subject.id}" ${(user.role != "Assistant") ? '' : 'disabled'}  name="subjects[]" multiple="multiple" data-pid= "${subject.id}">
											<c:forEach items="${categories}" var="category">
												<option value="${category.id}">${category.name}</option>
											</c:forEach>
										</select>
									</div>
									<div class="col col-2" data-label="Program">
										<!-- update the program of a subject -->
										<select class="custom-select" name="role" ${(user.role != "Assistant") ? '' : 'disabled'}  onchange="updateSubjectProgram(${subject.id}, this);">
											<c:forEach items="${programs}" var="program">
												<option value="${program.id}" ${subject.programId == program.id ? 'selected' : '' }>${program.name} - ${program.year}</option>
											</c:forEach>
										</select>
									</div>
									<div class="col col-2" data-label="AdminValidate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<select id="select-admin-valid-${subject.id}" class="custom-select" onchange="updateSubjectAdminValid(${subject.id}, this);">
											<option value="true" ${subject.adminValid ? 'selected' : ''}>Admin valid</option>
											<option value="false" ${subject.adminValid ? '' : 'selected'}>Admin invalid</option>
										  
										</select>
									</div>
									<div class="col col-2" data-label="SciValidate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<select id="select-sci-valid-${subject.id}" class="custom-select" ${(subject.adminValid && user.role != "Assistant") ? '' : 'disabled'} onchange="updateSubjectSciValid(${subject.id}, this);">
											
											<option value="true" ${(role != "Assistant") ? '' : 'disabled' } ${subject.sciValid ? 'selected' : ''}>Sci. valid</option>
										  	<option value="false" ${(role != "Assistant") ? '' : 'disabled' } ${subject.sciValid ? '' : 'selected'}>Sci. invalid</option>
										</select>
									</div>
									<div class="col col-1">
										<a href="/InternshipsAtX/download-subject?internshipId=${subject.id}" target="_blank">Download</a>
									</div>
									<div class="col col-1" data-label="Delete">
										<button type="button" class="btn btn-secondary btn-sm" onclick="deleteSubject(${subject.id}, '${subject.title}');">Delete</button>
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
	
	<c:forEach items="${subjects}" var="subject">
		var vals = []
		<c:forEach items="${subject.getCategories()}" var="category">
			vals.push("${category.id}")
		</c:forEach>
		
		// initialize the multiple seletion box
		// select the pre-selected progams for each users
	    $('#mul-select-' + "${subject.id}").select2({
	    	placeholder:"-- select categories --"
	    }).val(vals).trigger('change');
		
		// listen for the select event
		$('#mul-select-' + "${subject.id}").on('select2:select', function (e) {
		    var categoryid = e.params.data.id;
		    var subjetid = ${subject.id}
		    updateSubjectCategory(subjetid, categoryid, true)		    
		});
		
		// listen for the unselect event
		$('#mul-select-' + "${subject.id}").on('select2:unselect', function (e) {
			var categoryid = e.params.data.id;
		    var subjetid = ${subject.id}
		    updateSubjectCategory(subjetid, categoryid, false)
		});
		
	</c:forEach>
	
});

function updateSubjectCategory(subjectId, categoryId, value){
    $.ajax({
        type : "GET",
        url : "UpdateSubjectCategoryServlet",
        data : "subjectId=" + subjectId + "&categoryId=" + categoryId + "&select=" + value,
        success : function(data) {
        	console.log("update subject " + subjectId + ", category " + categoryId + " select " + value);
        	if(value){
    		    // if the validate option was disabled(no program selected), remove it
    		    $('#select-valid-' + subjectId).attr("disabled",false);
        	}
        	alert("updated connection subject id " + subjectId + ", category id " + categoryId + " to " + value);
        	location.reload();
        },
        error: function(res){
        	alert("Failed to update subject category");
        	location.reload();
        }
    });
}

function updateSubjectProgram(subjectId, sel){
	var programId = sel.value;
    $.ajax({
        type : "GET",
        url : "UpdateSubjectProgramServlet",
        data : "subjectId=" + subjectId + "&programId=" + programId,
        success : function(data) {
        	console.log("update subject " + subjectId + " program into " + programId);
        	alert("updated prgram of subject id" + subjectId + " to program with id " + programId);
        	location.reload();
        },
        error: function(res){
        	alert("Failed to update subject program. Program and subject must have at least one common category!");
        	location.reload();
        }
    });
}

function updateSubjectAdminValid(subjectId, sel){
	var valid = sel.value;
    $.ajax({
        type : "GET",
        url : "UpdateSubjectAdminValidServlet",
        data : "subjectId=" + subjectId + "&valid=" + valid,
        success : function(data) {
        	console.log("update subject " + subjectId + " valid into " + valid);
        	alert("updated subject id " + subjectId + " admin valid to " + valid);
        	location.reload();
        },
        error: function(res){
        	alert("Failed to update subject admin valid");
        	location.reload();
        }
    });
}

function updateSubjectSciValid(subjectId, sel){
	var valid = sel.value;
    $.ajax({
        type : "GET",
        url : "UpdateSubjectSciValidServlet",
        data : "subjectId=" + subjectId + "&valid=" + valid,
        success : function(data) {
        	console.log("update subject " + subjectId + " valid into " + valid);
        	alert("updated subject id " + subjectId + " scientific valid to " + valid);
        	location.reload();
        },
        error: function(res){
        	alert("Failed to update subject scientifical valid");
        	location.reload();
        }
    });
}

function deleteSubject(id, title){
	var r = confirm("Are you sure you want to delete the subject " + title + " ?");
	if (r == true) {
	    $.ajax({
	        type : "GET",
	        url : "DeleteSubjectServlet",
	        data : "subjectId=" + id ,
	        success : function(data) {
	        	console.log("delete subject " + title);
	        	alert("deleted subject " + title);
	        	location.reload();
	        },
	        error: function(res){
	        	alert("Failed to delete the subject" + title + ". Cannot delete subject if it is already assigned to a student!");
	        }
	    });
	}
}


</script>



</body>
</html>