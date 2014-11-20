package jKMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController extends AbstractController {
	
	@RequestMapping("/")
	public String redirect() {
		return "redirect:/contract";
	}
	
	@RequestMapping("/contract")
	public String contract()	{
		return "contract";
	}

}
