package YoannAMIOT.ANPEPSigning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HeadmasterController {

	@GetMapping("/headmasterHome")
	public String showTeacherHome() {
		return "headmasterHomePage";
	}
}