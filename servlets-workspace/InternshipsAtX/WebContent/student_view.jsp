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
    
    
   	
		
		<script>
		var programs_categories = new Map();
		var program_name_id = new Map();
		var categories_to_subjects = new Map();
		
		<c:forEach items="${programs}" var="program">
			var program_dict = [];
		    <c:forEach items="${program.getCategories()}" var="category">
		   	 program_dict.push({
		   		 key: "${category.getId()}",
		   		 value: "${category.getName()}"
		   	 });
		    </c:forEach>
		    program_name_id.set("${program.getId()}", "${program.getName()}");
		    programs_categories.set("${program.getId()}", program_dict);
		</c:forEach>
		
		<c:forEach items="${subjectsPerCategory}" var="categoryAndSubjects">
			var subjects = [];
			 <c:forEach items="${categoryAndSubjects.getSubjects()}" var="subject">
			 	
			 </c:forEach>
			 var categoryId = "${categoryAndSubjects.getCategoryId()}";
		</c:forEach>
		</script>

    	
    </body>
</html>
