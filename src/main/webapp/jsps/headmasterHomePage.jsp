<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	    <title>Directeur</title>
	</head>
	
	
	
	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">
			<!-- MAIN -->	
			<div id="mainContainer" class="sm-col12 md-col9 lg-col9">
			
			</div>
			
			
			
			<!-- NAVBAR -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<h2 class="font2em">Menu de navigation:</h2>
				<a href="${pageContext.request.contextPath}/headmaster/classrooms" class="font2em navSelector">Formations</a>
				<a href="${pageContext.request.contextPath}/headmaster/users" class="font2em navSelector">Utilisateurs</a>
				<a href="${pageContext.request.contextPath}" class="font2em navSelector">Déconnexion</a>
			</div>
		</div>
	</body>
</html>