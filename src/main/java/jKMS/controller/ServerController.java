package jKMS.controller;

import javax.servlet.ServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServerController extends AbstractServerController {

	@RequestMapping(value = "/index")
	public String index(ServletRequest request) {
		return "index";
	}
	
	@RequestMapping("/settings")
	public String settigs()	{
		return "settings";
	}
}
