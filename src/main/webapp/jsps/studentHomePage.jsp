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
		<link rel="stylesheet" href="css/style.css">
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
									<form:form method="POST" action="morningSigning" modelAttribute="history" acceptCharset="utf-8">
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
									<form:form method="POST" action="afternoonSigning" modelAttribute="history" acceptCharset="utf-8">
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
					
					
					
					<!--ID THERE'S NO HISTORY FOR TODAY-->
					<c:if test = "${todaysHistoryExists == false}">
						<div>
							<p class="font2em">Aujourd'hui vous êtes en vacance! Ce n'est pas un jour de formation!</p>
						</div>
					</c:if>
				</div>
			</div>
			
			
			
			<!-- STUDENT HISTORY -->
			<div id="studentHistory" class="sm-col12 md-col3 lg-col3">
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
			

			
			<!-- PRIVACY -->
			<div id="privacy" class="sm-col12 md-col12 lg-col12">
				<a href="" id="privacyClose">&times;</a>
				
				<div id="policyContainer">
					<h2>Politique de confidentialité et mentions légales</h2>
					<div id="lilPolicyContainer" >
						<div class="policyParaContainer">
							<h3>Quelles données collectons-nous auprès de vous ?</h3>
							<p>Avec votre consentement préalable, lorsque vous utilisez notre site, nous collectons les informations suivantes :</p>
							<ul>
								<li>Nom</li>
								<li>Prénom</li>
								<li>Adresse électronique</li>
							</ul>
						</div>
						<div class="policyParaContainer">
							<h3>Avec quels outils ?</h3>
							<p>Vos données personnelles sont collectées lors de votre inscription et lors de vos émargements.</p>
						</div>
						<div class="policyParaContainer">
							<h3>Nous utilisons vos données personnelles ainsi collectées pour les finalités suivantes :</h3>
							<ul>
								<li>E-Mailing</li>
								<li>Statistiques</li>
								<li>Contact</li>
								<li>Contact téléphonique</li>
								<li>Gestion du site Web (présentation, organisation)</li>
							</ul>
						</div>
						<div class="policyParaContainer">
							<h3>Comment exercer votre droit d’opposition, de rectification ou de suppression?</h3>
							<p>Nous nous engageons à vous offrir un droit d’opposition, de rectification et de suppression quant à vos données personnelles. Le droit d’opposition s’entend comme étant la possibilité offerte aux visiteurs de refuser que leurs renseignements personnelles soient utilisées à certaines fins mentionnées lors de la collecte. Le droit de rectification s’entend comme étant la possibilité offerte aux internautes de demander à ce que leurs renseignements personnels soient modifiées, par exemple, dans une liste de diffusion. Le droit de suppression s’entend comme étant la possibilité offerte aux internautes de demander à ce que leurs renseignements personnels ne figurent plus, par exemple, dans une liste de diffusion.</p>
						</div>
						<div class="policyParaContainer">
							<h3>Comment accéder à mes données personnelles ?</h3>
							<p>Vos données personnelles sont stockées soit dans nos bases de données et dans celles de nos prestataires de services. Nous nous engageons à reconnaître un droit d’accès et de rectification aux personnes concernées désireuses de consulter, modifier, voire supprimer les informations les concernant.</p>
						</div>
						<div class="policyParaContainer">
							<h3>Partageons-nous vos données personnelles ?</h3>
							<p>A moins que nous n’ayons préalablement obtenu votre consentement, nous ne communiquons pas vos données personnelles. Veuillez noter que nous exigeons de manière stricte de nos prestataires de services et nos sous-traitants qu’ils utilisent vos données personnelles uniquement pour gérer les services que nous leur demandons de fournir. Nous demandons également à nos prestataires de toujours agir en conformité avec les lois applicables en matière de protection de données personnelles et d’accorder une attention particulière à la confidentialité de ces données.</p>
						</div>
						<div class="policyParaContainer">
							<h3>Sécurité</h3>
							<p>Les données personnelles que nous collectons sont conservées dans un environnement sécurisé. Nous avons pour objectif de toujours stocker vos données personnelles de la manière la plus sûre et la plus sécurisée, et uniquement pendant la durée nécessaire à la réalisation de la finalité poursuivie par le traitement. Dans cette perspective, nous prenons les mesures physiques, techniques et organisationnelles appropriées pour empêcher dans toute la mesure du possible toute altération ou perte de vos données ou tout accès non autorisé à celles-ci.</p>
						</div>
						<div class="policyParaContainer">
							<h3>Durée de conservation des données</h3>
							<p>Les données personnelles sont conservées uniquement le temps nécessaire à l’accomplissement de l’objectif qui était poursuivi lors de leur collecte. Les données personnelles collectées suite au téléchargement d’une de nos publications ou d’une inscription à notre newsletter sont conservées durant 3 ans.</p>
						</div>
						<div class="policyParaContainer">
							<h3>Législation</h3>
							<p>Nous nous engageons à respecter les dispositions législatives énoncées dans la loi  « Informatique et Libertés » du 6 janvier 1978, modifiée par la loi du 6 août 2004, ainsi que le Règlement Européen sur la protection des données (mis en vigueur le 28 mai 2018).</p>
						</div>
						<div class="policyParaContainer">
		​					<h3>Cookies</h3>
							<p>Les cookies ont pour but de collecter des informations relatives à votre navigation et de vous adresser des services adaptés à votre terminal (ordinateur, mobile ou tablette). Ils nous permettent d'établir des statistiques de fréquentation, d'utilisation des rubriques et contenus de notre site afin d'améliorer notre service ou de détecter des dysfonctionnements. Les cookies sont gérés par votre navigateur internet. Vous pouvez à tout moment choisir de les désactiver.</p>
						</div>
						<div class="policyParaContainer">
							<h3>L’exercice de ce droit se fera:</h3>
							<p>ANPEP. 301 Av. Philippe de Girard. 84400 APT</p>
							<p>Courriel : contact@anpep.fr</p>
							<p>Téléphone : 04 90 74 25 47</p>
							<p>Site web : <a href="https://www.anpep.fr">www.anpep.fr</a></p>
						</div>
					</div>
				</div>
			</div>

			
			
			<!-- FOOTER -->
			<footer id="footer" class="sm-col12 md-col9 lg-col9">
				<a href="#privacy" id="privacyAndLegalLink">Politique de confidentialité et Mentions légales</a>
			</footer>
		</div>
	</body>
</html>