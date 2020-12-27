<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"
    import="edu.polytechnique.inf553.Subject"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>Student View</title>
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
	<link rel="stylesheet" type="text/css" href="css/student_view.css">
	
<!--===============================================================================================-->
</head>
<body>

<%
Person user = (Person)session.getAttribute("user");
String name = user.getName();
String role = user.getRole();
%>

	<nav class="navbar navbar-dark bg-dark">
	  <div class="container-fluid justify-content-start">
	    <a class="navbar-brand" href="/InternshipsAtX/student-view">
	      <img src="images/logo.png" style="max-height: 35px;">
	      Internship Management
	    </a>
	    <div class="ml-auto d-flex">
	        <div class="nav-item dropdown">
	          <a class="text-white dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
	            <%=role %>: <%=name %>
	          </a>
	          <ul class="dropdown-menu" aria-labelledby="navbarDropdown" style="right:0;left:auto;">
	            <li><a class="dropdown-item" href="./LogoutServlet">Log out</a></li>
	          </ul>
	        </div>
	    </div>
	  </div>
	</nav>
	
	
	<%
		// show user internship if he has
		Subject userSubject = (Subject)request.getAttribute("userSubject");
		if(userSubject!=null){
			String userSubjectId = userSubject.getId();
			String userSubjectTitle = userSubject.getTitle();
			String userSubjectSupervisorName = userSubject.getSupervisorName();
			String userSubjectSupervisorEmail = userSubject.getSupervisorEmail();
			%>
			<div class="limiter">
				<div class="container-login100 background_style" style="min-height:auto;">
					<div class="wrap-login100-V2">
		
		
							<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
								<span class="login100-form-title">
									<h1>My Internship</h1>
								</span>
								<div class="text-center">
									<ul class="responsive-table">
										<li class="table-header">
											<div class="col col-1"> Id </div>
											<div class="col col-2">Subject Title</div>
											<div class="col col-3">Supervisor Name</div>
											<div class="col col-4">Supervisor Email</div>
											<div class="col col-5">Subject</div>
										</li>
										<li class="table-row">
											<div class="col col-1" data-label="Id"><%=userSubjectId %></div>
											<div class="col col-2" data-label="Subject Title"><%=userSubjectTitle %></div>
											<div class="col col-3" data-label="Supervisor Name"><%=userSubjectSupervisorName %></div>
											<div class="col col-4" data-label="Supervisor Email"><%=userSubjectSupervisorEmail %></div>
											<div class="col col-5" data-label="Subject">
												<a href="downloadsubject?internshipId=<%=userSubjectId %>" target="_blank">Download</a>
											</div>
										</li>
									</ul>
								</div>
							</form>
						
					</div>
				</div>
			</div>			
			<%
		}
	%>
	

	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">

				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						Available Internships
					</span>
			
				<div class="wrap-input100 validate-input m-b-16" data-validate = "Filter on the programs">
					<select name="programs" id="programs" class="input100">
				    	<option value="0">All Programs</option>
					</select>
				</div>	
					
				<div class="container" id="list">
					  
				</div>
				<h1 class="easter-egg" style="visibility:hidden; font-size:0;">Remi Delacourt was here</h1> <!-- An easter-egg ! -->
					
					<script>
		//Loading the data
		var programs_categories = new Map();
		var program_name_id = new Map();
		var categories_to_subjects = new Map();
		
		<c:forEach items="${programs}" var="program">
			var program_categories = [];
		    <c:forEach items="${program.getCategories()}" var="category">
			    program_categories.push({
			   		 key: "${category.getId()}",
			   		 value: "${category.getName()}"
			   	 });
		    </c:forEach>
		    program_name_id.set("${program.getId()}", "${program.getName()}" + " - " + "${program.getYear()}");
		    programs_categories.set("${program.getId()}", program_categories);
		</c:forEach>
		
		<c:forEach items="${subjectsPerCategory}" var="categoryAndSubjects">
			var subjects = [];
			<c:forEach items="${categoryAndSubjects.getSubjects()}" var="subject">
			 	subjects.push({title: "${subject.getTitle()}", id: "${subject.getId()}", supervisorEmail: "${subject.getSupervisorEmail()}", supervisorName: "${subject.getSupervisorName()}"})
			</c:forEach>
			var categoryId = "${categoryAndSubjects.getCategoryId()}";
			var programId = "${categoryAndSubjects.getProgramId()}";
			
			if(categories_to_subjects.has(programId)) {
				categories_to_subjects.get(programId).set(categoryId, subjects);
			} else {
				var new_categories_subjects = new Map();
				new_categories_subjects.set(categoryId, subjects);
				categories_to_subjects.set(programId, new_categories_subjects);
			}
		</c:forEach>
		
		
		window.onload = function() {
			showAllPrograms();
			
			var programSel = document.getElementById("programs");
			programs_categories.forEach((value, key) => {
				programSel.options[programSel.options.length] = new Option(program_name_id.get(key), key);
			});
			programSel.onchange = function(){
				var pId = this.value
				if(pId==="0") {
					//Print all progams with their categories
					showAllPrograms();
				} else {
					//Print certain program (based on pId)
					showProgram(pId)
				}
			}
			
		}
		
		function showAllPrograms() {
			var programList = document.getElementById('list');
			programList.innerHTML = '';
			
			programs_categories.forEach((v, k) => {
				programList.innerHTML += '<div class="program", id="'+k+'">'; 
				programList.innerHTML += '<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">'+
											'<h2 class="login100-form-btn-V2 p-l-5 p-r-5">' + program_name_id.get(k) + '</h2></div>';
				
				
				for (const category of v) {
					
					var subjects = categories_to_subjects.get(k).get(category.key);
					if(subjects.length>0) {
						
						programList.innerHTML += '<div class="category", id="'+category.key+'">';					
						programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
						'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
						
						//New table for each category
						programList.innerHTML += '<ul class="responsive-table" id="'+k.concat(category.key)+'pctable">';
						
						var newRow = document.getElementById(k.concat(category.key).concat("pctable"));
						
						
						newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-2">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-4">Supervisor Email</div><div class="col col-5">Subject</div></li>';		
						
						for(const subject of subjects) {
							var downloadForm = '<a href="downloadsubject?internshipId='+subject.id+'" target="_blank">Download</a>';
							var newRowE = document.getElementById(k.concat(category.key).concat("pctable"));
							newRowE.innerHTML += '<li class="table-row"><div class="col col-1" data-label="Id">' + subject.id + '</div>'+
															'<div class="col col-2" data-label="Subject Title">'+subject.title+'</div>'+
															'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
															'<div class="col col-4" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
															'<div class="col col-5" data-label="Subject">'+downloadForm+'</div></li>';
						}
						programList.innerHTML += '</ul>'; 
					}
					
					programList.innerHTML += '</div>';
				}
				programList.innerHTML += '</div>';
			});
		}
		
		function showProgram(pId) {
			var programList = document.getElementById('list');
			programList.innerHTML = '<div class="program", id="'+pId+'">'; 
			programList.innerHTML += '<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">'+
										'<h2 class="login100-form-btn-V2 p-l-5 p-r-5">' + program_name_id.get(pId) + '</h2></div>';
			
			
			for (const category of programs_categories.get(pId)) {
				
				var subjects = categories_to_subjects.get(pId).get(category.key);
				if(subjects.length>0) {
					
					programList.innerHTML += '<div class="category", id="'+category.key+'">';					
					programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
					'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
					
					//New table for each category
					programList.innerHTML += '<ul class="responsive-table" id="'+pId.concat(category.key)+'pctable">';
					
					var newRow = document.getElementById(pId.concat(category.key).concat("pctable"));
					
					
					newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-2">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-4">Supervisor Email</div><div class="col col-5">Subject</div></li>';		
					
					for(const subject of subjects) {
						var downloadForm = '<a href="downloadsubject?internshipId='+subject.id+'" target="_blank">Download</a>';
						var newRowE = document.getElementById(pId.concat(category.key).concat("pctable"));
						newRowE.innerHTML += '<li class="table-row"><div class="col col-1" data-label="Id">' + subject.id + '</div>'+
														'<div class="col col-2" data-label="Subject Title">'+subject.title+'</div>'+
														'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
														'<div class="col col-4" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
														'<div class="col col-5" data-label="Subject">'+downloadForm+'</div></li>';
					}
					programList.innerHTML += '</ul>'; 
				}
				
				programList.innerHTML += '</div>';
			}
			programList.innerHTML += '</div>';
		}
		
		</script>		
					
					
					
					
				</form>
				
			</div>
		</div>
	</div>
	
	
							
	
<!-- Bootstrap -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
</body>
</html>