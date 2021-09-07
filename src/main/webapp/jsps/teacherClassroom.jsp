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
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/style.css">
	<title>${classroom.getName()}</title>
	</head>



	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">
			<!-- MAIN -->
			<div id="mainTeachContainer" class="sm-col12 md-col9 lg-col9">
				<c:set var="countersignTest" value="${countersignTest}" />
				<c:set var="createSchoolDayTest" value="${createSchoolDayTest}" />
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
				
				<div id="mainClassroom" <c:if test="${createSchoolDayTest == true && countersignTest == true}">style="height: 90vh;"</c:if> <c:if test="${createSchoolDayTest == false && countersignTest == false}">style="height: 80vh;"</c:if> <c:if test="${createSchoolDayTest == true && countersignTest == false}">style="height: 85vh;"</c:if> <c:if test="${createSchoolDayTest == false && countersignTest == true}">style="height: 85vh;"</c:if>>
					<h1>Formation ${classroom.getName()}</h1>
					<div>
		
						<!-- COUNTERSIGN FORM -->
						<c:set var="canCreateSchoolDay" value="${canCreateSchoolDay}" />
						<c:if test="${createSchoolDayTest == true && canCreateSchoolDay == true}">
							<div class="teachStuffContainer">
								<p class="font1dot5em">Cliquez sur le bouton ci-dessous pour créer une journée de formation et permettre à vos apprenants d'émarger:</p>
								<a id="createSchoolDayButton" href="${pageContext.request.contextPath}/createSchoolDay/${classroom.getId()}" class="font1dot5em">Créer une journée de formation pour ${classroom.getName()}</a>
							</div>
						</c:if>
						
						<c:if test="${createSchoolDayTest == true && canCreateSchoolDay == false}">
							<div class="teachStuffContainer">
								<p class="font1dot5em">Une journée de formation existe déjà pour vos élève, ils peuvent émarger.</p>							
							</div>
						</c:if>
						
						<!-- COUNTERSIGN FORM -->
						<c:if test="${countersignTest == true}">
							<div class="teachStuffContainer">
								<p class="font1dot5em">CONTRESEING</p>
								
								<div id="counterSignContainer">
									<form:form method="POST" action="countersign" modelAttribute="history" acceptCharset="utf-8">
										<table>
										    <thead>
										        <tr>
										            <th colspan="2"></th>
										            <th colspan="2">Matin</th>
										            <th colspan="2">Après-midi</th>
										        </tr>
										        <tr>
										            <th colspan="1">NOM</th>
										            <th colspan="1">Prénom</th>
										            <th colspan="1">Présent</th>
										            <th colspan="1">Absent</th>
										            <th colspan="1">Présent</th>
										            <th colspan="1">Absent</th>										        
										        </tr>
										    </thead>
										    <tbody>
										        <tr>
										            <td colspan="1">TOMMY</td>
										            <td colspan="1">Tommy</td>
										            <td colspan="1"><form:radiobutton class="signingCheckbox" path="morningCheck" value="true"/></td>
										            <td colspan="1"><form:radiobutton class="signingCheckbox" path="morningCheck" value="false"/></td>
										            <td colspan="1"><form:radiobutton class="signingCheckbox" path="afternoonCheck" value="true"/></td>
										            <td colspan="1"><form:radiobutton class="signingCheckbox" path="afternoonCheck" value="false"/></td>

										        </tr>
										    </tbody>
										</table>
										
										<div>
											<button type="submit" class="font1dot5em">Valider</button>
										</div>
									</form:form>
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</div>
	
	
	
			<!-- CLASSROOMS LIST -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<h2>Liste de vos formations</h2>
	
				<c:set var="classroomWithTeachExists"
					value="${classroomWithTeachExists}" />
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