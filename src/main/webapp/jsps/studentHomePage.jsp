<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	    <title>Apprenant</title>
	</head>
	
	
	
	<body>
		<div id="homeContainer" class="sm-col12 md-col12 lg-col12">				
			<div id="mainContainer" class="sm-col12 md-col9 lg-col9">
				<h2 class="font2em" id="signingTitle">Votre emargement journalier:</h2>
				
				<!-- SIGNING FORM -->
				<div id="formContainer">
				
					<!--ID THERE'S A HISTORY FOR TODAY-->
					<c:set var = "todaysHistoryExists" value = "${todaysHistoryExists}"/> 					
					<c:if test = "${todaysHistoryExists == true}">

						<!-- IF IT'S IN THE MORNING TIME SLOT -->
						<c:set var="morningTest" value="${morningTest}"/>
						<c:set var="morningSigned" value="${morningSigned}"/>
						<c:if test="${morningTest == true}">
							
							<!-- IF THE STUDENT HASN'T ALREADY SIGNED THIS MORNING -->
							<c:if test="${morningSigned == false}">
								<div id="morningForm" class="font1dot5em">
									<form:form method="POST" action="${pageContext.request.contextPath}/studentHome/morningSigning" modelAttribute="history" acceptCharset="utf-8">
										<div>
											<form:checkbox class="signingCheckbox" path="morningSign" value="true" required="required" />
											<label for="checkBox">J'atteste sur l'honneur être présent à mon poste de travail ce matin afin de suivre ma formation.</label>
										</div>
											
										<div>
											<button type="submit" class="font1dot5em">Emarger</button>
										</div>
									</form:form>
								</div>
							</c:if>	
										
							<!-- IF THE STUDENT HAS ALREADY SIGNED THIS MORNING -->			
							<c:if test="${morningSigned == true}">
								<div>
									<p class="font2em">Vous avez émarger ce matin. Revenez cet après midi.</p>
								</div>
							</c:if>
						</c:if>
						
						
								
						<!-- IF IT'S IN THE AFTERNOON TIME SLOT -->
    					<c:set var="afternoonTest" value="${afternoonTest}"/>
    					<c:set var="afternoonSigned" value="${afternoonSigned}"/>
    					<c:if test="${afternoonTest == true}">
    					
    						<!-- IF THE STUDENT HASN'T ALREADY SIGNED THIS MORNING -->
    						<c:if test="${afternoonSigned == false}">
								<div id="afternoonForm" class="font1dot5em">
									<form:form method="POST" action="${pageContext.request.contextPath}/studentHome/afternoonSigning" modelAttribute="history" acceptCharset="utf-8">
										<div>
											<form:checkbox class="signingCheckbox" path="afternoonSign" value="true" required="required" />
											<label for="checkBox">J'atteste sur l'honneur être présent à mon poste de travail cet après-midi afin de suivre ma formation.</label>
										</div>
									
										<div>
											<button type="submit" class="font1dot5em">Emarger</button>
										</div>
									</form:form>
								</div>
							</c:if>
							
							<!-- IF THE STUDENT HAS ALREADY SIGNED THIS AFTERNOON -->
							<c:if test="${afternoonSigned == true}">
								<div>
									<p class="font2em">Vous avez émarger cet après-midi. On se revoit plus tard.</p>
								</div>
							</c:if>
						</c:if>
						
						
						
						
						<!-- IF THE ACTUAL TIME IS NOT IN A TIME SLOT -->
						<c:if test="${morningTest == false && afternoonTest == false}">
							<div>
								<p class="font2em">Vous ne pouvez pas émarger actuellement. Revenez entre ${startMorning} et ${stopMorning} ou l'après-midi entre ${startAfternoon} et ${stopAfternoon}.</p>
							</div>
						</c:if>
					</c:if>
					
					
					
					<!--IF THERE'S NO HISTORY FOR TODAY-->
					<c:if test = "${todaysHistoryExists == false}">
						<div>
							<p class="font2em">Aujourd'hui vous êtes en vacance! Ce n'est pas un jour de formation!</p>
						</div>
					</c:if>
				</div>
			</div>
			
			
			
			<!-- STUDENT HISTORY -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
				<h2 class="font2em">Menu de navigation:</h2>
				<a href="${pageContext.request.contextPath}" class="font2em navSelector">Déconnexion</a>
				
				<h2 class="font2em">Vos absences:</h2>

				<div>
					<c:set var="absenceHistoryExists" value="${absenceHistoryExists}"/>
					<c:if test="${absenceHistoryExists == true}">
						<c:forEach var="h" items="${absencesHistories}">
							<p class="font2em"><fmt:formatDate pattern = "dd/MM/yyyy" value = "${h.getDate()}" /></p>
						</c:forEach>
					</c:if>
					
					
					
					<c:if test="${absenceHistoryExists == false}">
						<p class="font1dot5em" id="neverAbsent">Vous n'avez jamais été absent.</p>
					</c:if>
				</div>
			</div>
			

			
			<%@ include file="privacyPolicy.jsp"%>
		</div>
	</body>
</html>