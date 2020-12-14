<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
     	<meta charset="UTF-8" />
    	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Sign In</title>
        <!-- css -->
    	<link rel="stylesheet" href="uploadtopicstyle.css" />
    </head>
    <body>
		<div class='bg-light' style='width: 200px; height: 200px; position: absolute; left:50%; top:50%;  margin:-100px 0 0 -100px; padding-top: 40px; padding-left: 10px;'>
    	<form method="post" action="uploadtopic">
    	First Name:<br/><input type="text" name="firstName" /><br>
    	Last Name:<br/><input type="text" name="lastName" /><br>
    	Email:<br/><input type="text" name="email" /><br/>
    	Topic Title:<input type="text" name="topicTitle" /><br/>
    	<select name="program">
    		<option value="">--Select a program--</option>
    		<c:forEach items="${programs}" var="program">
    			<option value="${program.getId()}">${program.getName()}</option>
    		</c:forEach>
    	</select>
    	<input type="submit" value="Accept">
    	</form>
    	</div>

		<form method="post" action="login">
		Programs: <select name="programs" id="programs">
		    <option value="" selected disabled hidden>--Select a program--</option>
		  </select>
		  <br><br>
		Categories: <select name="categories" id="categories">
		    <option value="" selected disabled hidden>--Select a program first--</option>
		  </select>
		  <br><br>
		  <input type="submit" value="Upload">  
		</form>
		
		<script>
		var data = new Map();
		var program_name_id = new Map();
		<c:forEach items="${programs}" var="program">
			var program_dict = [];
		    <c:forEach items="${program.getCategories()}" var="category">
		   	 program_dict.push({
		   		 key: "${category.getId()}",
		   		 value: "${category.getName()}"
		   	 });
		    </c:forEach>
		    program_name_id.set("${program.getName()}", "${program.getId()}");
		    data.set("${program.getName()}", program_dict);
		</c:forEach>
		
		var programSel = document.getElementById("programs");
		var categorySel = document.getElementById("categories");
		window.onload = function() { 
			data.forEach((value, key) => {
				programSel.options[programSel.options.length] = new Option(key, key);
			});
		}
		programSel.onchange = function(){
			categorySel.length = 1;
			categorySel.options[1] = new Option("--Select a category--", "selectacategory1tobesure");
		    for (var i in data.get(this.value)) {
		   		categorySel.options[categorySel.options.length] = new Option(data.get(this.value)[i].value, data.get(this.value)[i].value);
		    }
		}
		</script>

    	
    </body>
</html>
