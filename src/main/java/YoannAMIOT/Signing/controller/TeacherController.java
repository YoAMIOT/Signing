package YoannAMIOT.Signing.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import YoannAMIOT.Signing.entities.Classroom;
import YoannAMIOT.Signing.entities.History;
import YoannAMIOT.Signing.entities.User;
import YoannAMIOT.Signing.repositories.ClassroomRepository;
import YoannAMIOT.Signing.repositories.HistoryRepository;
import YoannAMIOT.Signing.repositories.UserRepository;

@Controller
public class TeacherController {
	
    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private HistoryRepository historyRepository;
    
    @Autowired
    private UserRepository userRepository;
	
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
		if(user != null && user.getResponsability() == 1) {
			idUser = user.getId();
		} else {
			return "redirect:/login";
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
	public String showClassroom(@PathVariable String id, HttpServletRequest request, @ModelAttribute History history) {
		
		//Local variables
		int counter = 0;
		int countersignedCounter = 0;
		
		//Attributes variables for the JSP
		List <Classroom> classrooms = new ArrayList<Classroom>();
		List <Integer> studentsId = new ArrayList<Integer>();
		List <User> students = new ArrayList<User>();
		boolean canCreateSchoolDay = false;
		boolean alreadyCountersigned = false;
		
        //Checking if the user is still connected and if yes getting the user id
        HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		int idUser = 0;
		if(user != null && user.getResponsability() == 1) {
			idUser = user.getId();
		} else {
			return "redirect:/login";
		}

    	//Checking if there's a Classroom where the connected teacher is the main teacher
        boolean classroomWithTeachExists = classroomRepository.existsClassroomWithThisMainTeach(idUser);
        
        //If there's at least one classroom with the connected teacher as the main teacher we get all this classrooms
        if (classroomWithTeachExists == true) {
        	classrooms = classroomRepository.findByTeacherId(idUser);
        }
		
		//Getting the Classroom
		Classroom classroom = classroomRepository.findById(Integer.parseInt(id));
		
        //Getting the actual time and date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
        LocalTime currentTime = LocalTime.now();
        
    	//Checking if the actual time is in one of the time slot
    	Boolean createSchoolDayTest = (currentTime.isAfter(startCreateDay) && currentTime.isBefore(stopCreateDay));
    	Boolean	countersignTest = (currentTime.isAfter(countersignStart) && currentTime.isBefore(countersignStop));

		//Checking if there is any existing student in this classroom
		boolean studentInThisClassroomExists = classroomRepository.existsStudentInThisClassroom(classroom.getId());
		
		//If ther's any existing student in this classroom we get all the ids of the students and all the students
		if(studentInThisClassroomExists == true) {
			studentsId = classroomRepository.findStudentsIdByClassroomId(classroom.getId());
			
			//For each students id in the list
			for (Integer s : studentsId) {
				//Check if the student already has a history for today
				boolean historyAlreadyExists = historyRepository.existsByStudentIdAndDate(s, currentDate);
				//If the student already has a history we add 1 to the counter
				if(historyAlreadyExists == true) {
					counter += 1;
				}
				//Check if the student is already countersigned for today
				boolean studentAlreadyCountersigned = historyRepository.existsStudentByCountersigned(s, currentDate);
				//If the student is already countersigned we add 1 to the counter
				if(studentAlreadyCountersigned == true) {
					countersignedCounter += 1;
				}
				//We get all the history where the student was absent
				List<History> absentHistories = historyRepository.findByStudentsNotSigned(s);
				//Get the student and add it into the "students" list
				User u = userRepository.findStudentById(s);
				//Set the absent History of the student
				u.setAbsentHistories(absentHistories);
				//Adding the student to the list of students
				students.add(u);
			}
		}

		//Check if the difference between the size of the list and the counter is greater than 1 and if yes the teach can create a school day
		if((studentsId.size() - counter) >= 1 ){
			canCreateSchoolDay = true;
		}
		
		//Check if the difference between the size of the list and the counter is greater than 1 and if yes the teach can create a school day
		if((studentsId.size() - countersignedCounter) >= 1 ){
			alreadyCountersigned = true;
		}
		
		//Attributes for the JSP
		request.setAttribute("alreadyCountersigned", alreadyCountersigned);
		request.setAttribute("students", students);
		request.setAttribute("studentInThisClassroomExists", studentInThisClassroomExists);
		request.setAttribute("canCreateSchoolDay", canCreateSchoolDay);
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
	
	
	
	//GET OF THE CREATE SCHOOL DAY//
	@GetMapping("/createSchoolDay/{id}")
	public String createSchoolDayForAClassroom(@PathVariable String id, HttpServletRequest request) {
		
		//Checking if the user is still connected and if yes getting the user id
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if(user == null || user.getResponsability() != 1) {
			return "redirect:/login";
		}
		
		//Getting the Classroom
		Classroom classroom = classroomRepository.findById(Integer.parseInt(id));
		
		
		//Checking if there is any existing student in this classroom
		boolean studentInThisClassroomExists = classroomRepository.existsStudentInThisClassroom(classroom.getId());
		
        //Getting today's date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
		
		//If ther's any existing student in this classroom we get all the ids of the students
		if(studentInThisClassroomExists == true) {
			List<Integer> studentsId = classroomRepository.findStudentsIdByClassroomId(classroom.getId());
			
			//For each students id in the list check if the student already has a history for today
			for (Integer s : studentsId) {
				boolean historyAlreadyExists = historyRepository.existsByStudentIdAndDate(s, currentDate);
				//If the student doesn't have a history for today we create one
				if(historyAlreadyExists == false) {
					History h = new History();
					User u = userRepository.findStudentById(s);
					h.setStudent(u);
					h.setDate(currentDate);
					historyRepository.save(h);						
				}
			}
		}
		
		return "redirect:/teacherClassroom/" + id;
	}
	
	
	
	//POST OF THE COUNTERSIGN//
	@PostMapping("/countersign/{id}")
	public String submitCountersign(@PathVariable String id,  HttpServletRequest request) {
		
        //Checking if the user is still connected and if yes getting the user id
        HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if(user == null || user.getResponsability() != 1) {
			return "redirect:/login";
		}
		
		//Getting the Classroom
		Classroom classroom = classroomRepository.findById(Integer.parseInt(id));
		
		//Getting today's date
		long millis = System.currentTimeMillis();
		java.sql.Date currentDate = new java.sql.Date(millis);
			
		//Getting all the students ids
		List<Integer> studentsId = classroomRepository.findStudentsIdByClassroomId(classroom.getId());
		
		//For each students id in the list
		for (Integer s : studentsId) {	
			//We get the param from the form
	        String morningCheckStr = request.getParameter("morningCheck" + s);
	        String afternoonCheckStr = request.getParameter("afternoonCheck" + s);
	        //We parse the param that are strings into booleans
	        boolean morningCheck = Boolean.parseBoolean(morningCheckStr);
	        boolean afternoonCheck = Boolean.parseBoolean(afternoonCheckStr);
	        //We update the DB with the new datas
	        historyRepository.updateStudentHistoryWithTeachCheck(morningCheck, afternoonCheck, s, currentDate);
		}
		
		return "redirect:/teacherClassroom/" + id;
	}
}
