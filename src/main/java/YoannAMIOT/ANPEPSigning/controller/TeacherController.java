package YoannAMIOT.ANPEPSigning.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import YoannAMIOT.ANPEPSigning.entities.Classroom;
import YoannAMIOT.ANPEPSigning.entities.User;
import YoannAMIOT.ANPEPSigning.repositories.ClassroomRepository;

@Controller
public class TeacherController {
	
    @Autowired
    private ClassroomRepository classroomRepository;
	
	//CREATE HISTORIES HOUR//
	//The Teacher can create a school day between "startCreateDay" and "stopCreateDay"
	LocalTime startCreateDay = LocalTime.parse( "08:30:00" );
    LocalTime stopCreateDay = LocalTime.parse( "09:00:00" );
	
	//COUNTERSIGNATURE HOURS//
	//The teacher can countersign between "countersignStart" and "countersignStop" 
	LocalTime countersignStart = LocalTime.parse( "16:00:00" );
    LocalTime countersignStop = LocalTime.parse( "16:30:00" );
    
    
    
    //GET OF THE TEACHER HOME PAGE
	@GetMapping("/teacherHome")
	public String showTeacherHome(HttpServletRequest request) {
        
		//Attributes variables for the JSP
		List <Classroom> classrooms = new ArrayList<Classroom>();

		
        //Checking if the user is still connected and if yes getting the user id
        HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		int idUser = 0;
        if(session.getAttribute("user") != null) {
        	idUser = user.getId();
        } else {
        	return "redirect:login";
        }

        //Getting the actual time
        LocalTime currentTime = LocalTime.now();
        
    	//Checking if the actual time is in one of the time slot
    	Boolean createSchoolDayTest = (currentTime.isAfter(startCreateDay) && currentTime.isBefore(stopCreateDay));
    	Boolean	countersignTest = (currentTime.isAfter(countersignStart) && currentTime.isBefore(countersignStop));
    		
    	//Checking if there's a Classroom where the connected teacher is the main teacher
        boolean classroomWithTeachExists = classroomRepository.existsClassroomWithThisMainTeach(idUser);
        
        //If there's at least one classroom with the connected teacher as the main teacher we get all this classrooms
        if (classroomWithTeachExists == true) {
        	classrooms = classroomRepository.findByTeacherId(idUser);
        }
    	
    	//Attributes for the JSP
        request.setAttribute("classrooms", classrooms);
        request.setAttribute("classroomWithTeachExists", classroomWithTeachExists);
    	request.setAttribute("createSchoolDayTest", createSchoolDayTest);
    	request.setAttribute("countersignTest", countersignTest);
    	request.setAttribute("countersignStart", countersignStart);
        request.setAttribute("countersignStop", countersignStop);
    	request.setAttribute("startCreateDay", startCreateDay);
        request.setAttribute("stopCreateDay", stopCreateDay);
        
		return "teacherHomePage";
	}
}
