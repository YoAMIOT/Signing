package YoannAMIOT.ANPEPSigning.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import YoannAMIOT.ANPEPSigning.repositories.UserRepository;
import YoannAMIOT.ANPEPSigning.entities.User;

@Controller
public class UserAdderController {
	
	@Autowired
	private UserRepository UserRepo;
	
	@GetMapping("/addUser")
	public String showAddUserForm(@ModelAttribute User user) {
		return "UserAdder";
	}
	
	
	
    @PostMapping("/addUser")
    public @ResponseBody void addUser(@ModelAttribute User user) {
    	
    	//ENCRYPTING OF THE PASSWORD//
    	user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
    	
    	//FORCING THE LASTNAME IN UPPERCASE//
    	user.setLastName(user.getLastName().toUpperCase());
    	
    	//FORCING THE FIRST LETTER OF FIRSTNAME IN UPPERCASE//
    	String FirstLetter = user.getFirstName().substring(0,1).toUpperCase();
    	String Rest = user.getFirstName().substring(1);
    	user.setFirstName(FirstLetter + Rest);
    	
    	//SAVING TO DB//
    	UserRepo.save(user);
    }
}