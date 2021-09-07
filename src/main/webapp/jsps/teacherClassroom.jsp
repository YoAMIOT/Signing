<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
	<head>
	    <meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="css/style.css">
	    <title>${classroom.getName()}</title>
	</head>
	
	
	
	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">
			<!-- MAIN -->
			<div id="mainTeachContainer" class="sm-col12 md-col9 lg-col9">
				<c:set var="countersignTest" value="${countersignTest}"/>
				<c:set var="createSchoolDayTest" value="${createSchoolDayTest}"/>
				<c:if test="${createSchoolDayTest == false}">
					<div class="warnMessage">
						<p>Vous pouvez créer un jour de formation uniquement entre ${startCreateDay} et ${stopCreateDay}.</p>
					</div>
				</c:if>
				<c:if test="${countersignTest == false}">
					<div class="warnMessage">
						<p>Vous pouvez contresigner uniquement entre ${countersignStart} et ${countersignStop}.</p>
					</div>
				</c:if>
			</div>
			
			
			
			<!-- CLASSROOMS LIST -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<h2>Liste de vos formations</h2>
				
				<c:set var="classroomWithTeachExists" value="${classroomWithTeachExists}"/>
				<c:if test="${classroomWithTeachExists == true}">
					<c:forEach var="c" items="${classrooms}">
						<a href="/ANPEPSigning/teacherClassroom/${c.getId()}" class="font2em classroomSelector">${c.getName()}</a>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</body>
</html>