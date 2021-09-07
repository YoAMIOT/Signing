package YoannAMIOT.ANPEPSigning.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    
    
    
    //GET OF THE TEACHER HOME PAGE//
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

    	//Checking if there's a Classroom where the connected teacher is the main teacher
        boolean classroomWithTeachExists = classroomRepository.existsClassroomWithThisMainTeach(idUser);
        
        //If there's at least one classroom with the connected teacher as the main teacher we get all this classrooms
        if (classroomWithTeachExists == true) {
        	classrooms = classroomRepository.findByTeacherId(idUser);
        }
    	
    	//Attributes for the JSP
        request.setAttribute("classrooms", classrooms);
        request.setAttribute("classroomWithTeachExists", classroomWithTeachExists);
        
		return "teacherHomePage";
	}
	
	
	
	//GET OF THE CLASSROOM PAGE FOR TEACHER//
	@GetMapping("/teacherClassroom/{id}")
	public String showClassroom(@PathVariable String id, HttpServletRequest request) {
		
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

    	//Checking if there's a Classroom where the connected teacher is the main teacher
        boolean classroomWithTeachExists = classroomRepository.existsClassroomWithThisMainTeach(idUser);
        
        //If there's at least one classroom with the connected teacher as the main teacher we get all this classrooms
        if (classroomWithTeachExists == true) {
        	classrooms = classroomRepository.findByTeacherId(idUser);
        }
		
		//Getting the Classroom id
		Classroom classroom = classroomRepository.findById(Integer.parseInt(id));
		
        //Getting the actual time
        LocalTime currentTime = LocalTime.now();
        
    	//Checking if the actual time is in one of the time slot
    	Boolean createSchoolDayTest = (currentTime.isAfter(startCreateDay) && currentTime.isBefore(stopCreateDay));
    	Boolean	countersignTest = (currentTime.isAfter(countersignStart) && currentTime.isBefore(countersignStop));

		//Attributes for the JSP
    	request.setAttribute("createSchoolDayTest", createSchoolDayTest);
    	request.setAttribute("countersignTest", countersignTest);
		request.setAttribute("classroom", classroom);
    	request.setAttribute("countersignStart", countersignStart);
        request.setAttribute("countersignStop", countersignStop);
    	request.setAttribute("startCreateDay", startCreateDay);
        request.setAttribute("stopCreateDay", stopCreateDay);
        request.setAttribute("classrooms", classrooms);
        request.setAttribute("classroomWithTeachExists", classroomWithTeachExists);
        
        
		return ("teacherClassroom");
	}
}
