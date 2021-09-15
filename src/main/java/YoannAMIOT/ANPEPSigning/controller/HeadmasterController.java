package YoannAMIOT.ANPEPSigning.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import YoannAMIOT.ANPEPSigning.entities.Classroom;
import YoannAMIOT.ANPEPSigning.entities.History;
import YoannAMIOT.ANPEPSigning.entities.User;
import YoannAMIOT.ANPEPSigning.repositories.ClassroomRepository;
import YoannAMIOT.ANPEPSigning.repositories.HistoryRepository;
import YoannAMIOT.ANPEPSigning.repositories.UserRepository;

@Controller
public class HeadmasterController {
	
    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HistoryRepository historyRepository;

	//GET OF THE HEADMASTER HOME PAGE//
	@GetMapping("/headmaster")
	public String showHeadmasterHome(HttpServletRequest request) {
		
        //Checking if the user is still connected and if not we to the login page
        HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if(user == null || user.getResponsability() != 2) {
			return "redirect:/login";
		}
        
		return "headmasterHomePage";
	}
	
	
	
	//GET OF THE HEADMASTER CLASSROOMS PAGE//
	@GetMapping("/headmaster/classrooms")
	public String showHeadmasterClassrooms(HttpServletRequest request, @ModelAttribute Classroom classroom) {
		
		//Attributes variables for the JSP
		boolean classroomSelected = false;
		List <Classroom> classrooms = new ArrayList<Classroom>();
		List <User> teachers = new ArrayList<User>();
		
        //Checking if the user is still connected and if not we redirect to the login page
        HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if(user == null || user.getResponsability() != 2) {
			return "redirect:/login";
		}
        
    	//Checking if there's any existing Classroom
        boolean anyExistingClassroom = classroomRepository.existsAnyClassroom();
        
        //If there's any existing classroom we get 'em all
        if(anyExistingClassroom == true) {
        	classrooms = classroomRepository.findAll();
        }
        
        //Checking if there's any existing Teacher
        boolean anyExistingTeacher = userRepository.existsAnyUserWithResponsability(1);
        
        //If there's any existing teacher we get the list of all the teachers
        if(anyExistingTeacher == true) {
        	teachers = userRepository.findAllUserByResponsability(1);
        }
        
    	//Attributes for the JSP
        request.setAttribute("anyExistingTeacher", anyExistingTeacher);
        request.setAttribute("teachers", teachers);
        request.setAttribute("classroomSelected", classroomSelected);
        request.setAttribute("anyExistingClassroom", anyExistingClassroom);
        request.setAttribute("classrooms", classrooms);
        
        
		return "headmasterClassroom";
	}
	
	
	
