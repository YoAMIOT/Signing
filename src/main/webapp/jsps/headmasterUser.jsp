<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
		<c:set var="userSelected" value="${userSelected}"/>
		<title>
			<c:if test="${userSelected == false}">Gestion des utilisateurs</c:if>
			<c:if test="${userSelected == true}">
				USER
			</c:if>
		</title>
	</head>



	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">
			<!-- MAIN -->
			<div id="mainTeachContainer" class="sm-col12 md-col9 lg-col9">
				<div id="headmasterMainWrapper">
					<c:if test="${userSelected == false}">
						<!-- USER CREATOR -->
						<div class="teachStuffContainer creator" >
							<h2 class="font2em">Créer un utilisateur</h2>
							<form:form method="POST" action="addUser" modelAttribute="user">
						
								<label for="email" class="font1dot5em">Email:</label>
								<form:input path="email" class="font1dot5em" placeholder="Email" required="required" />
									
								<label for="firstName" class="font1dot5em">Prénom:</label>
								<form:input path="firstName" class="font1dot5em" placeholder="Prénom" required="required" />
										
								<label for="lastName" class="font1dot5em">Nom:</label>
								<form:input path="lastName" class="font1dot5em" placeholder="Nom" required="required" />
										
								<label for="password" class="font1dot5em">Mot de Passe:</label>
								<form:input path="password" class="font1dot5em" minlength="5" placeholder="Mot de Passe" required="required" />
										
								<label for="responsability" class="font1dot5em">Responsabilité: </label>
								<form:select path="responsability" class="font1dot5em" required="required" >
									<form:option value="0">Apprenant</form:option>
									<form:option value="1">Formateur</form:option>
									<form:option value="2">Directeur</form:option>
								</form:select>
										
								<button type="submit" class="font1dot5em">Ajouter l'utilisateur</button>
							</form:form>
						</div>
					</c:if>
					
					
					
					<c:if test="${userSelected == true}">

					</c:if>
				</div>
			</div>
	
	
	
			<!-- CLASSROOMS LIST -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<a href="${pageContext.request.contextPath}/headmaster/" class="font2em navSelector">Retour au menu principal</a>
				
				<c:if test="${userSelected == true}">
					<a href="${pageContext.request.contextPath}/headmaster/users" class="font2em navSelector">Retour au menu des utilisateurs</a>
				</c:if>
			</div>
		</div>
	</body>
</html>