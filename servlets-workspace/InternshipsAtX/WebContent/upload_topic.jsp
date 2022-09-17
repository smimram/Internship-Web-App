<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Upload Topic</title>
	<%@ include file="meta.jsp" %>
</head>
<body>
	<!-- navigation bar -->
	<jsp:include page="header.jsp"></jsp:include>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100">
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178 p-b-10" method="post" action="upload-topic" enctype="multipart/form-data">
					<span class="login100-form-title">
						Upload a topic now
					</span>

	
					<div class="text-red flex-col-c p-b-10">
						<p class="text-red" style="color:red;">${err_message}</p>
					</div>
					
					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name="firstName" placeholder="First Name" value="${firstName}">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name="lastName" placeholder="Last Name" value="${lastName}">
						<span class="focus-input100"></span>
					</div>
					
					<div class="wrap-input100 validate-input m-b-16" data-validate="Please enter your email">
						<input class="input100" type="text" name="email" placeholder="Email" value="${email}">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Please confirm your email">
						<input class="input100" type="text" name="topicTitle" placeholder="Topic title" value="${topicTitle}">
						<span class="focus-input100"></span>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate = "Please choose the program">
						<select name="programs" id="programs" class="input100">
				    		<option value="0" selected disabled hidden>Select a program</option>
						</select>
					</div>

					<div class="wrap-input100 validate-input m-b-25" data-validate = "Please choose the category">
					<select name="categories" id="categories" class="input100">
		    			<option value="-1" selected disabled hidden> Select a category </option>
		  			</select>
					</div>

					<div class="wrap-input100 validate-input m-b-16" data-validate="Check this if the topic is confidential">
						<label for="confidentiality">Confidential topic: </label>
						<label class="switch">
							<input type="checkbox" id="confidentiality" name="confidentiality">
							<span class="slider round"></span>
						</label>
					</div>

					<div class="wrap-input100 validate-input m-b-5" class="input100">
						<p class="text-black"> <b> Please upload the PDF describing the internship offer. </b> </p>
						<input type="file" name="uploadFile" accept="application/pdf"/>
		  				<br><br>
					</div>


					<div class="container-login100-form-btn p-t-5">
						<button type="submit" class="login100-form-btn">
							Upload topic
						</button>
					</div>
				
				</form>
			</div>
		</div>
	</div>
	
	
		
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
		    program_name_id.set("${program.getId()}", "${program.getName()}" + " - " + "${program.getYear()}");
		    data.set("${program.getId()}", program_dict);
		</c:forEach>
		
		var programSel = document.getElementById("programs");
		var categorySel = document.getElementById("categories");
		window.onload = function() {
			data.forEach((value, key) => {
				programSel.options[programSel.options.length] = new Option(program_name_id.get(key), key);
			});
		}
		programSel.onchange = function(){
			categorySel.length = 1;
			categorySel.options[1] = new Option("--Select a category--", "0");
		    for (var i in data.get(this.value)) {
		   		categorySel.options[categorySel.options.length] = new Option(data.get(this.value)[i].value, data.get(this.value)[i].key);
		    }
		}
		</script>

    	

</body>
</html>