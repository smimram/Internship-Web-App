<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Topic management</title>
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
						<h1> Topic Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1">Id
									<a href="./topic-management?orderByColumn=id&orderBySort=ASC"><i class="fas fa-sort-numeric-down" title="sort by increasing order"></i></a>
									<a href="./topic-management?orderByColumn=id&orderBySort=DESC"><i class="fas fa-sort-numeric-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Title
									<a href="./topic-management?orderByColumn=title&orderBySort=ASC"><i class="fas fa-sort-alpha-down" title="sort by increasing order"></i></a>
									<a href="./topic-management?orderByColumn=title&orderBySort=DESC"><i class="fas fa-sort-alpha-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Categories</div>
								<div class="col col-1">Program</i></div>
								<div class="col col-1">Admin. validation
									<a href="./topic-management?orderByColumn=administr_validated&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a>
									<a href="./topic-management?orderByColumn=administr_validated&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-1">Scientific validation
									<a href="./topic-management?orderByColumn=scientific_validated&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a>
									<a href="./topic-management?orderByColumn=scientific_validated&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Attributed to 
								</div>
								<div class="col col-1">Confidential topic
								</div>
								<div class="col col-1">Actions</div>
							</li>
							
							<c:forEach items="${topics}" var="topic">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${topic.id}</div>
									<div class="col col-2" data-label="Title">${topic.title}</div>
									<div class="col col-2" data-label="Categories">
										<!-- update the categories of a topic -->
										<select class="mul-select" id="mul-select-category-${topic.id}" ${(user.role != "Assistant") ? '' : 'disabled'}  name="topics[]" multiple="multiple" data-pid= "${topic.id}">
											<c:forEach items="${categoriesForPrograms}" var="entry">
												<c:if test="${entry.key == topic.programId}">
													<c:forEach items="${categoriesForPrograms[entry.key]}" var="cat">
														<option value="${cat.id}">${cat.name}</option>
													</c:forEach>
												</c:if>
											</c:forEach>
										</select>
									</div>
									<div class="col col-1" data-label="Program">
										<!-- update the program of a topic -->
										<select id="select-program-${topic.id}" class="custom-select" name="role" ${(user.role != "Assistant") ? '' : 'disabled'}  onchange="updateTopicProgram(${topic.id}, this);">
											<c:forEach items="${programs}" var="program">
												<option value="${program.id}" ${topic.programId == program.id ? 'selected' : '' }>${program.name} - ${program.year}</option>
											</c:forEach>
										</select>
									</div>
									<div class="col col-1" data-label="AdminValidate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<label class="switch">
											<input type="checkbox" id="select-admin-valid-${topic.id}" onchange="updateTopicAdminValid(${topic.id}, this);" ${topic.adminValid ? 'checked' : ''}>
											<span class="slider round"></span>
										</label>
									</div>
									<div class="col col-1" data-label="SciValidate">
										<!-- update the valid status of a user -->
										<!-- need to select at least one program before validate a user -->
										<label class="switch">
											<input type="checkbox" id="select-sci-valid-${topic.id}" onchange="updateTopicSciValid(${topic.id}, this);" ${topic.sciValid ? 'checked' : ''}> <!-- ${(topic.adminValid && user.role != "Assistant") ? '' : 'disabled'}  -->
											<span class="slider round"></span>
										</label>
									</div>
									<div class="col col-2" data-label="AttributedTo">
										<c:choose>
											<c:when test="${topic.affiliatedStudent != null}"> <!-- if there is an affiliated student, display it -->
												${topic.affiliatedStudent.name}
												<br>
												<button type="button" class="btn btn-secondary btn-sm" onclick="unassignStudentToAffiliation(${topic.id},${topic.affiliatedStudent.id})"><i class="fas fa-trash" style="color: white"></i></button>
												<button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${topic.affiliatedStudent.email}')"><i class="fas fa-at" style="color: white"></i></button>
											</c:when>
											<c:otherwise> <!-- else display the list of students without internships -->
												<select class="custom-select" id="select-aff-student-topic-${topic.id}" name="assignedStudent" ${(user.role != "Assistant") ? '' : 'disabled'} onfocus="updateOldAffiliatedStudent(this)" onchange="updateTopicAffiliatedStudent(${topic.id}, this);">
													<option value="null" selected>No student</option>
													<c:forEach items="${studentsNoInternship}" var="studentNoInternship">
														<option value="${studentNoInternship.id}">${studentNoInternship.name}</option>
													</c:forEach>
												</select>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col col-1" data-label="confidentialTopic">
										<label class="switch">
											<input type="checkbox" disabled ${topic.isConfidentialInternship() ? 'checked' : ''}>
											<span class="slider round"></span>
										</label>
									</div>
									<div class="col col-1">
										<button type="button" class="btn btn-primary btn-sm"><a href="/download-topic?internshipId=${topic.id}" target="_blank" style="color: white">Download topic</a></button>
										<button type="button" class="btn btn-primary btn-sm" title="${(topic.dateFiche != null) ? topic.dateFiche : 'No file'}"><a href="/download-fiche?internshipId=${topic.id}" target="_blank" style="color: white">Download fiche de stage</a></button>
										<button type="button" class="btn btn-primary btn-sm" title="${(topic.dateReport != null) ? topic.dateReport : 'No file'}"><a href="/download-report?internshipId=${topic.id}" target="_blank" style="color: white">Download report</a></button>
										<button type="button" class="btn btn-primary btn-sm" title="${(topic.dateSlides != null) ? topic.dateSlides : 'No file'}"><a href="/download-slides?internshipId=${topic.id}" target="_blank" style="color: white">Download slides</a></button>
										<button type="button" class="btn btn-danger btn-sm" onclick="deleteTopic(${topic.id}, '${topic.title}');"><i class="fas fa-trash"></i></button>
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
		<c:forEach items="${topics}" var="topic">
			var valCategories = []
			<c:forEach items="${topic.getCategories()}" var="category">
				valCategories.push("${category.id}")
			</c:forEach>
			
			// initialize the multiple selection box for the topic categories
			// select the pre-selected programs for each users
			$('#mul-select-category-' + "${topic.id}").select2({
				placeholder:"-- select categories --"
			}).val(valCategories).trigger('change');
			
			// listen for the select event
			$('#mul-select-category-' + "${topic.id}").on('select2:select', function (e) {
				var categoryId = e.params.data.id;
				var topicId = ${topic.id}
				updateTopicCategory(topicId, categoryId, true)
			});
			
			// listen for the unselect event
			$('#mul-select-category-' + "${topic.id}").on('select2:unselect', function (e) {
				var categoryId = e.params.data.id;
				var topicId = ${topic.id}
				updateTopicCategory(topicId, categoryId, false)
			});

			// // initialize the multiple selection box for the unaffiliated students
			// // select the pre-selected progams for each users
			// var valStudents = []
			// <c:forEach items="${topics}" var="studentNoInternship">
			// 	valStudents.push("${studentNoInternship.id}")
			// </c:forEach>
			// console.log(valStudents);
			// $('#mul-select-student-' + "${topic.id}").select2({
			// 	placeholder:"-- no student affiliated --"
			// }).val(valStudents).trigger('change');
			
			// // // listen for the select event
			// $('#mul-select-student-' + "${topic.id}").on('select2:select', function (e) {
			//     var studentId = e.params.data.id;
			//     var topicId = ${topic.id}
			//     assignStudentToAffiliation(topicId, studentId)
			// });
			
			// // // listen for the unselect event
			// $('#mul-select-student-' + "${topic.id}").on('select2:unselect', function (e) {
			// 	var studentId = e.params.data.id;
			//     var topicId = ${topic.id}
			//     unassignStudentToAffiliation(topicId, studentId)
			// });
		</c:forEach>
	});

	function updateTopicCategory(topicId, categoryId, value){
		$.ajax({
			type : "GET",
			url : "UpdateTopicCategoryServlet",
			data : "topicId=" + topicId + "&categoryId=" + categoryId + "&select=" + value,
			success : function(data) {
				console.log("update topic " + topicId + ", category " + categoryId + " select " + value);
				if(value){
					// if the validate option was disabled(no program selected), remove it
					$('#select-valid-' + topicId).attr("disabled",false);
				}
				alert("updated connection topic id " + topicId + ", category id " + categoryId + " to " + value);
				location.reload();
			},
			error: function(res){
				alert("Failed to update topic category");
				location.reload();
			}
		});
	}

	function updateOldAffiliatedStudent(selection) {
		console.log("onfocus");
		oldAffiliatedStudent = selection.value;
		console.log(oldAffiliatedStudent);
	}

	function updateTopicAffiliatedStudent(topicId, newStudent) {
		if(oldAffiliatedStudent == "" || oldAffiliatedStudent == "null" || oldAffiliatedStudent == undefined || oldAffiliatedStudent == "undefined" ) {
			oldStudentId = null;
		} else {
			oldStudentId = oldAffiliatedStudent;
		}
		console.log("onchange");
		newStudentId = newStudent.value;

		console.log(oldStudentId + " ; " + newStudentId);
		if(oldStudentId != null && newStudentId != null) {
			unassignStudentToAffiliation(topicId, oldStudentId);
			assignStudentToAffiliation(topicId, newStudentId);
			console.log("unassign " + oldStudentId + " + assign " + newStudentId);
		} else if(oldStudentId == null && newStudentId != null) {
			assignStudentToAffiliation(topicId, newStudentId);
			console.log("assign " + newStudentId);
		} else if(oldStudentId != null && newStudentId == null) {
			unassignStudentToAffiliation(topicId, oldStudentId);
			console.log("unassign " + oldStudentId);
		}
	}

	function assignStudentToAffiliation(topicId, studentId){
		$.ajax({
			type : "GET",
			url : "AssignStudentTopicServlet",
			data : "topicId=" + topicId + "&studentId=" + studentId,
			success : function(data) {
				console.log("assign topic " + topicId + " to student " + studentId);
				alert("updated connection: assign topic id " + topicId + " to student id " + studentId);
				location.reload();
			},
			error: function(res){
				alert("Failed to assign student to topic");
				location.reload();
			}
		});
	}

	function unassignStudentToAffiliation(topicId, studentId){
		$.ajax({
			type : "GET",
			url : "UnassignStudentTopicServlet",
			data : "topicId=" + topicId + "&studentId=" + studentId,
			success : function(data) {
				console.log("assign topic " + topicId + " to student " + studentId);
				alert("updated connection: unassign topic id " + topicId + " to student id " + studentId);
				location.reload();
			},
			error: function(res){
				alert("Failed to unassign student to topic");
				location.reload();
			}
		});
	}

	function updateTopicProgram(topicId, sel){
		var programId = sel.value;
		$.ajax({
			type : "GET",
			url : "UpdateTopicProgramServlet",
			data : "topicId=" + topicId + "&programId=" + programId,
			success : function(data) {
				console.log("update topic " + topicId + " program into " + programId);
				alert("updated program of topic id" + topicId + " to program with id " + programId);
				location.reload();
			},
			error: function(res){
				alert("Failed to update topic program.");
				location.reload();
			}
		});
	}

	function updateTopicAdminValid(topicId, sel){
		var valid = sel.checked;
		$.ajax({
			type : "GET",
			url : "UpdateTopicAdminValidServlet",
			data : "topicId=" + topicId + "&valid=" + valid,
			success : function(data) {
				console.log("update topic " + topicId + " valid into " + valid);
				alert("updated topic id " + topicId + " admin valid to " + valid);
				location.reload();
			},
			error: function(res){
				alert("Failed to update topic admin valid");
				location.reload();
			}
		});
	}

	function updateTopicSciValid(topicId, sel){
		var valid = sel.checked;
		$.ajax({
			type : "GET",
			url : "UpdateTopicSciValidServlet",
			data : "topicId=" + topicId + "&valid=" + valid,
			success : function(data) {
				console.log("update topic " + topicId + " valid into " + valid);
				alert("updated topic id " + topicId + " scientific valid to " + valid);
				location.reload();
			},
			error: function(res){
				alert("Failed to update topic scientifical valid");
				location.reload();
			}
		});
	}

	function deleteTopic(id, title){
		var r = confirm("Are you sure you want to delete the topic " + title + " ?");
		if (r == true) {
			$.ajax({
				type : "GET",
				url : "DeleteTopicServlet",
				data : "topicId=" + id ,
				success : function(data) {
					console.log("delete topic " + title);
					alert("deleted topic " + title);
					location.reload();
				},
				error: function(res){
					alert("Failed to delete the topic" + title + ". Cannot delete topic if it is already assigned to a student!");
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
