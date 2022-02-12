<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="fr">
	<head>
	    <meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	    <title>Connexion</title>
	</head>
	
	
	
	<body>	    
		<!-- LOGIN FORM -->
		<div id="loginFormContainer" class="sm-col12 md-col10 lg-col10 font2em">
	    	<form:form id="form" method="POST" action="login" modelAttribute="user" acceptCharset="utf-8">
				<div id="formContainer">
					<div>
						<label for="email">Votre adresse Email:</label>
						<form:input path="username" class="font0dot8em" id="email" placeholder="Email" required="required" autocomplete="off"/>
					</div>
					
					<div>
						<label for="password">Votre mot de passe:</label>
						<form:input path="password" class="font0dot8em" id="password" type="password" placeholder="Mot de passe" required="required" autocomplete="off"/>
					</div>
	
					<div>
						<button type="submit" class="font1dot5em" id="submitLoginFormButton" disabled="true">Se connecter</button>
					</div>
				</div>
			</form:form>
			
			
			
			<script>
				let regexEmail = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
				const SUBMIT_BUTTON = document.getElementById("submitLoginFormButton");

				/* Event Listener */
				document.getElementById("email").addEventListener("input", checkEmail);

				/* Function to Check the mail */
				function checkEmail(){
				    if(this.value != ""){
				    	if(regexEmail.test(this.value)){
				    		SUBMIT_BUTTON.disabled = false;
					    }else{
					    	SUBMIT_BUTTON.disabled = true;
					    }
				    }
				}
			</script>
	    </div>
	</body>
</html>