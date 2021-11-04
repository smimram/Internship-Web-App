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
				<div class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1> Student list by program </h1>
					</span>
			
					<div class="wrap-input100 validate-input m-b-16" data-validate = "Filter on the programs">
						<select name="programs" id="programs" class="input100">
							<option value="0">All Programs</option>
						</select>
					</div>	
						
					<div class="container" id="list">
						
					</div>
						
					<script>
						//Loading the data
						<c:forEach items="${programs}" var="program">
							var studentsInProgram = [];
							<c:forEach items="${program.getStudents()}" var="student">
								studentsInProgram.push({"id": ${student.getId()}, "name": ${student.getName()}, "email": ${student.getEmail()}, "hasInternship": ${student.hasInternship()}});
							</c:forEach>
						
							var programList = document.getElementById('list');
							programList.innerHTML = '';
							programList.innerHTML += "<p style='font-weight: bold;'>${program.getName()}</p>";
							programList.innerHTML += "<ul>";
							console.log(studentsInProgram);
							for(var i = 0 ; i < studentsInProgram.length ; i++) {
								console.log(studentsInProgram[i]);
								programList.innerHTML += "<li>"+studentsInProgram[i]["name"]+"("+studentsInProgram[i]["id"]+"): "+ studentsInProgram[i]["email"] + " / " + studentsInProgram[i]["hasInternship"] + "</li>";
							}
							programList.innerHTML += "</ul>";
							// programList.innerHTML = '';
							// programList.innerHTML += '<div class="program", id="'+k+'">'; 
							// programList.innerHTML += '<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">'+
							// '<h2 class="login100-form-btn-V2 p-l-5 p-r-5">' + ${program} + '</h2></div>';
							// var students = categories_to_subjects.get(k).get(category.key);
							// if(subjects.length>0) {
							// 	programList.innerHTML += '<div class="category", id="'+category.key+'">';					
							// 	programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
							// 	'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
							// 	// New table for each category
							// 	programList.innerHTML += '<ul class="responsive-table" id="'+k.concat(category.key)+'pctable">';
							// 	var newRow = document.getElementById(k.concat(category.key).concat("pctable"));
							// 	newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-3">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-3">Supervisor Email</div><div class="col col-1">Subject</div><div class="col col-1">Confidential internship</div></li>';
							// 	studentsInProgram.forEach((v, k) => {
							// 			var newRowE = document.getElementById(k.concat(category.key).concat("pctable"));
							// 			console.log(${subject.isConfidentialInternship()});
							// 			newRowE.innerHTML += '<li class="table-row">' + 
							// 			'<div class="col col-1" data-label="Id">' + subject.id + '</div>'+
							// 			'<div class="col col-3" data-label="Subject Title">'+subject.title+'</div>'+
							// 			'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
							// 			'<div class="col col-3" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
							// 			'<div class="col col-1" data-label="Subject">'+downloadForm+'</div>'+
							// 			'<div class="col col-1" data-label="Confidential internship"><label class="switch"><input type="checkbox" disabled ${subject.isConfidentialInternship == true ? 'checked' : ''}><span class="slider round"></span></label></div>'+
							// 			'</li>';
							// 		programList.innerHTML += '</ul>'; 
							// 		programList.innerHTML += '</div>';
							// 	});
							// }
							// programList.innerHTML += '</div>';
						</c:forEach>
					</script>
				</div>
			</div>
		</div>
	</div>
</body>
</html>