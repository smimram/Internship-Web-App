<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Defense management</title>
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
						<h1> Defense Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1">Id
									<a href="./defense-management?orderByColumn=id&orderBySort=ASC"><i class="fas fa-sort-numeric-down" title="sort by increasing order"></i></a>
									<a href="./defense-management?orderByColumn=id&orderBySort=DESC"><i class="fas fa-sort-numeric-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Date
									<a href="./defense-management?orderByColumn=date&orderBySort=ASC"><i class="fas fa-sort-alpha-down" title="sort by increasing order"></i></a>
									<a href="./defense-management?orderByColumn=date&orderBySort=DESC"><i class="fas fa-sort-alpha-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Time
									<a href="./defense-management?orderByColumn=time&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a>
									<a href="./defense-management?orderByColumn=time&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a>
								</div>
								<div class="col col-2">Referent
									<%-- <a href="./defense-management?orderByColumn=referent_id&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a> --%>
									<%-- <a href="./defense-management?orderByColumn=referent_id&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a> --%>
								</div>
								<div class="col col-2">Jury 2
									<%-- <a href="./defense-management?orderByColumn=jury2_id&orderBySort=ASC"><i class="fas fa-sort-amount-down" title="sort by increasing order"></i></a> --%>
									<%-- <a href="./defense-management?orderByColumn=jury2_id&orderBySort=DESC"><i class="fas fa-sort-amount-down-alt" title="sort by decreasing order"></i></a> --%>
								</div>
								<div class="col col-2">Student
								</div>
								<div class="col col-1">Actions</div>
							</li>
							
							<c:forEach items="${defenses}" var="defense">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${defense.id}</div>
									<div class="col col-2" data-label="Date">
										<input id="date-picker-defense-${defense.id}" type="date" value="${defense.date}" onchange="updateDefense(${defense.id}, $(this).val(), $('#time-picker-defense-${defense.id}').val())">
									</div>
									<div class="col col-2" data-label="Time">
										<input id="time-picker-defense-${defense.id}" type="time" value="${defense.time}" onchange="updateDefense(${defense.id}, $('#date-picker-defense-${defense.id}').val(), $(this).val())">
									</div>
									<div class="col col-2" data-label="Referent">
										<c:choose>
											<c:when test="${empty defense.referent.name}">
												<select class="custom-select" id="select-referent-defense-${defense.id}" name="referent" onchange="updateReferent(${defense.id}, $(this).val());">
													<option value="null" selected>No referent</option>
													<c:forEach items="${professors}" var="professor">
														<option value="${professor.id}">${professor.name}</option>
													</c:forEach>
												</select>
											</c:when>    
											<c:otherwise>
												${defense.referent.name}
												</br>
												<button type="button" class="btn btn-secondary btn-sm" onclick="deleteReferent(${defense.id});"><i class="fas fa-trash"></i></button>
												<button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${defense.referent.email}')"><i class="fas fa-at"></i></button>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col col-2" data-label="Jury2">
										<c:choose>
											<c:when test="${empty defense.jury2.name}">
												<select class="custom-select" id="select-jury2-defense-${defense.id}" name="jury2" onchange="updateJury2(${defense.id}, $(this).val());">
													<option value="null" selected>No jury 2</option>
													<c:forEach items="${professors}" var="professor">
														<option value="${professor.id}">${professor.name}</option>
													</c:forEach>
												</select>
											</c:when>    
											<c:otherwise>
												${defense.jury2.name}
												</br>
												<button type="button" class="btn btn-secondary btn-sm" onclick="deleteJury2(${defense.id});"><i class="fas fa-trash"></i></button>
												<button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${defense.jury2.email}');"><i class="fas fa-at"></i></button>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col col-2" data-label="Student">
										<c:choose>
											<c:when test="${empty defense.student.name}">
												<select class="custom-select" id="select-student-defense-${defense.id}" name="student" onchange="updateStudent(${defense.id}, $(this).val());">
													<option value="null" selected>No student</option>
													<c:forEach items="${students}" var="student">
														<option value="${student.id}">${student.name}</option>
													</c:forEach>
												</select>
											</c:when>    
											<c:otherwise>
												${defense.student.name}
												</br>
												<button type="button" class="btn btn-secondary btn-sm" onclick="deleteStudent(${defense.id});"><i class="fas fa-trash"></i></button>
												<button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${defense.student.email}');"><i class="fas fa-at"></i></button>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="col col-1">
										<button type="button" class="btn btn-secondary btn-sm" onclick="deleteDefense(${defense.id});"><i class="fas fa-trash"></i></button>
									</div>
								</li>
							</c:forEach> 
						</ul>
					</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Create a new defense -->
	<div class="limiter">
		<div class="container-login100 background_style" style="min-height:60vh;">
			<div class="wrap-login100-V2">
				<div class="login100-form validate-form p-l-55 p-r-55 p-t-178 p-b-40">
					<span class="login100-form-title">
						<h1> Create a defense </h1>
					</span>

					<!-- form to create new defense -->
					<form class="create-defense m-t-10 text-center" onsubmit="createDefense();">
						<div class="form-group">
							<label for="newDefenseDate" class="mr-3">Date: </label>
						    <input type="date" class="form-control w-50 d-inline" id="newDefenseDate">
						</div>
						<div class="form-group">
							<label for="newDefenseTime" class="mr-3">Time: </label>
						    <input type="time" class="form-control w-50 d-inline" id="newDefenseTime">
						</div>
						<div class="form-group">
							<label for="newDefenseStudent" class="mr-3">Student: </label>
							<select class="custom-select" id="newDefenseStudent" name="student">
								<option value="null" selected>No student</option>
								<c:forEach items="${students}" var="student">
									<option value="${student.id}">${student.name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
						  	<label for="newDefenseReferent" class="mr-3">Referent: </label>
						    <select class="custom-select" id="newDefenseReferent" name="referent">
								<option value="null" selected>No referent</option>
								<c:forEach items="${professors}" var="professor">
									<option value="${professor.id}">${professor.name}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
						  	<label for="newDefenseJury2" class="mr-3">Jury 2: </label>
						    <select class="custom-select" id="newDefenseJury2" name="jury2">
								<option value="null" selected>No jury 2</option>
								<c:forEach items="${professors}" var="professor">
									<option value="${professor.id}">${professor.name}</option>
								</c:forEach>
							</select>
						</div>
					  <button type="button" class="btn btn-primary btn-sm" type="submit" onclick="createDefense();">Create defense</button>
					</form>
					
				</div>
			</div>
		</div>
	</div>
<script>
	function updateDefense(defenseId, date, time){
		console.log(date)
		console.log(time)
		if(date == "" || date == undefined) {
			date = "NULL";
		}
		if(time == "" || time == undefined) {
			time = "NULL";
		}
		$.ajax({
			type : "GET",
			url : "UpdateDefenseServlet",
			data : "defenseId=" + defenseId + "&defenseDate=" + date + "&defenseTime=" + time,
			success : function(data) {
				console.log("update defense " + defenseId + " with date " + date + " and time " + time);
				alert("update defense " + defenseId + " with date " + date + " and time " + time);
				location.reload();
				// $("#date-picker-defense-1").val(time);
			},
			error: function(res){
				alert("Failed to update defense"); // TODO vider
				// location.reload();
			}
		});
	}

	function updateStudent(defenseId, studentId){
		$.ajax({
			type : "GET",
			url : "UpdatePersonDefenseServlet",
			data : "defenseId=" + defenseId + "&studentId="+studentId+"&referentId=SAME&jury2Id=SAME",
			success : function(data) {
				location.reload();
			},
			error: function(res){
				alert("Failed to update the student of defense " + defenseId);
			}
		});
	}

	function deleteStudent(defenseId){
		$.ajax({
			type : "GET",
			url : "UpdatePersonDefenseServlet",
			data : "defenseId=" + defenseId + "&studentId=NULL&referentId=SAME&jury2Id=SAME",
			success : function(data) {
				location.reload();
			},
			error: function(res){
				alert("Failed to delete the student of defense " + defenseId);
			}
		});
	}

	function updateReferent(defenseId, referentId){
		$.ajax({
			type : "GET",
			url : "UpdatePersonDefenseServlet",
			data : "defenseId=" + defenseId + "&studentId=SAME&referentId="+referentId+"&jury2Id=SAME",
			success : function(data) {
				location.reload();
			},
			error: function(res){
				alert("Failed to update the referent of defense " + defenseId);
			}
		});
	}

	function deleteReferent(defenseId){
		$.ajax({
			type : "GET",
			url : "UpdatePersonDefenseServlet",
			data : "defenseId=" + defenseId + "&studentId=SAME&referentId=NULL&jury2Id=SAME",
			success : function(data) {
				location.reload();
			},
			error: function(res){
				alert("Failed to delete the referent of defense " + defenseId);
			}
		});
	}

	function updateJury2(defenseId, jury2Id){
		$.ajax({
			type : "GET",
			url : "UpdatePersonDefenseServlet",
			data : "defenseId=" + defenseId + "&studentId=SAME&referentId=SAME&jury2Id="+jury2Id,
			success : function(data) {
				location.reload();
			},
			error: function(res){
				alert("Failed to update the jury 2 of defense " + defenseId);
			}
		});
	}

	function deleteJury2(defenseId){
		$.ajax({
			type : "GET",
			url : "UpdatePersonDefenseServlet",
			data : "defenseId=" + defenseId + "&studentId=SAME&referentId=SAME&jury2Id=NULL",
			success : function(data) {
				location.reload();
			},
			error: function(res){
				alert("Failed to delete the jury 2 of defense " + defenseId);
			}
		});
	}

	function deleteDefense(defenseId){
		var r = confirm("Are you sure you want to delete the defense "+defenseId+"?");
		if (r == true) {
			$.ajax({
				type : "GET",
				url : "DeleteDefenseServlet",
				data : "defenseId=" + defenseId ,
				success : function(data) {
					console.log("delete defense " + defenseId);
					alert("deleted defense " + defenseId);
					location.reload();
				},
				error: function(res){
					alert("Failed to delete the defense scheduled on "+defenseDate+" at "+defenseTime);
				}
			});
		}
	}

	function displayEmail(email) {
		alert("Email: " + email);
	}

	function createDefense(){
		console.log("creating a new defense");
		var newDate = $("#newDefenseDate").val();
		$("#newDefenseDate").val("");
		console.log(newDate);
		var newTime = $("#newDefenseTime").val();
		$("#newDefenseTime").val("");
		console.log(newTime);
		var referentId = $("#newDefenseReferent").val();
		$("#newDefenseReferent").val("");
		console.log(referentId);
		var jury2Id = $("#newDefenseJury2").val();
		$("#newDefenseJury2").val("");
		console.log(jury2Id);
		var studentId = $("#newDefenseStudent").val();
		$("#newDefenseStudent").val("");
		console.log(studentId);
		// TODO 
		// if(cName.trim() == ''){
			// alert('Please enter a category name');
		// }else{
			$.ajax({
				type : "GET",
				url : "CreateDefenseServlet",
				data : "defenseDate=" + newDate+"&defenseTime="+newTime+"&referentId="+referentId+"&jury2Id="+jury2Id+"&studentId="+studentId,
				success : function(data) {
					console.log("created new defense for student "+studentId);
					// reload the new data
					location.reload();
				},
				error: function(res){
					alert("Failed to create new defense");
				}
			});
		// }
	}
</script>
</body>
</html>