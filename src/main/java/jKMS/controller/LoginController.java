package jKMS.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login() throws IOException	{
		ControllerHelper.checkFolders();
		return "login";
	}
}
