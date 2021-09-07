<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="css/style.css">
	    <title>UserAdder</title>
	</head>



	<body>
		<form:form method="POST" action="addUser" modelAttribute="user">
				
					<label for="email">Email:</label>
					<form:input path="email" placeholder="Email" required="required" />
				
					<label for="firstName">Prénom:</label>
					<form:input path="firstName" placeholder="Prénom" required="required" />
					
					<label for="lastName">Nom:</label>
					<form:input path="lastName" placeholder="Nom" required="required" />
					
					<label for="password">Mot de Passe:</label>
					<form:input path="password" minlength="5" placeholder="Mot de Passe" required="required" />
					
					<label for="responsability">Responsabilité: </label>
					<form:select path="responsability" required="required" >
						<form:option value="0">Apprenant</form:option>
						<form:option value="1">Formateur</form:option>
						<form:option value="2">Directeur</form:option>
					</form:select>
					
					
					<button type="submit">Ajouter l'User</button>
		</form:form>
	</body>
</html>