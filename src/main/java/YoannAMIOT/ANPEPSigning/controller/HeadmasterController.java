package YoannAMIOT.ANPEPSigning.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

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
		boolean canExportDatasToCSV = false;
		
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
		
        //Getting the actual date
		long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
        
        //Checking if the actual date is after the start date of the classroom
        if(currentDate.after(selectedClassroom.getStartDate())) {
        	canExportDatasToCSV = true;
        }
		
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
        request.setAttribute("canExportDatasToCSV", canExportDatasToCSV);
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
	public String showHeadmasterUser(@PathVariable String id, HttpServletRequest request, @ModelAttribute User user) {
		
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
    @PostMapping("/headmaster/classroom/addClassroom")
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
    @PostMapping("/headmaster/user/addUser")
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
    
    
    
	//POST OF THE UPDATE USER//
    @PostMapping("/headmaster/user/updateUser/{id}")
    public String updateUser(@PathVariable String id, HttpServletRequest request, @ModelAttribute User user) {

    	//ENCRYPTING OF THE PASSWORD//
    	user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    	
    	//FORCING THE LASTNAME IN UPPERCASE//
    	user.setLastName(user.getLastName().toUpperCase());
    	
    	//FORCING THE FIRST LETTER OF FIRSTNAME IN UPPERCASE//
    	String FirstLetter = user.getFirstName().substring(0,1).toUpperCase();
    	String Rest = user.getFirstName().substring(1);
    	user.setFirstName(FirstLetter + Rest);
    	
    	userRepository.updateUser(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getResponsability(), user.getId());
    	
    	return "redirect:/headmaster/user/" + id;    
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
    
    
    
	//POST OF THE ADD STUDENT TO CLASSROOM//
    @PostMapping("/headmaster/classroom/exportToCSV/{id}")
    public void exportToCSV(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	//Variables
    	response.setContentType("text/csv");
    	List<User> students = new ArrayList<User>();

    	//We get the dates from the form
        java.sql.Date exportStartDate = java.sql.Date.valueOf(request.getParameter("exportStartDate"));
        java.sql.Date exportEndDate = java.sql.Date.valueOf(request.getParameter("exportEndDate"));
        
		//Getting the Classroom
		Classroom classroom = classroomRepository.findById(Integer.parseInt(id));
        
		//We declare a string variable for the file name
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String startDateSTR = df.format(exportStartDate);
		String endDateSTR = df.format(exportEndDate);
    	String fileName = "historique_" + classroom.getName() + "_" + startDateSTR + "_" + endDateSTR + ".csv";

		//Setting the header
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		
		//Initialize CSV writers
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		//We get all the ids of the students
		List<Integer> studentsId = classroomRepository.findStudentsIdByClassroomId(classroom.getId());
			
		//For each student id of the list we get the student it refers to
		for(Integer s : studentsId) {
			User student = userRepository.findStudentById(s);
				
			//Add the student to the list of students
			students.add(student);
		}
			
		//For each user of the list we check if he has any existing history
		for(User u : students) {
			boolean anyHistoryExists = historyRepository.existsAnyHistoryByStudent(u.getId());
				
			//If the student has any existing history we get all it's histories and add 'em all into it's own history list
			if(anyHistoryExists == true) {
				List<History> studentHistories = historyRepository.findAllHistoriesBetweenDatesForClassroom(u.getId(), exportStartDate, exportEndDate);
				u.setHistories(studentHistories);
			}
		}
        
        //Initialize a List of dates
        List<Date> dates = new ArrayList<Date>();

        //If the size of the list is greater than 0
	    if(students.size() > 0) {
	    	//For each element of histories we get the date and add it to the date list
        	for (History h : students.get(0).getHistories()) {
	        	dates.add(h.getDate());
	        }
	    }
	    
	    //Initialize two strings for the histories
	    String[] csvHeader = new String[(dates.size() * 3) + 2];
	    
        
        //For each date we add the date to the string
	    int i = 2;
	    csvHeader[0] = "Nom et prenom";
	    csvHeader[1] = "|||";
	    for (Date d : dates) {
	    	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
	    	String strDate = dateFormat.format(d);  
	    	
	    	csvHeader[i] = strDate + " M";
	    	i = i + 1;
	    	csvHeader[i] = strDate + " A-M";
	    	i = i + 1;
	    	csvHeader[i] = "|||";
	    	i = i + 1;
	    }
	    
        //Write out the header
        csvWriter.writeHeader(csvHeader);
        
        //For each user we write out it's datas
        for(User u : students) {
        	String[] studentPresences = new String[(u.getHistories().size() * 3) + 2];
        	int j = 2;
        	studentPresences[0] = u.getLastName() + " " + u.getFirstName();
        	studentPresences[1] = "|||";
        	
        	//For each history of the user
        	for(History h : u.getHistories()) {
        		//If the student was here in the morning
        		if(h.isMorningCheck() == true) {
        			studentPresences[j] = "Ok";
        			j = j + 1;
        		//If the student wasn't here in the morning
        		} else if(h.isMorningCheck() == false) {
        			studentPresences[j] = "ABSENT!";
        			j = j + 1;
        		}
        		
        		//If the student was here in the afternoon
        		if(h.isAfternoonCheck() == true) {
        			studentPresences[j] = "Ok";
        			j = j + 1;
        		//If the student wasn't here in the afternoon
        		} else if(h.isAfternoonCheck() == false) {
        			studentPresences[j] = "ABSENT!";
        			j = j + 1;
        		}
        		
        		studentPresences[j] = "|||";
        		j = j + 1;
        	}
        	
        	//Write the presences
        	csvWriter.writeHeader(studentPresences);
        }

        //Close the writer
        csvWriter.close(); 
    }
}