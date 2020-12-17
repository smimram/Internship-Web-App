<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
	<link rel="stylesheet" type="text/css" href="css/student_view.css">
	
<!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">

		
		
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						Internships
					</span>
						
					<div class="container" id="list">
					  
					</div>
					
					
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
		    program_name_id.set("${program.getId()}", "${program.getName()}");
		    programs_categories.set("${program.getId()}", program_categories);
		</c:forEach>
		
		<c:forEach items="${subjectsPerCategory}" var="categoryAndSubjects">
			var subjects = [];
			<c:forEach items="${categoryAndSubjects.getSubjects()}" var="subject">
			 	subjects.push({title: "${subject.getTitle()}", id: "${subject.getId()}", supervisorEmail: "${subject.getSupervisorEmail()}", supervisorName: "${subject.getSupervisorName()}", encodedContent: "${subject.getContent()}"})
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
		
		var programList = document.getElementById('list');
		window.onload = function() {
			var str = "P=";
			
			
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
						//programList.innerHTML += '<table class="subjects" id="'+k.concat(category.key)+'pctable" style="width:100%">'; 
						programList.innerHTML += '<ul class="responsive-table" id="'+k.concat(category.key)+'pctable">';
						
						var newRow = document.getElementById(k.concat(category.key).concat("pctable"));
						
						
						//newRow.innerHTML = '<th>Id</th><th>Subject Title</th><th>Supervisor Name</th><th>Supervisor Email</th><th>Subject</th>';
						newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-2">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-4">Supervisor Email</div><div class="col col-5">Subject</div></li>';		
						
						for(const subject of subjects) {
							var newRowE = document.getElementById(k.concat(category.key).concat("pctable"));
							if(subject.title==="OOO") {
								str = subject.encodedContent;
								//newRowE.innerHTML = '<td>'+subject.id+'</td><td>'+subject.title+'</td><td>'+subject.supervisorName+'</td><td>'+subject.supervisorEmail+'</td><td><input type="button" onClick="base64toPDF(\'' +str+ '\')" value="Dowload"/></td>';
								
								newRowE.innerHTML += '<li class="table-row"><div class="col col-1" data-label="Id">' + subject.id + '</div>'+
															'<div class="col col-2" data-label="Subject Title">'+subject.title+'</div>'+
															'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
															'<div class="col col-4" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
															'<div class="col col-5" data-label="Subject"><input type="button" onClick="base64toPDF(\'' +str+ '\')" value="Download"/></div></li>';
							
							
							
							} else {
								newRowE.innerHTML += '<li class="table-row"><div class="col col-1" data-label="Id">' + subject.id + '</div><div class="col col-2" data-label="Subject Title">'+subject.title+'</div><div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div><div class="col col-4" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div><div class="col col-5" data-label="Subject"><input type="button" onClick="" value="Download"/></div></li>';
								//newRowE.innerHTML += '<td>'+subject.id+'</td><td>'+subject.title+'</td><td>'+subject.supervisorName+'</td><td>'+subject.supervisorEmail+'</td><td><input type="button" onClick="" value="Dowload"/></td>';
							}
						}
						//programList.innerHTML += '</table>'; 
						programList.innerHTML += '</ul>'; 
					}
					
					programList.innerHTML += '</div>';
				}
				programList.innerHTML += '</div>';
			});
		}
		
		function base64toPDF(data) {
		    var bufferArray = base64ToArrayBuffer(data);
		    var blobStore = new Blob([bufferArray], { type: "application/pdf" });
		    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
		        window.navigator.msSaveOrOpenBlob(blobStore);
		        return;
		    }
		    var data = window.URL.createObjectURL(blobStore);
		    var link = document.createElement('a');
		    document.body.appendChild(link);
		    link.href = data;
		    link.download = "file.pdf";
		    link.click();
		    window.URL.revokeObjectURL(data);
		    link.remove();
		}

		function base64ToArrayBuffer(data) {
		    var bString = window.atob(data);
		    var bLength = bString.length;
		    var bytes = new Uint8Array(bLength);
		    for (var i = 0; i < bLength; i++) {
		        var ascii = bString.charCodeAt(i);
		        bytes[i] = ascii;
		    }
		    return bytes;
		};
		
		</script>		
					
					
					
					
				</form>
				
			</div>
		</div>
	</div>
	
	
							
	

</body>
</html>