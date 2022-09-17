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
						<h1> User Management </h1>
					</span>
					
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1"> Id </div>
								<div class="col col-3">Name</div>
								<div class="col col-2">Role</div>
								<div class="col col-4">Program</div>
								<div class="col col-2">Validate</div>
							</li>
							
							<c:forEach items="${persons}" var="person">
								<li class="table-row">
									<div class="col col-1" data-label="Id">${person.id}</div>
									<div class="col col-3" data-label="Name">${person.name}</br><button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${person.email}')"><i class="fas fa-at" style="color: white"></i></button></div>
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
										<%-- <select id="select-valid-${person.id}" class="custom-select" ${person.programSize() == 0 ? 'disabled' : ''} onchange="updateUserValid(${person.id}, this);"> --%>
										  <%-- <option value="true" ${person.valid ? 'selected' : ''}>Valid</option> --%>
										  <%-- <option value="false" ${person.valid ? '' : 'selected'}>Invalid</option> --%>
										<%-- </select> --%>
										<label class="switch">
											<input type="checkbox" id="select-valid-${person.id}" onchange="updateUserValid(${person.id}, this);"" ${person.valid ? 'checked' : ''}> <!-- ${(topic.adminValid && user.role != "Assistant") ? '' : 'disabled'}  -->
											<span class="slider round"></span>
										</label>
									</div>
								</li>
							</c:forEach>
							
						</ul>
					</div>
				</div>
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
        	if(select){
    		    // if the validate option was disabled(no program selected), remove it
    		    $('#select-valid-' + pid).attr("disabled",false);
        	}
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
	var valid = sel.checked;
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

function displayEmail(email) {
	// console.log(decodeURIComponent(email));
	// alert("Email: " + decodeURIComponent(email));
	alert("Email: " + email);
}
</script>

</body>
</html>