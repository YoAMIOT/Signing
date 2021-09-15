<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
		<c:set var="userSelected" value="${userSelected}"/>
		<c:set var="selectedUser" value="${selectedUser}"/>
		<title>
			<c:if test="${userSelected == false}">Gestion des utilisateurs</c:if>
			<c:if test="${userSelected == true}">
				${selectedUser.getLastName()} ${selectedUser.getFirstName()}
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
						<h1>Gestion des utilisateurs</h1>
						<div class="teachStuffContainer creator" >
							<h2 class="font2em">Créer un utilisateur</h2>
							<form:form method="POST" action="${pageContext.request.contextPath}/headmaster/user/addUser" modelAttribute="user">
						
								<label for="email" class="font1dot5em">Email:</label>
								<form:input path="email" class="font1dot5em" placeholder="Email" required="required" autocomplete="off"/>
									
								<label for="firstName" class="font1dot5em">Prénom:</label>
								<form:input path="firstName" class="font1dot5em" placeholder="Prénom" required="required" autocomplete="off"/>
										
								<label for="lastName" class="font1dot5em">Nom:</label>
								<form:input path="lastName" class="font1dot5em" placeholder="Nom" required="required" autocomplete="off"/>
										
								<label for="password" class="font1dot5em">Mot de Passe:</label>
								<form:input path="password" class="font1dot5em" minlength="5" placeholder="Mot de Passe" required="required" autocomplete="off"/>
										
								<label for="responsability" class="font1dot5em">Responsabilité: </label>
								<form:select path="responsability" class="font1dot5em" required="required" >
									<form:option value="0">Apprenant</form:option>
									<form:option value="1">Formateur</form:option>
									<form:option value="2">Directeur</form:option>
								</form:select>
										
								<button type="submit" class="font1dot5em">Ajouter l'utilisateur</button>
							</form:form>
						</div>
						
						
						
						<!-- LIST OF USERS -->
						<h2>Liste de tout les directeurs</h2>
						<div class="teachStuffContainer">
							<c:forEach var="h" items="${allHeadmasters}">
								<a href="${pageContext.request.contextPath}/headmaster/user/${h.getId()}" class="userFromList font1dot5em">${h.getLastName()} ${h.getFirstName()}</a>
							</c:forEach>
						</div>

						<c:set var="anyExistingTeacher" value="${anyExistingTeacher}"/>
						<c:if test="${anyExistingTeacher == true}">
							<h2>Liste de tout les formateurs</h2>
							<div class="teachStuffContainer">
								<c:forEach var="t" items="${allTeachers}">
									<a href="${pageContext.request.contextPath}/headmaster/user/${t.getId()}" class="userFromList font1dot5em">${t.getLastName()} ${t.getFirstName()}</a>
								</c:forEach>
							</div>
						</c:if>
						
						<c:set var="anyExistingStudent" value="${anyExistingStudent}"/>
						<c:if test="${anyExistingStudent == true}">
							<h2>Liste de tout les apprenants</h2>
							<div class="teachStuffContainer">
								<c:forEach var="s" items="${allStudents}">
									<a href="${pageContext.request.contextPath}/headmaster/user/${s.getId()}" class="userFromList font1dot5em">${s.getLastName()} ${s.getFirstName()}</a>
								</c:forEach>
							</div>
						</c:if>
					</c:if>
					
					
					
					<c:if test="${userSelected == true}">
						<!-- USER'S INFOS -->
						<div class="teachStuffContainer">
							<h1>${selectedUser.getLastName()} ${selectedUser.getFirstName()}</h1>
							<p class="font1dot5em">Résponsabilité: 
								<c:if test="${selectedUser.getResponsability() == 0}">Apprenant</c:if>
								<c:if test="${selectedUser.getResponsability() == 1}">Formateur</c:if>
								<c:if test="${selectedUser.getResponsability() == 2}">Directeur</c:if>
							</p>
							<p class="font1dot5em">Email: ${selectedUser.getEmail()}</p>
						</div>
						
						
						
						<!-- ABSENCE HISTORIES -->
						<c:if test="${selectedUser.getResponsability() == 0}">
							<div id="studentsHistories" class="teachStuffContainer">
								<h3 class="font1dot5em">Historiques des absences de ${selectedUser.getLastName()} ${selectedUser.getFirstName()}</h3>
								<div class="studentHistoryContainer">
									<c:if test="${selectedUser.getAbsentHistories().size() > 0}">
										<table class="studentHistoryTable">
											<thead>
												<tr>
													<th colspan="1" class="tableHead">Apprenant</th>
													<c:forEach var="h" items="${selectedUser.getAbsentHistories()}">
														<th colspan="1" class="tableHead tableBody"><fmt:formatDate pattern = "dd/MM/yyyy" value = "${h.getDate()}"/></th>
													</c:forEach>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td colspan="1">${selectedUser.getLastName()} ${selectedUser.getFirstName()}</td>
													<c:forEach var="h" items="${selectedUser.getAbsentHistories()}">
														<td colspan="1" class="redBg tableBody">Absent</td>
													</c:forEach>
												</tr>
											</tbody>
										</table>
									</c:if>
									<c:if test="${selectedUser.getAbsentHistories().size() == 0}">
										<p class="font1dot5em borderGreen">${selectedUser.getLastName()} ${selectedUser.getFirstName()} n'a jamais été absent!</p>
									</c:if>
								</div>
							</div>
						</c:if>
						
						
						
						<!-- UPDATE USER -->
						<div class="teachStuffContainer creator">
							<h2 class="font2em">Modifier l'utilisateur</h2>
							<form:form method="POST" action="${pageContext.request.contextPath}/headmaster/user/updateUser/${selectedUser.getId()}" modelAttribute="user">
						
								<label for="email" class="font1dot5em">Email:</label>
								<form:input path="email" class="font1dot5em" placeholder="Email" required="required" autocomplete="off"/>
									
								<label for="firstName" class="font1dot5em">Prénom:</label>
								<form:input path="firstName" class="font1dot5em" placeholder="Prénom" required="required" autocomplete="off"/>
										
								<label for="lastName" class="font1dot5em">Nom:</label>
								<form:input path="lastName" class="font1dot5em" placeholder="Nom" required="required" autocomplete="off"/>
										
								<label for="password" class="font1dot5em">Mot de Passe:</label>
								<form:input path="password" class="font1dot5em" minlength="5" placeholder="Mot de Passe" required="required" autocomplete="off"/>
										
								<label for="responsability" class="font1dot5em">Responsabilité: </label>
								<form:select path="responsability" class="font1dot5em" required="required" >
									<form:option value="0">Apprenant</form:option>
									<form:option value="1">Formateur</form:option>
									<form:option value="2">Directeur</form:option>
								</form:select>
										
								<button type="submit" class="font1dot5em">Modifier l'utilisateur</button>
							</form:form>
						</div>
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