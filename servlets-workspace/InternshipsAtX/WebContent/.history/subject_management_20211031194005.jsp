<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Subject management</title>
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
/* icons */
.fas {
	color: white;
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
						<h1> Subject Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1">Id
									<a href="./subject-management?orderByColumn=id&orderBySort=ASC"><i class="fas fa-sort-numeric-down" title="sort by increasing order"></i></a>
									<a href="./subject-management?orderByColumn=id&orderBySort=DESC"><i class="fas fa-sort-numeric-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Title
									<a href="./subject-management?orderByColumn=title&orderBySort=ASC"><i class="fas fa-sort-alpha-down" title="sort by increasing order"></i></a>
									<a href="./subject-management?orderByColumn=title&orderBySort=DESC"><i class="fas fa-sort-alpha-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Categories</div>
								<div class="col col-1">Program</i></div>
								<div class="col col-1">Admin. validation
									<a href="./subject-management?orderByColumn=administr_validated&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a>
									<a href="./subject-management?orderByColumn=administr_validated&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-1">Scientific validation
									<a href="./subject-management?orderByColumn=scientific_validated&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a>
									<a href="./subject-management?orderByColumn=scientific_validated&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Attributed to 
								</div>
								<div class="col col-1">Confidential subject
								</div>
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
									<div class="col col-1" data-label="Program">
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
												<br>
												<button type="button" class="btn btn-secondary btn-sm" onclick="unassignStudentToAffiliation(${subject.id},${subject.affiliatedStudent.id})"><i class="fas fa-trash" style="color: white"></i></button>
												<button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${subject.affiliatedStudent.email}')"><i class="fas fa-at" style="color: white"></i></button>
											</c:when>
											<c:otherwise> <!-- else display the list of students without internships -->
												<select class="custom-select" id="select-aff-student-subject-${subject.id}" name="assignedStudent" ${(user.role != "Assistant") ? '' : 'disabled'} onfocus="updateOldAffiliatedStudent(this)" onchange="updateSubjectAffiliatedStudent(${subject.id}, this);">
													<option value="null" selected>No student</option>
													<c:forEach items="${studentsNoInternship}" var="studentNoInternship">
														<option value="${studentNoInternship.id}">${studentNoInternship.name}</option>
													</c:forEach>
												</select>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col col-1" data-label="confidentialSubject">
										<label class="switch">
											<input type="checkbox" disabled ${subject.isConfidentialInternship() ? 'checked' : ''}>
											<span class="slider round"></span>
										</label>
									</div>
									<div class="col col-1">
										<button type="button" class="btn btn-primary btn-sm" style="color: white;"><a href="/InternshipsAtX/download-subject?internshipId=${subject.id}" target="_blank">Download subject</a></button>
										<button type="button" class="btn btn-primary btn-sm" style="color: white;"><a href="/InternshipsAtX/download-fiche?internshipId=${subject.id}" target="_blank">Download fiche de stage</a></button>
										<button type="button" class="btn btn-primary btn-sm" style="color: white;"><a href="/InternshipsAtX/download-report?internshipId=${subject.id}" target="_blank">Download report</a></button>
										<button type="button" class="btn btn-primary btn-sm" style="color: white;"><a href="/InternshipsAtX/download-slides?internshipId=${subject.id}" target="_blank">Download slides</a></button>
										<button type="button" class="btn btn-danger btn-sm" onclick="deleteSubject(${subject.id}, '${subject.title}');"><i class="fas fa-trash"></i></button>
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

	function displayEmail(email) {
		// console.log(decodeURIComponent(email));
		// alert("Email: " + decodeURIComponent(email));
		alert("Email: " + email);
	}
</script>
</body>
</html>