	//GET OF THE HEADMASTER CLASSROOM PAGE//
	@GetMapping("/headmaster/classroom/{id}")
	public String showHeadmasterClassroom(@PathVariable String id, HttpServletRequest request, @ModelAttribute Classroom classroom) {
		
		//Local variables
		int counter = 0;
		
		//Attributes variables for the JSP
		boolean classroomSelected = true;
		List <Classroom> classrooms = new ArrayList<Classroom>();
		List <Integer> studentsId = new ArrayList<Integer>();
		boolean canCreateSchoolDay = false;
		List <User> teachers = new ArrayList<User>();
		List <User> allStudentsNotInThisClassroom = new ArrayList<User>();
		List <User> studentsOfClassroom = new ArrayList<User>();
		
        //Checking if the user is still connected and if not we redirect to the login page
        HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if(user == null || user.getResponsability() != 2) {
			return "redirect:/login";
		}
        
    	//Checking if there's any existing Classroom
        boolean anyExistingClassroom = classroomRepository.existsAnyClassroom();
        
        //If there's any existing classroom we get 'em all
        if(anyExistingClassroom == true) {
        	classrooms = classroomRepository.findAll();
        }
        
		//Getting the selected classroom
		Classroom selectedClassroom = classroomRepository.findById(Integer.parseInt(id));
        
		//Checking if there is any existing student in this classroom
		boolean studentInThisClassroomExists = classroomRepository.existsStudentInThisClassroom(selectedClassroom.getId());
		
        //Getting the actual time and date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
		
		//If ther's any existing student in this classroom we get all the ids of the students
		if(studentInThisClassroomExists == true) {
			studentsId = classroomRepository.findStudentsIdByClassroomId(selectedClassroom.getId());
			
			//For each students id in the list
			for (Integer s : studentsId) {
				//Add in the list of students of the class the student we get by it's id
				studentsOfClassroom.add(userRepository.findStudentById(s));
				//Check if the student already has a history for today
				boolean historyAlreadyExists = historyRepository.existsByStudentIdAndDate(s, currentDate);
				//If the student already has a history we add 1 to the counter
				if(historyAlreadyExists == true) {
					counter += 1;
				}
			}
		}
		
		if((studentsId.size() - counter) >= 1 ){
			canCreateSchoolDay = true;
		}
		
        //Checking if there's any existing Teacher
        boolean anyExistingTeacher = userRepository.existsAnyUserWithResponsability(1);
        
        //If there's any existing teacher we get the list of all the teachers
        if(anyExistingTeacher == true) {
        	teachers = userRepository.findAllUserByResponsability(1);
        }
        
        //Checking if there's any existing student
        boolean anyExistingStudent = userRepository.existsAnyUserWithResponsability(0);
        
        //If there's any existing student we get the list of all the students not in classroom
        if(anyExistingStudent == true) {
        	allStudentsNotInThisClassroom = userRepository.findAllStudentNotInClassroom(Integer.parseInt(id));
        }
		
    	//Attributes for the JSP
		request.setAttribute("selectedClassroom", selectedClassroom);
        request.setAttribute("classroomSelected", classroomSelected);
        request.setAttribute("anyExistingClassroom", anyExistingClassroom);
        request.setAttribute("classrooms", classrooms);
		request.setAttribute("canCreateSchoolDay", canCreateSchoolDay);
        request.setAttribute("teachers", teachers);
        request.setAttribute("allStudentsNotInThisClassroom", allStudentsNotInThisClassroom);
        request.setAttribute("studentInThisClassroomExists", studentInThisClassroomExists);
        request.setAttribute("studentsOfClassroom", studentsOfClassroom);
        request.setAttribute("anyExistingStudent", anyExistingStudent);

        
		return "headmasterClassroom";
	}
	
	
	
	//GET OF THE HEADMASTER USERS PAGE//
	@GetMapping("/headmaster/users")
	public String showHeadmasterUsers(HttpServletRequest request, @ModelAttribute User user) {
		
		//Attributes variables for the JSP
		boolean userSelected = false;
		List <User> allHeadmasters = new ArrayList<User>();
		List <User> allTeachers = new ArrayList<User>();
		List <User> allStudents = new ArrayList<User>();
 		
        //Checking if the user is still connected and if not we redirect to the page
        HttpSession session = request.getSession(true);
		User u = (User) session.getAttribute("user");
		if(u == null || u.getResponsability() != 2) {
			return "redirect:/login";
		}
        
        //Get of all the headmasters
		allHeadmasters = userRepository.findAllUserByResponsability(2);
        
        //Checking if there's any existing Teacher
        boolean anyExistingTeacher = userRepository.existsAnyUserWithResponsability(1);
        
        //If there's any existing teacher we get the list of all the teachers
        if(anyExistingTeacher == true) {
        	allTeachers = userRepository.findAllUserByResponsability(1);
        }
        
        //Checking if there's any existing Student
        boolean anyExistingStudent = userRepository.existsAnyUserWithResponsability(1);
        
        //If there's any existing student we get the list of all the teachers
        if(anyExistingStudent == true) {
        	allStudents = userRepository.findAllUserByResponsability(0);
        }
		
    	//Attributes for the JSP
        request.setAttribute("userSelected", userSelected);
        request.setAttribute("anyExistingTeacher", anyExistingTeacher);
        request.setAttribute("anyExistingStudent", anyExistingStudent);
        request.setAttribute("allStudents", allStudents);
        request.setAttribute("allTeachers", allTeachers);
        request.setAttribute("allHeadmasters", allHeadmasters);
        
		return "headmasterUser";
	}
	
	
	
