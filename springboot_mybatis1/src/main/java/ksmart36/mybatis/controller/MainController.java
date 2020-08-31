package ksmart36.mybatis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	// request get방식 http://localhost
	@GetMapping("/main")
	public String main(Model model) {
		model.addAttribute("title", "main 화면입니당");
		return "main";		
	}

}
