package jKMS.controller;

import jKMS.exceptionHelper.CreateFolderFailedException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	/*
	 * Show login page, init
	 */
	@RequestMapping("/login")
	public String login() throws CreateFolderFailedException	{
		ControllerHelper.init();
		return "login";
	}
}