	//GET OF THE HEADMASTER USER PAGE//
	@GetMapping("/headmaster/user/{id}")
	public String showHeadmasterUser(@PathVariable String id, HttpServletRequest request) {
		
		//Attributes variables for the JSP
		boolean userSelected = true;
 		
        //Checking if the user is still connected and if not we redirect to the login page
        HttpSession session = request.getSession(true);
		User u = (User) session.getAttribute("user");
		if(u == null || u.getResponsability() != 2) {
			return "redirect:/login";
		}
		
		//Get the user
		User selectedUser = userRepository.findStudentById(Integer.parseInt(id));

		//We get all the history where the student was absent
		List<History> absentHistories = historyRepository.findByStudentsNotSigned(Integer.parseInt(id));
		//Set the absent History of the student
		selectedUser.setAbsentHistories(absentHistories);

		
    	//Attributes for the JSP
        request.setAttribute("userSelected", userSelected);
        request.setAttribute("selectedUser", selectedUser);
		
		return "headmasterUser";
	}
	
	
	
	//GET OF THE HEADMASTER CREATE SCHOOL DAY//
	@GetMapping("/headmaster/headmasterCreateSchoolDay/{id}")
	public String createShoolDayForClassroom(@PathVariable String id, HttpServletRequest request) {
		
		//Checking if the user is still connected and if yes getting the user id
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if(user == null || user.getResponsability() != 2) {
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

		return "redirect:/headmaster/classroom/" + id;
	}
	
	
	
	//POST OF THE ADD CLASSROOM//
    @PostMapping("/headmaster/addClassroom")
    public String addClassroom(@ModelAttribute Classroom classroom, HttpServletRequest request) {

    	classroomRepository.save(classroom);
    	
    	return "redirect:/headmaster/classrooms";
    }
    
    
    
	//POST OF THE UPDATE CLASSROOM//
    @PostMapping("/headmaster/classroom/updateClassroom/{id}")
    public String updateClassroom(@PathVariable String id, HttpServletRequest request, @ModelAttribute Classroom classroom) {
    	
    	//Get the id of the teacher
    	int teacherId = classroom.getTeacher().getId();
    	
    	//Cast the id in string to int
    	int classroomId = Integer.parseInt(id);
    	
    	//Update the classroom in DB
    	classroomRepository.updateClassroom(classroom.getName(), classroom.getStartDate(), classroom.getEndDate(), teacherId, classroomId);
    	
    	return "redirect:/headmaster/classroom/" + id;    
    }
    
    
    
    //POST OF THE ADD USER//
    @PostMapping("/headmaster/addUser")
    public String addUser(@ModelAttribute User user) {
    	
    	//ENCRYPTING OF THE PASSWORD//
    	user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    	
    	//FORCING THE LASTNAME IN UPPERCASE//
    	user.setLastName(user.getLastName().toUpperCase());
    	
    	//FORCING THE FIRST LETTER OF FIRSTNAME IN UPPERCASE//
    	String FirstLetter = user.getFirstName().substring(0,1).toUpperCase();
    	String Rest = user.getFirstName().substring(1);
    	user.setFirstName(FirstLetter + Rest);
    	
    	//SAVING TO DB//
    	userRepository.save(user);
    	
    	return "redirect:/headmaster/users";
    }
    
    
    
	//POST OF THE ADD STUDENT TO CLASSROOM//
    @PostMapping("/headmaster/classroom/addStudent/{id}")
    public String addStudentToClassroom(@PathVariable String id, HttpServletRequest request) {
    	
    	//Get the id of the student to add
    	int idStudent = Integer.parseInt(request.getParameter("student"));
    	
    	//We add the user to the classroom;
    	classroomRepository.addUserToClassroom(idStudent, Integer.parseInt(id));
    	
    	return "redirect:/headmaster/classroom/" + id;    
    }
    
    
    
	//POST OF THE REMOVE STUDENT FROM CLASSROOM//
    @PostMapping("/headmaster/classroom/removeStudent/{id}")
    public String removeStudentToClassroom(@PathVariable String id, HttpServletRequest request) {
    	
    	//Get the id of the student to add
    	int idStudent = Integer.parseInt(request.getParameter("student"));
    	
    	//We add the user to the classroom;
    	classroomRepository.removeUserFromClassroom(idStudent, Integer.parseInt(id));
    	
    	return "redirect:/headmaster/classroom/" + id;    
    }
}