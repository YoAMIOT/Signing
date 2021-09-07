<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	    <title>Formateur</title>
	</head>
	
	
	
	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">
			<!-- MAIN -->
			<div id="mainTeachContainer" class="sm-col12 md-col9 lg-col9">
				<div id="teachTutorial">
					<p class="font2em">Pour commencer, vous pouvez sélectionner une des formation dont vous êtes le référant dans la liste de formations sur votre droite. Ainsi vous pourrez y voir les historique des absences de vos apprenant et vous pourrez aussi contresigner à la fin de la journée.</p>
				</div>
			</div>
			
			
			
			<!-- CLASSROOMS LIST -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<h2>Liste de vos formations</h2>
				
				<c:set var="classroomWithTeachExists" value="${classroomWithTeachExists}"/>
				<c:if test="${classroomWithTeachExists == true}">
					<c:forEach var="c" items="${classrooms}">
						<a href="${pageContext.request.contextPath}/teacherClassroom/${c.getId()}" class="font2em classroomSelector">${c.getName()}</a>
					</c:forEach>
				</c:if>
			</div>
			
			
			
			<%@ include file="privacyPolicy.jsp"%>
		</div>
	</body>
</html>