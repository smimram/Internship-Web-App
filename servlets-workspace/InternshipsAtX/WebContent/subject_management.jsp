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
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta2/css/all.min.css">
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
								<div class="col col-1">Id </div>
								<div class="col col-2">Title</div>
								<div class="col col-2">Categories</div>
								<div class="col col-2">Program</div>
								<div class="col col-1">Admin validate</div>
								<div class="col col-1">Sci validate</div>
								<div class="col col-2">Attributed to</div>
								<%-- <div class="col col-1">Download</div> --%>
								<div class="col col-1">Actions</div>
							</li>
							
							<c:forEach items="${subjects}" var="subject">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${subject.id}</div>
									<div class="col col-2" data-label="Title">${subject.title}</div>
									<div class="col col-2" data-label="Categories">
										<!-- update the categories of a subject -->
										<select class="mul-select" id="mul-select-category-${subject.id}" ${(user.role != "Assistant") ? '' : 'disabled'}  name="subjects[]" multiple="multiple" data-pid= "${subject.id}">
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
									<div class="col col-1" data-label="AdminValidate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<label class="switch">
											<input type="checkbox" id="select-admin-valid-${subject.id}" onchange="updateSubjectAdminValid(${subject.id}, this);" ${subject.adminValid ? 'checked' : ''}>
											<span class="slider round"></span>
										</label>
									</div>
									<div class="col col-1" data-label="SciValidate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<label class="switch">
											<input type="checkbox" id="select-sci-valid-${subject.id}" onchange="updateSubjectSciValid(${subject.id}, this);" ${subject.sciValid ? 'checked' : ''}> <!-- ${(subject.adminValid && user.role != "Assistant") ? '' : 'disabled'}  -->
											<span class="slider round"></span>
										</label>
									</div>
									<div class="col col-2" data-label="AttributedTo">
										<c:choose>
											<c:when test="${subject.affiliatedStudent != null}"> <!-- if there is an affiliated student, display it -->
												${subject.affiliatedStudent.name}
											</c:when>
											<c:otherwise> <!-- else display the list of students without internships -->
												<select class="custom-select" id="select-aff-student-subject-${subject.id}" name="assignedStudent" ${(user.role != "Assistant") ? '' : 'disabled'} onfocus="updateOldAffiliatedStudent(this)" onchange="updateSubjectAffiliatedStudent(${subject.id}, this);">
													<option value="null">No student</option>
													<c:forEach items="${studentsNoInternship}" var="studentNoInternship">
														<option value="${studentNoInternship.id}" ${subject.affiliatedStudent.id == studentNoInternship.id ? 'selected' : '' }>${studentNoInternship.name}</option>
													</c:forEach>
												</select>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col col-1">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/InternshipsAtX/download-subject?internshipId=${subject.id}" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
										
									<%-- </div> --%>
									<%-- <div class="col col-1" data-label="Delete"> --%>
										<button type="button" class="btn btn-secondary btn-sm" onclick="deleteSubject(${subject.id}, '${subject.title}');"><i class="fas fa-trash"></i></button>
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
		var oldAffiliatedStudent = null;
		<c:forEach items="${subjects}" var="subject">
			var valCategories = []
			<c:forEach items="${subject.getCategories()}" var="category">
				valCategories.push("${category.id}")
			</c:forEach>
			
			// initialize the multiple selection box for the subject categories
			// select the pre-selected programs for each users
			$('#mul-select-category-' + "${subject.id}").select2({
				placeholder:"-- select categories --"
			}).val(valCategories).trigger('change');
			
			// listen for the select event
			$('#mul-select-category-' + "${subject.id}").on('select2:select', function (e) {
				var categoryId = e.params.data.id;
				var subjectId = ${subject.id}
				updateSubjectCategory(subjectId, categoryId, true)		    
			});
			
			// listen for the unselect event
			$('#mul-select-category-' + "${subject.id}").on('select2:unselect', function (e) {
				var categoryId = e.params.data.id;
				var subjectId = ${subject.id}
				updateSubjectCategory(subjectId, categoryId, false)
			});

			// // initialize the multiple selection box for the unaffiliated students
			// // select the pre-selected progams for each users
			// var valStudents = []
			// <c:forEach items="${subjects}" var="studentNoInternship">
			// 	valStudents.push("${studentNoInternship.id}")
			// </c:forEach>
			// console.log(valStudents);
			// $('#mul-select-student-' + "${subject.id}").select2({
			// 	placeholder:"-- no student affiliated --"
			// }).val(valStudents).trigger('change');
			
			// // // listen for the select event
			// $('#mul-select-student-' + "${subject.id}").on('select2:select', function (e) {
			//     var studentId = e.params.data.id;
			//     var subjectId = ${subject.id}
			//     assignStudentToAffiliation(subjectId, studentId)		    
			// });
			
			// // // listen for the unselect event
			// $('#mul-select-student-' + "${subject.id}").on('select2:unselect', function (e) {
			// 	var studentId = e.params.data.id;
			//     var subjectId = ${subject.id}
			//     unassignStudentToAffiliation(subjectId, studentId)
			// });
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

	function updateOldAffiliatedStudent(selection) {
		console.log("onfocus");
		oldAffiliatedStudent = selection.value;
		console.log(oldAffiliatedStudent);
	}

	function updateSubjectAffiliatedStudent(subjectId, newStudent) {
		if(oldAffiliatedStudent == "" || oldAffiliatedStudent == "null" || oldAffiliatedStudent == undefined || oldAffiliatedStudent == "undefined" ) {
			oldStudentId = null;
		} else {
			oldStudentId = oldAffiliatedStudent;
		}
		console.log("onchange");
		newStudentId = newStudent.value;

		console.log(oldStudentId + " ; " + newStudentId);
		if(oldStudentId != null && newStudentId != null) {
			unassignStudentToAffiliation(subjectId, oldStudentId);
			assignStudentToAffiliation(subjectId, newStudentId);
			console.log("unassign " + oldStudentId + " + assign " + newStudentId);
		} else if(oldStudentId == null && newStudentId != null) {
			assignStudentToAffiliation(subjectId, newStudentId);
			console.log("assign " + newStudentId);
		} else if(oldStudentId != null && newStudentId == null) {
			unassignStudentToAffiliation(subjectId, oldStudentId);
			console.log("unassign " + oldStudentId);
		}
	}

	function assignStudentToAffiliation(subjectId, studentId){
		$.ajax({
			type : "GET",
			url : "AssignStudentSubjectServlet",
			data : "subjectId=" + subjectId + "&studentId=" + studentId,
			success : function(data) {
				console.log("assign subject " + subjectId + " to student " + studentId);
				alert("updated connection: assign subject id " + subjectId + " to student id " + studentId);
				location.reload();
			},
			error: function(res){
				alert("Failed to assign student to subject");
				location.reload();
			}
		});
	}

	function unassignStudentToAffiliation(subjectId, studentId){
		$.ajax({
			type : "GET",
			url : "UnassignStudentSubjectServlet",
			data : "subjectId=" + subjectId + "&studentId=" + studentId,
			success : function(data) {
				console.log("assign subject " + subjectId + " to student " + studentId);
				alert("updated connection: unassign subject id " + subjectId + " to student id " + studentId);
				location.reload();
			},
			error: function(res){
				alert("Failed to unassign student to subject");
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
				alert("updated program of subject id" + subjectId + " to program with id " + programId);
				location.reload();
			},
			error: function(res){
				alert("Failed to update subject program. Program and subject must have at least one common category!");
				location.reload();
			}
		});
	}

	function updateSubjectAdminValid(subjectId, sel){
		var valid = sel.checked;
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
		var valid = sel.checked;
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