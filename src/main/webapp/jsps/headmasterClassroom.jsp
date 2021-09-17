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
		<c:set var="classroomSelected" value="${classroomSelected}"/>
		<title>
			<c:if test="${classroomSelected == false}">Gestion des formations</c:if>
			<c:if test="${classroomSelected == true}">
				<c:set var="selectedClassroom" value="${selectedClassroom}"/>
				${selectedClassroom.getName()}
			</c:if>
		</title>
	</head>



	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">
			<!-- MAIN -->
			<div id="mainTeachContainer" class="sm-col12 md-col9 lg-col9">
				<div id="headmasterMainWrapper">
					<c:if test="${classroomSelected == false}">
						<!-- CLASSROOM CREATOR -->
						<div class="teachStuffContainer creator">
							<h2 class="font2em">Créer une formation</h2>
							
							<c:set var="anyExistingTeacher" value="${anyExistingTeacher}"/>
							<c:if test="${anyExistingTeacher == true}">
								<form:form method="POST" action="${pageContext.request.contextPath}/headmaster/classroom/addClassroom" modelAttribute="classroom">
									<label for="name" class="font1dot5em">Nom de la formation:</label>
									<form:input path="name" class="font1dot5em" id="name" placeholder="Nom de la formation" required="required" />
									
									<label for="StartDate" class="font1dot5em">Date de début de formation:</label>
									<form:input type="date" class="font1dot5em" path="StartDate" id="StartDate" required="required" />
										
									<label for="EndDate" class="font1dot5em">Date de fin de formation:</label>
									<form:input type="date" class="font1dot5em" path="EndDate" id="EndDate" required="required" />
										
									<label for="Teacher" class="font1dot5em">Formateur référent:</label>
									<form:select path="Teacher" class="font1dot5em" id="Teacher" required="required">
										<c:forEach var="t" items="${teachers}">
											<form:option value="${t}" class="font1dot5em">${t.getFirstName()} ${t.getLastName()}</form:option>
										</c:forEach>
									</form:select>
									
									<button type="submit" class="font1dot5em">Ajouter la formation</button>
								</form:form>
							</c:if>
							<c:if test="${anyExistingTeacher == false}">
								<p class="font1dot5em">Il n'y a actuellement aucun formateur, vous ne pouvez donc pas créer de formation, créez d'abord un formateur dans l'onglet "Utilisateurs" du menu principal.<p>
							</c:if>
						</div>
					</c:if>
					
					
					
					<c:if test="${classroomSelected == true}">
						<!-- CLASSROOM INFOS -->
						<div class="teachStuffContainer">
							<h1>${selectedClassroom.getName()}</h1>
							<p class="font1dot5em">Date de début: <fmt:formatDate pattern = "dd/MM/yyyy" value = "${selectedClassroom.getStartDate()}"/></p>
							<p class="font1dot5em">Date de fin: <fmt:formatDate pattern = "dd/MM/yyyy" value = "${selectedClassroom.getEndDate()}"/></p>
							<p class="font1dot5em">Formateur référent: <a href="${pageContext.request.contextPath}/headmaster/user/${selectedClassroom.getTeacher().getId()}" class="userFromList">${selectedClassroom.getTeacher().getLastName()} ${selectedClassroom.getTeacher().getFirstName()}</a></p>
						</div>
						
						
						
						<!-- STUDENTS LIST -->
						<c:set var="studentInThisClassroomExists" value="${studentInThisClassroomExists}"/>
						<c:if test="${studentInThisClassroomExists == true}">
							<div class="teachStuffContainer">
								<h2 class="font2em">Liste des apprenants de la formation</h2>
								<c:forEach var="s" items="${studentsOfClassroom}">
									<a href="${pageContext.request.contextPath}/headmaster/user/${s.getId()}" class="userFromList font1dot5em">${s.getLastName()} ${s.getFirstName()}</a>
								</c:forEach>
							</div>
						
						
						
							<!-- EXPORT TO CSV -->
							<c:set var="canExportDatasToCSV" value="${canExportDatasToCSV}"/>
							<c:if test="${canExportDatasToCSV == true}">
								<div class="teachStuffContainer creator">
									<h2 class="font2em">Télécharger les absences de la formation sur un fichier Excel</h2>
									<form method="POST" action="${pageContext.request.contextPath}/headmaster/classroom/exportToCSV/${classroom.getId()}">
										<label for="exportStartDate" class="font1dot5em">Date de début:</label>
										<input class="font1dot5em" type="date" id="exportStartDate" name="exportStartDate" min="${selectedClassroom.getStartDate()}" max="${selectedClassroom.getEndDate()}" required>
										
										<label for="exportEndDate" class="font1dot5em">Date de fin:</label>
										<input class="font1dot5em" type="date" id="exportEndDate" name="exportEndDate" min="${selectedClassroom.getStartDate()}" max="${selectedClassroom.getEndDate()}" required>
										
										<div>
											<button type="submit" class="font1dot5em">Télécharger</button>
										</div>
									</form>
								</div>
							</c:if>
						
						
						
							<!-- CREATE SCHOOL DAY -->
							<c:set var="canCreateSchoolDay" value="${canCreateSchoolDay}" />
							<c:if test="${canCreateSchoolDay == true}">
								<div class="teachStuffContainer">
									<h2 class="font2em">Jour de formation</h2>
									<p class="font1dot5em">Cliquez sur le bouton ci-dessous pour créer une journée de formation et permettre aux apprenants de la formation d'émarger:</p>
									<a id="createSchoolDayButton" href="${pageContext.request.contextPath}/headmaster/headmasterCreateSchoolDay/${selectedClassroom.getId()}" class="font1dot5em">Créer une journée de formation pour ${selectedClassroom.getName()}</a>
								</div>
							</c:if>
						</c:if>
						
						
						
						<!-- ADD STUDENT TO CLASSROOM -->
						<c:set var="anyExistingStudent" value="${anyExistingStudent}"/>
						<c:if test="${anyExistingStudent == true}">
							<div id="classroomUserAdder" class="teachStuffContainer">
								<h2 class="font2em">Ajouter un apprenant à la formation</h2>
								<form method="POST" action="${pageContext.request.contextPath}/headmaster/classroom/addStudent/${classroom.getId()}">
									<label for="student" class="font1dot5em">Apprenant:</label>
									<select name="student" id="student" class="font1dot5em">
										<c:forEach var="s" items="${allStudentsNotInThisClassroom}">
											<option value="${s.getId()}" class="font1dot5em">${s.getLastName()} ${s.getFirstName()}</option>
										</c:forEach>
									</select>
									<div>
										<button type="submit" class="font1dot5em">Ajouter l'apprenant à la formation</button>
									</div>
								</form>
							</div>
						</c:if>
						<c:if test="${anyExistingStudent == false}">
							<div class="teachStuffContainer">
								<p>Il n'existe aucun apprenant, créer un apprenant pour l'ajouter à la formation.</p>
							</div>
						</c:if>
						
						
						
						<!-- REMOVE STUDENT FROM CLASSROOM -->
						<c:if test="${studentInThisClassroomExists == true}">
							<div id="classroomUserAdder" class="teachStuffContainer">
								<h2 class="font2em">Supprimer un apprenant à la formation</h2>
								<form method="POST" action="${pageContext.request.contextPath}/headmaster/classroom/removeStudent/${classroom.getId()}">
									<label for="student" class="font1dot5em">Apprenant:</label>
									<select name="student" id="student" class="font1dot5em">
										<c:forEach var="s" items="${studentsOfClassroom}">
											<option value="${s.getId()}" class="font1dot5em">${s.getLastName()} ${s.getFirstName()}</option>
										</c:forEach>
									</select>
									<div>
										<button type="submit" class="font1dot5em">Supprimer l'apprenant de la formation</button>
									</div>
								</form>
							</div>
						</c:if>
						
						
						
						<!-- UPDATE CLASSROOM -->
						<div class="teachStuffContainer creator">
							<h2 class="font2em">Modifier la formation</h2>
							<form:form method="POST" action="${pageContext.request.contextPath}/headmaster/classroom/updateClassroom/${selectedClassroom.getId()}" modelAttribute="classroom">
								<label for="name" class="font1dot5em">Nom de la formation:</label>
								<form:input path="name" class="font1dot5em" id="name" placeholder="Nom de la formation" required="required" value="${selectedClassroom.getName()}"/>
								
								<label for="StartDate" class="font1dot5em">Date de début de formation:</label>
								<form:input type="date" class="font1dot5em" path="StartDate" id="StartDate" required="required" />
									
								<label for="EndDate" class="font1dot5em">Date de fin de formation:</label>
								<form:input type="date" class="font1dot5em" path="EndDate" id="EndDate" required="required" />
									
								<label for="Teacher" class="font1dot5em">Formateur référent:</label>
								<form:select path="Teacher" class="font1dot5em" id="Teacher" required="required">
									<c:forEach var="t" items="${teachers}">
										<form:option value="${t}" class="font1dot5em">${t.getFirstName()} ${t.getLastName()}</form:option>
									</c:forEach>
								</form:select>
									
								<button type="submit" class="font1dot5em">Mettre à jour la formation</button>
							</form:form>
						</div>
					</c:if>
				</div>
			</div>
	
	
	
			<!-- CLASSROOMS LIST -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<h2>Liste de toutes les formation</h2>
				
				<a href="${pageContext.request.contextPath}/headmaster/" class="font2em navSelector">Retour au menu principal</a>
				
				<c:if test="${classroomSelected == true}">
					<a href="${pageContext.request.contextPath}/headmaster/classrooms" class="font2em navSelector">Retour au menu des formations</a>
				</c:if>
				
				<c:set var="anyExistingClassroom" value="${anyExistingClassroom}" />
				<c:if test="${anyExistingClassroom == true}">
					<c:forEach var="c" items="${classrooms}">
						<a href="${pageContext.request.contextPath}/headmaster/classroom/${c.getId()}" class="font2em classroomSelector">${c.getName()}</a>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</body>
</html>