<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
						<p>Vous pouvez créer un jour de formation uniquement entre
							${startCreateDay} et ${stopCreateDay}.</p>
					</div>
				</c:if>
				<c:if test="${countersignTest == false}">
					<div class="warnMessage">
						<p>Vous pouvez contresigner uniquement entre
							${countersignStart} et ${countersignStop}.</p>
					</div>
				</c:if>
	
				<div id="mainClassroom"
					<c:if test="${createSchoolDayTest == true && countersignTest == true}">style="height: 90vh;"</c:if>
					<c:if test="${createSchoolDayTest == false && countersignTest == false}">style="height: 80vh;"</c:if>
					<c:if test="${createSchoolDayTest == true && countersignTest == false}">style="height: 85vh;"</c:if>
					<c:if test="${createSchoolDayTest == false && countersignTest == true}">style="height: 85vh;"</c:if>>
					<h1>Formation ${classroom.getName()}</h1>
					<div>
	
	
	
						<!-- CREATE SCHOOL DAY -->
						<c:set var="canCreateSchoolDay" value="${canCreateSchoolDay}" />
						<c:if test="${createSchoolDayTest == true && canCreateSchoolDay == true}">
							<div class="teachStuffContainer">
								<p class="font1dot5em">Cliquez sur le bouton ci-dessous pour
									créer une journée de formation et permettre à vos apprenants
									d'émarger:</p>
								<a id="createSchoolDayButton"
									href="${pageContext.request.contextPath}/createSchoolDay/${classroom.getId()}"
									class="font1dot5em">Créer une journée de formation pour
									${classroom.getName()}</a>
							</div>
						</c:if>
	
						<c:if
							test="${createSchoolDayTest == true && canCreateSchoolDay == false}">
							<div class="teachStuffContainer">
								<p class="font1dot5em">Une journée de formation existe déjà
									pour vos élève, ils peuvent émarger.</p>
							</div>
						</c:if>
	
	
	
						<!-- COUNTERSIGN FORM -->
						<c:set var="studentInThisClassroomExists"
							value="${studentInThisClassroomExists}" />
						<c:set var="alreadyCountersigned" value="${alreadyCountersigned}"></c:set>
						<c:if test="${countersignTest == true && studentInThisClassroomExists == true}">
							<c:if test="${alreadyCountersigned == true && canCreateSchoolDay == false}">
								<div class="teachStuffContainer">
									<h2 class="font1dot5em">CONTRESEING</h2>
									<div id="counterSignContainer">
										<form method="POST" action="${pageContext.request.contextPath}/countersign/${classroom.getId()}">
											<div id="tableContainer" class="sm-col12 md-col12 lg-col12">
												<table>
													<thead>
														<tr>
															<th colspan="1"></th>
															<th colspan="2">Matin</th>
															<th colspan="2">Après-midi</th>
														</tr>
														<tr>
															<th colspan="1">Apprenant</th>
															<th colspan="1">Présent</th>
															<th colspan="1">Absent</th>
															<th colspan="1">Présent</th>
															<th colspan="1">Absent</th>
														</tr>
													</thead>
													<tbody>
														<c:forEach var="s" items="${students}">
															<tr>
																<td colspan="1">${s.getLastName()} ${s.getFirstName()}</td>
																<td colspan="1"><input type="radio" class="signingCheckbox" name="morningCheck${s.getId()}"	id="morningCheck${s.getId()}" value="true" checked /></td>
																<td colspan="1"><input type="radio" class="signingCheckbox" name="morningCheck${s.getId()}" id="morningCheck${s.getId()}" value="false" /></td>
																<td colspan="1"><input type="radio" class="signingCheckbox" name="afternoonCheck${s.getId()}" id="afternoonCheck${s.getId()}" value="true" checked /></td>
																<td colspan="1"><input type="radio" class="signingCheckbox" name="afternoonCheck${s.getId()}" id="afternoonCheck${s.getId()}" value="false" /></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
											<div id="attestCheckbox">
												<input type="checkbox" class="signingCheckbox" id="attest"
													required> <label for="attest" class="font1dot5em">J'atteste
													sur l'honneur que les informations que je communique via ce
													formulaire sont véridiques.</label>
											</div>
											<div>
												<button type="submit" class="font1dot5em">Valider</button>
											</div>
										</form>
									</div>
								</div>
							</c:if>
							<c:if test="${alreadyCountersigned == false}">
								<p class="font1dot5em">Vous avez déjà vérifier et
									contresigner les présences des élèves.</p>
							</c:if>
						</c:if>
						<c:if
							test="${countersignTest == true && studentInThisClassroomExists == false}">
							<p class="font1dot5em">Il n'y a aucun élève dans cette classe</p>
						</c:if>
					</div>
	
	
	
					<!-- STUDENT ABSENCES HISTORY -->
					<c:if test="${studentInThisClassroomExists == true}">
						<div id="studentsHistories">
							<h3 class="font1dot5em">Historiques des absences de vos apprenants:</h3>
							<c:forEach var="s" items="${students}">
								<div class="studentHistoryContainer">
									<c:if test="${s.getAbsentHistories().size() > 0}">
										<table class="studentHistoryTable">
											<thead>
												<tr>
													<th colspan="1" class="tableHead">Apprenant</th>
													<c:forEach var="h" items="${s.getAbsentHistories()}">
														<th colspan="1" class="tableHead tableBody"><fmt:formatDate pattern = "dd/MM/yyyy" value = "${h.getDate()}"/></th>
													</c:forEach>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td colspan="1">${s.getLastName()} ${s.getFirstName()}</td>
													<c:forEach var="h" items="${s.getAbsentHistories()}">
														<td colspan="1" class="redBg tableBody">Absent</td>
													</c:forEach>
												</tr>
											</tbody>
										</table>
									</c:if>
									<c:if test="${s.getAbsentHistories().size() == 0}">
										<p class="font1dot5em borderGreen">${s.getLastName()} ${s.getFirstName()} n'a jamais été absent!</p>
									</c:if>
								</div>
							</c:forEach>
						</div>
					</c:if>
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