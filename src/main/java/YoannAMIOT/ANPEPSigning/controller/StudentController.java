package YoannAMIOT.ANPEPSigning.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import YoannAMIOT.ANPEPSigning.entities.History;
import YoannAMIOT.ANPEPSigning.entities.User;
import YoannAMIOT.ANPEPSigning.repositories.HistoryRepository;

@Controller
public class StudentController {
	
    @Autowired
    private HistoryRepository historyRepository;
	
	//SIGNING HOURS//
	//In the morning the students can sign between "startMorning" and "stopMorning"
	LocalTime startMorning = LocalTime.parse( "08:30:00" );
    LocalTime stopMorning = LocalTime.parse( "09:30:00" );
    //In the afternoon the students can sign between "startAfternoon" and "stopAfternoon"
    LocalTime startAfternoon = LocalTime.parse( "13:00:00" );
    LocalTime stopAfternoon = LocalTime.parse( "14:00:00" );

    
    //GET OF THE STUDENT HOME PAGE//
	@GetMapping("/studentHome")
	public String showStudentHome(@ModelAttribute History history, HttpServletRequest request) {
		
		//Attributes variables for the JSP
		Boolean morningTest = false;
		Boolean afternoonTest = false;
		Boolean morningSigned = false;
		Boolean afternoonSigned = false;
		List <History> absencesHistories = new ArrayList<History>();
		
		//Getting the Student's Id
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		int idUser = 0;
		
		//Checking if the user is still connected and if yes getting the user id
		if(user != null && user.getResponsability() == 0) {
			idUser = user.getId();
		} else {
			return "redirect:/login";
		}
		
        //Getting today's date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
                
        //Searching if a history exist for today
        boolean todaysHistoryExists = historyRepository.existsByStudentIdAndDate(idUser, currentDate);
        
        //If there's an existing history for today we get the today's History and check the actual time to show what is needed
        if (todaysHistoryExists == true) {
        	//Getting today's history
        	History h = historyRepository.findByStudentIdAndDate(idUser, currentDate);
        	
        	//Getting the actual time
        	LocalTime currentTime = LocalTime.now();
        	
        	//Checking if the actual time is in one of the time slot
        	morningTest = (currentTime.isAfter(startMorning) && currentTime.isBefore(stopMorning));
        	afternoonTest = (currentTime.isAfter(startAfternoon) && currentTime.isBefore(stopAfternoon));
        	
        	//Checking if the student already signed
        	morningSigned = h.isMorningSign();
        	afternoonSigned = h.isAfternoonSign();
        }
        
        //Searching if the connected user has absent histories
        boolean absenceHistoryExists = historyRepository.existsByStudentIdAndNotSigned(idUser);
        
        //If there's existing history where the student was absent, we search in DB all the absences histories
        if (absenceHistoryExists == true) {
        	absencesHistories = historyRepository.findByStudentsAndNotSigned(idUser, currentDate);
        }

        //Attributes for the JSP
        request.setAttribute("todaysHistoryExists", todaysHistoryExists);
        request.setAttribute("morningTest", morningTest);
        request.setAttribute("afternoonTest", afternoonTest);
        request.setAttribute("startMorning", startMorning);
        request.setAttribute("stopMorning", stopMorning);
        request.setAttribute("startAfternoon", startAfternoon);
        request.setAttribute("stopAfternoon", stopAfternoon);
        request.setAttribute("morningSigned", morningSigned);
        request.setAttribute("afternoonSigned", afternoonSigned);
        request.setAttribute("absenceHistoryExists", absenceHistoryExists);
        request.setAttribute("absencesHistories", absencesHistories);
        
        //Return the JSP
		return "studentHomePage";
	}
	
	
	
	//POST OF THE MORNING SIGN//
	@PostMapping("/morningSigning")
	public String submitMorningSign(HttpServletRequest request) {
		
        //Getting the Student's Id
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idUser = 0;
        
        //Checking if the user is still connected and if yes getting the user id
		if(user != null && user.getResponsability() == 0) {
			idUser = user.getId();
		} else {
			return "redirect:/login";
		}
        
        //Getting today's date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
        
    	//Getting the actual time
    	LocalTime currentTime = LocalTime.now();
    	
    	//Checking if the actual time is in the morning time slot 
        Boolean morningTest = (currentTime.isAfter(startMorning) && currentTime.isBefore(stopMorning));
        
        if(morningTest == true) {
        	historyRepository.updateMorningSignByStudent(idUser, currentDate);
        }
        
		return "redirect:studentHome";
	}
	
	
	
	//POST OF THE AFTERNOON SIGN//
	@PostMapping("/afternoonSigning")
	public String submitAfternoonSign(HttpServletRequest request) {
		
        //Getting the Student's Id
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idUser = 0;
        
        //Checking if the user is still connected and if yes getting the user id
		if(user != null && user.getResponsability() == 0) {
			idUser = user.getId();
		} else {
			return "redirect:/login";
		}
        
        //Getting today's date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
        
    	//Getting the actual time
    	LocalTime currentTime = LocalTime.now();
    	
    	//Checking if the actual time is in the morning time slot 
    	Boolean afternoonTest = (currentTime.isAfter(startAfternoon) && currentTime.isBefore(stopAfternoon));
        
        if(afternoonTest == true) {
        	historyRepository.updateAfternoonSignByStudent(idUser, currentDate);
        }
        
		return "redirect:studentHome";
	}
}