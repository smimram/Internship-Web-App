<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>	
    <head>
     	<meta charset="UTF-8" />
    	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Student View</title>
        <!-- css -->
    	<link rel="stylesheet" href="student_viewstyle.css" />
    </head>
    <body>
    
		<div id="list">
		
		</div>

		<script>
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
				programList.innerHTML += '<h2>'+program_name_id.get(k)+'</h2>';
				for (const category of v) {
					programList.innerHTML += '<div class="category", id="'+category.key+'">';
					programList.innerHTML += '<h4>'+category.value+'</h4>';
					
					var subjects = categories_to_subjects.get(k).get(category.key);
					if(subjects.length>0) {
						programList.innerHTML += '<table class="subjects" id="'+k.concat(category.key)+'pctable" style="width:100%">';
						
						var newRow = document.getElementById(k.concat(category.key).concat("pctable")).insertRow();
						newRow.innerHTML = '<th>Id</th><th>Subject Title</th><th>Supervisor Name</th><th>Supervisor Email</th><th>Subject</th>';
						for(const subject of subjects) {
							var newRowE = document.getElementById(k.concat(category.key).concat("pctable")).insertRow();
							if(subject.title==="OOO") {
								str = subject.encodedContent;
								newRowE.innerHTML = '<td>'+subject.id+'</td><td>'+subject.title+'</td><td>'+subject.supervisorName+'</td><td>'+subject.supervisorEmail+'</td><td><input type="button" onClick="base64toPDF(\'' +str+ '\')" value="Dowload"/></td>';
							} else {
								newRowE.innerHTML = '<td>'+subject.id+'</td><td>'+subject.title+'</td><td>'+subject.supervisorName+'</td><td>'+subject.supervisorEmail+'</td><td><input type="button" onClick="" value="Dowload"/></td>';
							}
						}
						programList.innerHTML += '</table>'; 
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

    	<!-- <a href="#" onclick="window.open('MyPDF.pdf', '_blank', 'fullscreen=yes'); return false;">MyPDF</a> -->
    </body>
</html>
