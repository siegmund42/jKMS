package jKMS.controller;

import jKMS.Application;
import jKMS.LogicHelper;
import jKMS.exceptionHelper.CreateFolderFailedException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController extends AbstractController	{

	/*
	 * Show login page, init
	 */
	@RequestMapping("/login")
	public String login() throws CreateFolderFailedException	{
		ControllerHelper.init(messageSource);
		LogicHelper.init(messageSource);
		Application.gui.setReady();
		return "login";
	}
}
