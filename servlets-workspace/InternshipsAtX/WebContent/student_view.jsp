<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="edu.polytechnique.inf553.Person"
    import="edu.polytechnique.inf553.Topic" import="edu.polytechnique.inf553.Defense"%>
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
						<h1>My internship</h1>
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
		<div class="container-login100 background_style" style="min-height:auto;">
			<div class="wrap-login100-V2">
				<div class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						<h1>My defense</h1>
					</span>
					<%
						// show user internship if he has
						Defense defense = (Defense)request.getAttribute("studentDefense");
						if(defense != null){
					%>
					<div class="text-center">
						<ul class="responsive-table">
							<li class="table-header">
								<div class="col col-3">Date</div>
								<div class="col col-3">Time</div>
								<div class="col col-3">Référent</div>
								<div class="col col-3">Jury 2</div>
							</li>
							<li class="table-row">
								<div class="col col-3" data-label="Id"><%=defense.getDate()%></div>
								<div class="col col-3" data-label="Time"><%=defense.getTime() %></div>
								<div class="col col-3" data-label="Référent" title="${(defense.referent != null) ? defense.referent.email : 'No referent'}">${(defense.referent != null) ? defense.referent.mail : 'No referent'}</div>
								<div class="col col-3" data-label="Jury 2" title="${(defense.jury2 != null) ? defense.jury2.email : 'No jury 2'}">${(defense.jury2 != null) ? defense.jury2.mail : 'No jury 2'}</div>
							</li>
						</ul>
					</div>
					<%
					} else {
					%>
					<p style="text-align: center; font-size: 2em;"> No defense. </p>
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
					<c:choose>
						<c:when test="${atLeastOneAvailableInternship}">
							<!-- show available internships if exist -->
							<c:forEach items="${topicsAvailableForTheStudentPerProgram}" var="entry">
								<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">
									<h2 class="login100-form-btn-V2 p-l-5 p-r-5">${entry.key.name} - ${entry.key.year}</h2>
								</div>
								<div class="container" id="list">
								<ul class="responsive-table">
								<li class="table-header">
									<div class="col col-1"> Id </div>
									<div class="col col-4">Topic Title</div>
									<div class="col col-3">Categories</div>
									<div class="col col-3">Supervisor</div>
									<div class="col col-1">Download topic</div>
								</li>
								<c:forEach items="${topicsAvailableForTheStudentPerProgram[entry.key]}" var="topic">
									<li class="table-row">
									<div class="col col-1" data-label="Id">${topic.getId()}  ${topic.isConfidentialInternship() == true ? '<i class="fas fa-lock" style="color: goldenrod" title="Confidential internship"></i>' : '<i class="fas fa-unlock" style="color: goldenrod" title="Non-confidential internship"></i>'}</div>
									<div class="col col-4" data-label="Topic Title">${topic.getTitle()}</div>
									<div class="col col-3" data-label="Topic Categories">
									<select class="mul-select" id="mul-select-${topic.id}" name="topics[]" multiple="multiple" data-pid="${topic.id}" disabled>
									<c:forEach items="${topic2category}" var="entry2">
										<c:if test="${entry2.key == topic.getId()}">
											<c:forEach items="${topic2category[entry2.key]}" var="category">
												<option>${category.name}</option>
											</c:forEach>
										</c:if>
									</c:forEach>
									</select>
									</div>
										<div class="col col-3" data-label="Supervisor Name">${topic.getSupervisorName()}<br/><button type="button" class="btn btn-secondary btn-sm" onclick="displayEmail('${topic.getSupervisorEmail()}')"><i class="fas fa-at" style="color: white"></i></button></div>
										<div class="col col-1" data-label="Topic"><button type="button" class="btn btn-secondary btn-sm"><a href="./download-topic?internshipId=${topic.getId()}" target="_blank"><i class="fas fa-download" style="color: white"></i></a></button></div>
									</li>
								</c:forEach>
								</ul>
								</div>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<p style="text-align: center; font-size: 2em;"> No available internships. </p>
						</c:otherwise>
					</c:choose>
				</form>
			</div>
		</div>
	</div>	
</body>
</html>
