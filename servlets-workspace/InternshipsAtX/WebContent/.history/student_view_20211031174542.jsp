<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"
    import="edu.polytechnique.inf553.Subject"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>Home</title>
	<%@ include file="meta.jsp" %>
</head>
<body>

	<%
	Person user = (Person)session.getAttribute("user");
	String name = user.getName();
	String role = user.getRole();
	%>

	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	
	<div class="limiter">
		<div class="container-login100 background_style" style="min-height:auto;">
			<div class="wrap-login100-V2">
				<div class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1>My Internship</h1>
					</span>
					<%
						// show user internship if he has
						Subject subject = (Subject)request.getAttribute("userSubject");
						if(subject!=null){
					%>
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1"> Id </div>
								<div class="col col-2">Subject Title</div>
								<div class="col col-2">Supervisor Name</div>
								<div class="col col-1">Download Subject</div>
								<div class="col col-1">Upload Fiche de Stage</div>
								<div class="col col-1">Upload Report</div>
								<div class="col col-1">Upload Slides</div>
								<div class="col col-1">Confidential internship</div>
								<div class="col col-1">Confidential report</div>
							</li>
							<li class="table-row">
								<div class="col col-1" data-label="Id"><%=subject.getId() %></div>
								<div class="col col-2" data-label="Subject Title"><%=subject.getTitle() %></div>
								<div class="col col-2" data-label="Supervisor Name" title="<%=subject.getSupervisorEmail()%>"><%=subject.getSupervisorName() %></div>
								<div class="col col-1" data-label="Subject">
									<button type="button" class="btn btn-secondary btn-sm"><a href="/InternshipsAtX/download-subject?internshipId=<%=subject.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
								</div>
								<div class="col col-1" data-label="Fiche de stage">
									<form method="post" action="upload-fiche" enctype="multipart/form-data">
										<input name="subjectId" value="<%=subject.getId()%>" hidden />
										<input name="userId" value="<%=user.getId()%>" hidden />
										<input id="fiche" type="file" name="fiche" accept="application/pdf" title="Please upload your fiche de stage in PDF format."/>
										<button type=submit>Upload</button>
									</form>
									<div class="col col-1" data-label="Subject">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/InternshipsAtX/download-fiche?internshipId=<%=subject.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
									</div>
								</div>
								<div class="col col-1" data-label="Report">
									<form method="post" action="upload-fiche" enctype="multipart/form-data">
										<input name="subjectId" value="<%=subject.getId()%>" hidden />
										<input name="userId" value="<%=user.getId()%>" hidden />
										<input id="report" type="file" name="report" accept="application/pdf" title="Please upload your report in PDF format."/>
										<%-- <button type=submit>Upload</button> --%>
									</form>
									<div class="col col-1" data-label="Report">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/InternshipsAtX/download-report?internshipId=<%=subject.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
									</div>
								</div>
								<div class="col col-1" data-label="Slides">
									<form method="post" action="upload-slides" enctype="multipart/form-data">
										<input name="subjectId" value="<%=subject.getId()%>" hidden />
										<input name="userId" value="<%=user.getId()%>" hidden />
										<input id="slides" type="file" name="fiche" accept="application/pdf" title="Please upload your slides in PDF format."/>
										<button type=submit>Upload</button>
									</form>
									<div class="col col-1" data-label="Slides">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/InternshipsAtX/download-slides?internshipId=<%=subject.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
									</div>
								</div>
								<div class="col col-1" data-label="Confidential subject">
									<label class="switch">
										<input type="checkbox" disabled ${subject.isConfidentialInternship == true ? 'checked' : ''}>
										<span class="slider round"></span>
									</label>
								</div>
								<div class="col col-1" data-label="Confidential report">
									<label class="switch">
										<input type="checkbox" disabled ${subject.isConfidentialReport == true ? 'checked' : ''}>
										<span class="slider round"></span>
									</label>
								</div>
							</li>
						</ul>
					</div>
					<%
						} else {
					%>
					<p style="text-align: center; font-size: 2em;"> No internship. </p>
					<%
						}
					%>
				</div>
			</div>
		</div>
	</div>			
			
	
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
						
					<script>
						//Loading the data
						var programs_categories = new Map();
						var program_name_id = new Map();
						var categories_to_subjects = new Map();
						
						<c:forEach items="${programs}" var="program">
							var categoriesOfProgram = [];
							<c:forEach items="${program.getCategories()}" var="category">
								categoriesOfProgram.push({
									key: "${category.getId()}",
									value: "${category.getName()}"
								});
							</c:forEach>
							program_name_id.set("${program.getId()}", "${program.getName()}" + " - " + "${program.getYear()}");
							programs_categories.set("${program.getId()}", categoriesOfProgram);
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
							showProgram(-1);
							
							var programSel = document.getElementById("programs");
							programs_categories.forEach((value, key) => {
								programSel.options[programSel.options.length] = new Option(program_name_id.get(key), key);
							});
							programSel.onchange = function(){
								var pId = this.value
								if(pId==="0") {
									//Print all progams with their categories
									showProgram(-1);
								} else {
									//Print certain program (based on pId)
									showProgram(pId)
								}
							}
							
						}
						
						function showProgram(pId) {
							if(pId === -1) {
								// show all programs
								programs_categories2 = programs_categories;
							} else {
								// show the specified program
								programs_categories2 = new Map();
								programs_categories2.set(pId, programs_categories.get(pId));
							}

							var programList = document.getElementById('list');
							programList.innerHTML = '';
							programs_categories2.forEach((v, k) => {
								programList.innerHTML += '<div class="program", id="'+k+'">'; 
								programList.innerHTML += '<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">'+
								'<h2 class="login100-form-btn-V2 p-l-5 p-r-5">' + program_name_id.get(k) + '</h2></div>';
								for (const category of v) {
									var subjects = categories_to_subjects.get(k).get(category.key);
									if(subjects.length>0) {
										programList.innerHTML += '<div class="category", id="'+category.key+'">';					
										programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
										'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
										// New table for each category
										programList.innerHTML += '<ul class="responsive-table" id="'+k.concat(category.key)+'pctable">';
										var newRow = document.getElementById(k.concat(category.key).concat("pctable"));
										newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-3">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-3">Supervisor Email</div><div class="col col-1">Subject</div><div class="col col-1">Confidential internship</div></li>';
										for(const subject of subjects) {
											var downloadForm = '<button type="button" class="btn btn-secondary btn-sm"><a href="/InternshipsAtX/download-subject?internshipId='+subject.id+'" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>';
											var newRowE = document.getElementById(k.concat(category.key).concat("pctable"));
											console.log(${subject.isConfidentialInternship()});
											newRowE.innerHTML += '<li class="table-row">' + 
											'<div class="col col-1" data-label="Id">' + subject.id + '</div>'+
											'<div class="col col-3" data-label="Subject Title">'+subject.title+'</div>'+
											'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
											'<div class="col col-3" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
											'<div class="col col-1" data-label="Subject">'+downloadForm+'</div>'+
											'<div class="col col-1" data-label="Confidential internship"><label class="switch"><input type="checkbox" disabled ${subject.isConfidentialInternship == true ? 'checked' : ''}><span class="slider round"></span></label></div>'+
											'</li>';
										}
										programList.innerHTML += '</ul>'; 
									}
									programList.innerHTML += '</div>';
								}
								programList.innerHTML += '</div>';
							});
						}
					</script>
				</form>
			</div>
		</div>
	</div>	
</body>
</html>
