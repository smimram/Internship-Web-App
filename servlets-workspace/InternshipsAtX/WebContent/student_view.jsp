<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"
    import="edu.polytechnique.inf553.Topic"%>
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
						Topic topic = (Topic)request.getAttribute("userTopic");
						if(topic!=null){
					%>
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-1"> Id </div>
								<div class="col col-3">Topic Title</div>
								<div class="col col-2">Supervisor Name</div>
								<div class="col col-1">Topic</div>
								<div class="col col-1">Fiche de Stage</div>
								<div class="col col-1">Report</div>
								<div class="col col-1">Slides</div>
								<div class="col col-1">Confidential internship</div>
							</li>
							<li class="table-row">
								<div class="col col-1" data-label="Id"><%=topic.getId()%></div> <!--  %>-->
								<div class="col col-3" data-label="Topic title"><%=topic.getTitle() %></div>
								<div class="col col-2" data-label="Supervisor Name" title="<%=topic.getSupervisorEmail()%>"><%=topic.getSupervisorName() %></div>
								<div class="col col-1" data-label="Topic">
									<button type="button" class="btn btn-secondary btn-sm"><a href="/download-topic?internshipId=<%=topic.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
								</div>
								<div class="col col-1" data-label="Fiche de stage">
									<form method="post" action="upload-fiche" enctype="multipart/form-data">
										<input name="topicId" value="<%=topic.getId()%>" hidden />
										<input name="userId" value="<%=user.getId()%>" hidden />
										<input id="fiche" type="file" name="fiche" accept="application/pdf" title="Please upload your fiche de stage in PDF format." onchange="this.form.submit()"/> <!-- onchange to avoid submit button -->
									</form>
									<div class="col col-1" data-label="Fiche">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/download-fiche?internshipId=<%=topic.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
									</div>
								</div>
								<div class="col col-1" data-label="Report">
									<form method="post" action="upload-report" enctype="multipart/form-data">
										<input name="topicId" value="<%=topic.getId()%>" hidden />
										<input name="userId" value="<%=user.getId()%>" hidden />
										<input id="report" type="file" name="report" accept="application/pdf" title="Please upload your report in PDF format." onchange="this.form.submit()"/>
									</form>
									<div class="col col-1" data-label="Report">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/download-report?internshipId=<%=topic.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
									</div>
								</div>
								<div class="col col-1" data-label="Slides">
									<form method="post" action="upload-slides" enctype="multipart/form-data">
										<input name="topicId" value="<%=topic.getId()%>" hidden />
										<input name="userId" value="<%=user.getId()%>" hidden />
										<input id="slides" type="file" name="slides" accept="application/pdf" title="Please upload your slides in PDF format." onchange="this.form.submit()"/>
									</form>
									<div class="col col-1" data-label="Slides">
										<button type="button" class="btn btn-secondary btn-sm"><a href="/download-slides?internshipId=<%=topic.getId() %>" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>
									</div>
								</div>
								<div class="col col-1" data-label="Confidential topic">
									<label class="switch">
										<input type="checkbox" disabled ${topic.isConfidentialInternship == true ? 'checked' : ''}>
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
						var categories_to_topics = new Map();
						
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
						
						<c:forEach items="${topicsPerCategory}" var="categoryAndTopics">
							var topics = [];
							<c:forEach items="${categoryAndTopics.getTopics()}" var="topic">
								topics.push({title: "${topic.getTitle()}", id: "${topic.getId()}", supervisorEmail: "${topic.getSupervisorEmail()}", supervisorName: "${topic.getSupervisorName()}"})
							</c:forEach>
							var categoryId = "${categoryAndTopics.getCategoryId()}";
							var programId = "${categoryAndTopics.getProgramId()}";
							
							if(categories_to_topics.has(programId)) {
								categories_to_topics.get(programId).set(categoryId, topics);
							} else {
								var new_categories_topics = new Map();
								new_categories_topics.set(categoryId, topics);
								categories_to_topics.set(programId, new_categories_topics);
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
									var topics = categories_to_topics.get(k).get(category.key);
									if(topics.length>0) {
										programList.innerHTML += '<div class="category", id="'+category.key+'">';					
										programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
										'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
										// New table for each category
										programList.innerHTML += '<ul class="responsive-table" id="'+k.concat(category.key)+'pctable">';
										var newRow = document.getElementById(k.concat(category.key).concat("pctable"));
										newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-3">Topic Title</div><div class="col col-3">Supervisor Name</div><div class="col col-3">Supervisor Email</div><div class="col col-1">Topic</div><div class="col col-1">Confidential internship</div></li>';
										for(const topic of topics) {
											var downloadForm = '<button type="button" class="btn btn-secondary btn-sm"><a href="/download-topic?internshipId='+topic.id+'" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button>';
											var newRowE = document.getElementById(k.concat(category.key).concat("pctable"));
											console.log(${topic.isConfidentialInternship()});
											newRowE.innerHTML += '<li class="table-row">' + 
											'<div class="col col-1" data-label="Id">' + topic.id + '</div>'+
											'<div class="col col-3" data-label="Topic Title">'+topic.title+'</div>'+
											'<div class="col col-3" data-label="Supervisor Name">'+topic.supervisorName + '</div>'+
											'<div class="col col-3" data-label="Supervisor Email">'+ topic.supervisorEmail +'</div>'+
											'<div class="col col-1" data-label="Topic">'+downloadForm+'</div>'+
											'<div class="col col-1" data-label="Confidential internship"><label class="switch"><input type="checkbox" disabled ${topic.isConfidentialInternship == true ? 'checked' : ''}><span class="slider round"></span></label></div>'+
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
