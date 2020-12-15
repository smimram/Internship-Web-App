<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
     	<meta charset="UTF-8" />
    	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Log In</title>
        <!-- css -->
    	<link rel="stylesheet" href="loginstyle.css" />
    </head>
    <body>
    	<div class='bg-light' style='width: 200px; height: 200px; position: absolute; left:50%; top:50%;  margin:-100px 0 0 -100px; padding-top: 40px; padding-left: 10px;'>
        <form method="post" action="login">
        Email:<br/><input type="text" name="email" /><br/>
        Password:<input type="password" name="pass" /><br/>
        <input type="submit" value="Log In" />
        </form>
        <p>${err_message}</p>
        </div>
    </body>
</html>