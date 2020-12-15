<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
     	<meta charset="UTF-8" />
    	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Sign In</title>
        <!-- css -->
    	<link rel="stylesheet" href="signinstyle.css" />
    </head>
    <body>
    	<div class='bg-light' style='width: 200px; height: 200px; position: absolute; left:50%; top:50%;  margin:-100px 0 0 -100px; padding-top: 40px; padding-left: 10px;'>
        <form method="post" action="signin">
        First Name:<br/><input type="text" name="firstName" value="${firstName}"/><br>
        Last Name:<br/><input type="text" name="lastName" value="${lastName}"/><br>
        Email:<br/><input type="text" name="email" value="${email}"/><br/>
        Confirm Email:<br/><input type="text" name="confirmEmail" value="${confirmEmail}"/><br/>
        Password:<input type="password" name="pass"/><br/>
        Confirm Password:<input type="password" name="confirmPass" /><br/>
        <select name="role" >
        	<option value="" selected disabled hidden>--Please choose a role--</option>
        	<option value="admin">Admin</option>
        	<option value="assistant">Assistant</option>
        	<option value="professor">Professor</option>
        	<option value="proponent">Proponent</option>
        	<option value="student">Student</option>
        </select>
        <input type="submit" value="Sign In" />
        </form>
       	<p>${err_message}</p>
        </div>
    </body>
</html>
